using System;
using System.Collections.Generic;
using System.Text;

using System.Collections.Concurrent;

namespace ReliableBroadcast;

// processes events from blcking collection one by one (state mutation)
// sync callback without queue needed rb.OnRbDeliver -> app.HandleRbDeliver (value arrives before broadcast_ok)
// async with queue: 
// -stdin reader -> PlDeliverEvent,
// -pfd timer -> PfdTimeoutEvent, 
// -beb inbound message -> BebDeliverEvent enqueued by beb.HandleBebMessage
// -pfd timeout result -> PfdCrashEvent (nested, TryAdd)
public sealed class EventProcessor
{
    private readonly BlockingCollection<Event> _queue;
    private readonly PerfectLink _pl;
    private readonly PerfectFailureDetector _pfd;
    private readonly BestEffortBroadcast _beb;
    private readonly RbLayer _rb;
    private readonly Application _app;

    private bool _initialized;
    private string _selfId = "";

    public EventProcessor(
        BlockingCollection<Event> queue,
        PerfectLink pl,
        PerfectFailureDetector pfd,
        BestEffortBroadcast beb,
        RbLayer rb,
        Application app)
    {
        _queue = queue;
        _pl = pl;
        _pfd = pfd;
        _beb = beb;
        _rb = rb;
        _app = app;
    }

    public void Run()
    {
        foreach (var ev in _queue.GetConsumingEnumerable())
        {
            try { Dispatch(ev); }
            catch (Exception ex) { Log($"[EP] Unhandled exception in event {ev.GetType().Name}: {ex}"); }
        }
    }

    private void Dispatch(Event ev)
    {
        switch (ev)
        {
            case PlDeliverEvent { Message: var msg }:
                DispatchMaelstromMessage(msg);
                break;

            case BebDeliverEvent beb:
                _rb.HandleBebDeliver(beb);
                break;

            case PfdTimeoutEvent:
                // dont handle timeouts before init_ok sent
                if (!_initialized) break;
                var crashes = _pfd.HandleTimeout();
                foreach (var c in crashes)
                    _queue.TryAdd(c);
                break;

            case PfdCrashEvent { ProcessId: var pid }:
                _rb.HandleCrash(pid);
                break;

            default:
                Log($"[EP] Unknown event type: {ev.GetType().Name}");
                break;
        }
    }

    private void DispatchMaelstromMessage(MaelstromMessage msg)
    {
        var type = BodyHelper.GetType(msg.Body);
        Log($"[EP] plDeliver src={msg.Src} type={type}");

        switch (type)
        {
            case "init": // each node finds its own id
                HandleInit(msg);
                break;

            case "broadcast":
            {
                var value = msg.Body.GetProperty("message").GetInt32();
                _app.HandleBroadcastRequest(new AppBroadcastRequestEvent(msg, value));
                break;
            }
            case "read":
                _app.HandleReadRequest(new AppReadRequestEvent(msg));
                break;

            case "topology":
                _app.HandleTopologyRequest(new AppTopologyRequestEvent(msg));
                break;

            case "beb_broadcast":
                _beb.HandleBebMessage(msg);
                break;

            case "hb_request":
            {
                var msgId = BodyHelper.GetMsgId(msg.Body) ?? 0;
                _pfd.HandleHeartbeatRequest(msg.Src, msgId);
                break;
            }
            case "hb_reply":
                _pfd.HandleHeartbeatReply(msg.Src);
                break;

            default:
                Log($"[EP] Unrecognised message type '{type}' from {msg.Src} — ignoring");
                break;
        }
    }

    private void HandleInit(MaelstromMessage msg)
    {
        _selfId = msg.Body.GetProperty("node_id").GetString()!;
        var nodeIdsEl = msg.Body.GetProperty("node_ids");
        var allNodes = new List<string>();
        foreach (var el in nodeIdsEl.EnumerateArray())
            allNodes.Add(el.GetString()!);

        Log($"[EP] init node_id={_selfId} peers=[{string.Join(",", allNodes)}]");

        _pfd.Init(_selfId, allNodes); // init peers as all alive
        _beb.Init(_selfId, allNodes); // init peers
        _rb.Init(_selfId);
        _app.Init(_selfId);

        _rb.OnRbDeliver = (sender, value) => _app.HandleRbDeliver(sender, value);

        var inReplyTo = BodyHelper.GetMsgId(msg.Body) ?? 0;
        _pl.Send(_selfId, msg.Src, new InitOkBody
        {
            InReplyTo = inReplyTo,
            MsgId = _pl.NextMsgId()
        });

        // send hb to all peers
        _pfd.StartTimerThread();
        // initialized after all steps ready
        _initialized = true;
    }

    private static void Log(string m) => Console.Error.WriteLine(m);
}