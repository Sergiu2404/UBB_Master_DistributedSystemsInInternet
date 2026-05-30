using System;
using System.Collections.Generic;
using System.Text;

using System.Collections.Concurrent;

namespace ReliableBroadcast;


// betwene rb and pl
// maelstrom doesnt route a node's own messages to itself via stdin
// so broadcast returns the self-delivery event sync so caller rb can handle it before replying to the client (preserve sequencing)
public sealed class BestEffortBroadcast
{
    private readonly PerfectLink _pl;
    private readonly BlockingCollection<Event> _eventQueue;
    private string _selfId = "";
    private IReadOnlyList<string> _peers = Array.Empty<string>();

    public BestEffortBroadcast(PerfectLink pl, BlockingCollection<Event> eventQueue)
    {
        _pl = pl;
        _eventQueue = eventQueue;
    }

    public void Init(string selfId, IReadOnlyList<string> allNodes)
    {
        _selfId = selfId;
        _peers = allNodes;
    }

    // returns a beb deliv event for self if self is in the peer list (maelstrom never loops messages back, so we handle self sync)
    public BebDeliverEvent? Broadcast(BebPayload payload)
    {
        Log($"[BEB] bebBroadcast sender={payload.Sender} value={payload.Value} → {_peers.Count} peers");
        BebDeliverEvent? selfDeliver = null;
        foreach (var peer in _peers)
        {
            if (peer == _selfId)
                // val added to deliv before bcast_ok, otherwise deliv may not contain this val
                selfDeliver = new BebDeliverEvent(_selfId, payload);
            else
                // send payload with original sender and value to each peer
                _pl.Send(_selfId, peer, new BebMessageBody { MsgId = _pl.NextMsgId(), RbData = payload });
        }
        return selfDeliver;
    }

    // inbound beb sent message
    // enqueues a beb deliver event for the rb layer to proc
    public void HandleBebMessage(MaelstromMessage msg)
    {
        if (!msg.Body.TryGetProperty("rb_data", out var rbDataEl))
        {
            Log("[BEB] Received beb_broadcast without rb_data — ignoring");
            return;
        }

        var sender = rbDataEl.GetProperty("sender").GetString() ?? msg.Src;
        var value = rbDataEl.GetProperty("value").GetInt32();
        var payload = new BebPayload(sender, value);

        Log($"[BEB] bebDeliver from={msg.Src} sender={sender} value={value}");
        if (!_eventQueue.IsAddingCompleted) _eventQueue.TryAdd(new BebDeliverEvent(msg.Src, payload));
    }

    private static void Log(string m) => Console.Error.WriteLine(m);
}