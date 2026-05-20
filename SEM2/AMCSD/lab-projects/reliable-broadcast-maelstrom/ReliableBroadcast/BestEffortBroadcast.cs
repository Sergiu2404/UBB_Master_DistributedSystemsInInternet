using System;
using System.Collections.Generic;
using System.Text;

using System.Collections.Concurrent;

namespace ReliableBroadcast;

/// <summary>
/// Best-Effort Broadcast (BEB).
///
/// Abstraction stack position:
///   RB  →  BEB  →  PL
///
/// Algorithm:
///   bebBroadcast(m):
///     for all q in Π: plSend(q, m)
///
///   upon plDeliver(p, m):
///     trigger bebDeliver(p, m)
///
/// Self-delivery:
///   Maelstrom does not route a node's own messages back to itself via STDIN.
///   Therefore <see cref="Broadcast"/> returns the self-delivery event
///   synchronously so the caller (RB) can handle it inline before replying
///   to the client — preserving correct sequencing.
/// </summary>
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

    // ── BEB broadcast ─────────────────────────────────────────────────────────

    /// <summary>
    /// Flood <paramref name="payload"/> to ALL processes.
    /// Returns a <see cref="BebDeliverEvent"/> for self if self is in the peer
    /// list (Maelstrom never loops messages back, so we handle self synchronously).
    /// </summary>
    public BebDeliverEvent? Broadcast(BebPayload payload)
    {
        Log($"[BEB] bebBroadcast sender={payload.Sender} value={payload.Value} → {_peers.Count} peers");
        BebDeliverEvent? selfDeliver = null;
        foreach (var peer in _peers)
        {
            if (peer == _selfId)
                selfDeliver = new BebDeliverEvent(_selfId, payload);
            else
                _pl.Send(_selfId, peer, new BebMessageBody { MsgId = _pl.NextMsgId(), RbData = payload });
        }
        return selfDeliver;
    }

    // ── Inbound "beb_broadcast" from network (via PL / STDIN) ────────────────

    /// <summary>
    /// Called by the event processor for inbound "beb_broadcast" Maelstrom messages.
    /// Enqueues a <see cref="BebDeliverEvent"/> for the RB layer to process.
    /// </summary>
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