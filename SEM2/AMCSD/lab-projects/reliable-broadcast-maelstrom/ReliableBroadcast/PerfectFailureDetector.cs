using System;
using System.Collections.Generic;
using System.Text;

using System.Collections.Concurrent;

namespace ReliableBroadcast;

/// fail stop model, heartbeat based, between rb and pl layers
/// send hb_request to all suspected-alive peers, expect hb_reply within the timeout, otherwise crash event emited (never revived)
/// timeout event enqueued at each tick
public sealed class PerfectFailureDetector
{
    private readonly PerfectLink _pl;
    private readonly BlockingCollection<Event> _eventQueue;

    private string _selfId = "";
    private IReadOnlyList<string> _peers = Array.Empty<string>();

    // procs probably alive (without self)
    private readonly HashSet<string> _alive = new();
    // procs possibly crashed
    private readonly HashSet<string> _suspected = new();
    // received heartbeat replies since last timeout tick
    private readonly HashSet<string> _heartbeatReplied = new();

    private readonly TimeSpan _tickInterval;

    public PerfectFailureDetector(PerfectLink pl, BlockingCollection<Event> eventQueue,
        TimeSpan? tickInterval = null)
    {
        _pl = pl;
        _eventQueue = eventQueue;
        _tickInterval = tickInterval ?? TimeSpan.FromMilliseconds(1000);
    }

    public void Init(string selfId, IReadOnlyList<string> allNodes)
    {
        _selfId = selfId;
        _peers = allNodes.Where(n => n != selfId).ToList();
        foreach (var p in _peers) 
        {
            _heartbeatReplied.Add(p);
            _alive.Add(p);
        }
    }

    public void StartTimerThread()
    {
        var t = new Thread(TimerLoop) { Name = "pfd-timer", IsBackground = true };
        t.Start();
    }

    private void TimerLoop()
    {
        while (true)
        {
            Thread.Sleep(_tickInterval);
            if (!_eventQueue.IsAddingCompleted)
                _eventQueue.TryAdd(new PfdTimeoutEvent());
        }
    }

    // called by event processor on each timeout event
    // checks replies received since last tick
    public IReadOnlyList<PfdCrashEvent> HandleTimeout()
    {
        var newCrashes = new List<PfdCrashEvent>();

        foreach (var p in _alive.ToList())
        {
            if (!_heartbeatReplied.Contains(p) && !_suspected.Contains(p))
            {
                _suspected.Add(p);
                _alive.Remove(p);
                Log($"[PFD] CRASH detected: {p}");
                newCrashes.Add(new PfdCrashEvent(p));
            }
        }

        // reset list of nodes that replied last tick
        _heartbeatReplied.Clear();
        foreach (var p in _alive)
        {
            _pl.Send(_selfId, p, new HeartbeatRequestBody { MsgId = _pl.NextMsgId() });
        }

        return newCrashes;
    }

    // received an hb_req from from
    public void HandleHeartbeatRequest(string from, int incomingMsgId)
    {
        _pl.Send(_selfId, from, new HeartbeatReplyBody
        {
            InReplyTo = incomingMsgId,
            MsgId = _pl.NextMsgId()
        });
    }

    // receive hb_replly and record heartbeat
    public void HandleHeartbeatReply(string from)
    {
        if (!_suspected.Contains(from))
            _heartbeatReplied.Add(from);
        // if already suspected, fail-stop by ignoring the late reply
    }

    public IReadOnlySet<string> Suspected => _suspected;

    private static void Log(string m) => Console.Error.WriteLine(m);
}