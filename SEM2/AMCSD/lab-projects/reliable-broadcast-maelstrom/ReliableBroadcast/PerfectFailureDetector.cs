using System;
using System.Collections.Generic;
using System.Text;

using System.Collections.Concurrent;

namespace ReliableBroadcast;

/// <summary>
/// Perfect Failure Detector (PFD) — crash-stop (fail-stop) model.
///
/// Abstraction stack position:
///   RB  →  PFD  →  PL
///
/// Algorithm (heartbeat-based):
///   Periodically send hb_request to all suspected-alive peers.
///   If we do not receive hb_reply within the timeout period, mark the process
///   as crashed and emit <see cref="PfdCrashEvent"/>.
///   Once crashed, a process is never revived (fail-stop assumption).
///
/// Timer:
///   A background thread enqueues a <see cref="PfdTimeoutEvent"/> at each tick.
///   The event processor handles it synchronously, keeping all state mutation
///   on the single event-processor thread.
/// </summary>
public sealed class PerfectFailureDetector
{
    private readonly PerfectLink _pl;
    private readonly BlockingCollection<Event> _eventQueue;

    private string _selfId = "";
    private IReadOnlyList<string> _peers = Array.Empty<string>();

    // Alive: processes we believe are still up (excludes self)
    private readonly HashSet<string> _alive = new();
    // Suspected: processes we believe have crashed
    private readonly HashSet<string> _suspected = new();
    // Received heartbeat replies since last timeout tick
    private readonly HashSet<string> _heartbeatReplied = new();

    // ── Configuration ─────────────────────────────────────────────────────────
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
        foreach (var p in _peers) _alive.Add(p);
    }

    // ── Timer thread ─────────────────────────────────────────────────────────

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

    // ── Timeout handling (on event-processor thread) ──────────────────────────

    /// <summary>
    /// Called by the event processor on each <see cref="PfdTimeoutEvent"/>.
    /// Checks replies received since last tick; suspects nodes that didn't reply.
    /// </summary>
    public IReadOnlyList<PfdCrashEvent> HandleTimeout()
    {
        var newCrashes = new List<PfdCrashEvent>();

        foreach (var p in _alive.ToList())   // ToList: snapshot before mutation
        {
            if (!_heartbeatReplied.Contains(p) && !_suspected.Contains(p))
            {
                // No reply received → suspect crash
                _suspected.Add(p);
                _alive.Remove(p);
                Log($"[PFD] CRASH detected: {p}");
                newCrashes.Add(new PfdCrashEvent(p));
            }
        }

        // Reset reply tracker and send heartbeats to still-alive peers
        _heartbeatReplied.Clear();
        foreach (var p in _alive)
        {
            _pl.Send(_selfId, p, new HeartbeatRequestBody { MsgId = _pl.NextMsgId() });
        }

        return newCrashes;
    }

    // ── Inbound heartbeat messages (on event-processor thread) ───────────────

    /// <summary>We received an hb_request from <paramref name="from"/>. Reply immediately.</summary>
    public void HandleHeartbeatRequest(string from, int incomingMsgId)
    {
        _pl.Send(_selfId, from, new HeartbeatReplyBody
        {
            InReplyTo = incomingMsgId,
            MsgId = _pl.NextMsgId()
        });
    }

    /// <summary>We received an hb_reply from <paramref name="from"/>. Record it.</summary>
    public void HandleHeartbeatReply(string from)
    {
        if (!_suspected.Contains(from))
            _heartbeatReplied.Add(from);
        // If we already suspected them, fail-stop: we ignore the late reply.
    }

    public IReadOnlySet<string> Suspected => _suspected;

    private static void Log(string m) => Console.Error.WriteLine(m);
}