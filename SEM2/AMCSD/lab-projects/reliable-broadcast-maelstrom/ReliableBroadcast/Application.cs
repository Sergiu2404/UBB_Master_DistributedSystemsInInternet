using System;
using System.Collections.Generic;
using System.Text;

namespace ReliableBroadcast;

/// <summary>
/// Application (APP) layer — top of the abstraction stack.
///
/// Handles Maelstrom workload RPCs:
///   • "broadcast" → rbBroadcast(value)
///   • "read"      → return all rbDelivered values
///   • "topology"  → acknowledge (we use full-mesh BEB, ignoring the hint)
///
/// rbDeliver is called synchronously via RB.OnRbDeliver callback,
/// so values are always recorded before broadcast_ok is sent.
/// </summary>
public sealed class Application
{
    private readonly RbLayer _rb;
    private readonly PerfectLink _pl;

    private string _selfId = "";

    // All values rbDelivered to this node
    private readonly SortedSet<int> _deliveredValues = new();

    public Application(RbLayer rb, PerfectLink pl)
    {
        _rb = rb;
        _pl = pl;
    }

    public void Init(string selfId) => _selfId = selfId;

    // ── Maelstrom "broadcast" RPC ─────────────────────────────────────────────

    public void HandleBroadcastRequest(AppBroadcastRequestEvent ev)
    {
        Log($"[APP] broadcast request value={ev.Value}");

        // rbBroadcast triggers synchronous self-delivery via OnRbDeliver callback
        // so _deliveredValues already contains ev.Value when we reach the reply.
        _rb.Broadcast(ev.Value);

        var inReplyTo = BodyHelper.GetMsgId(ev.OriginalMessage.Body) ?? 0;
        _pl.Send(_selfId, ev.OriginalMessage.Src, new BroadcastOkBody
        {
            InReplyTo = inReplyTo,
            MsgId = _pl.NextMsgId()
        });
    }

    // ── rbDeliver (synchronous callback from RB) ──────────────────────────────

    public void HandleRbDeliver(string from, int value)
    {
        Log($"[APP] rbDeliver from={from} value={value}");
        _deliveredValues.Add(value);
    }

    // ── Maelstrom "read" RPC ──────────────────────────────────────────────────

    public void HandleReadRequest(AppReadRequestEvent ev)
    {
        var inReplyTo = BodyHelper.GetMsgId(ev.OriginalMessage.Body) ?? 0;
        _pl.Send(_selfId, ev.OriginalMessage.Src, new ReadOkBody
        {
            InReplyTo = inReplyTo,
            MsgId = _pl.NextMsgId(),
            Messages = _deliveredValues.ToList()
        });
    }

    // ── Maelstrom "topology" RPC ──────────────────────────────────────────────

    public void HandleTopologyRequest(AppTopologyRequestEvent ev)
    {
        var inReplyTo = BodyHelper.GetMsgId(ev.OriginalMessage.Body) ?? 0;
        _pl.Send(_selfId, ev.OriginalMessage.Src, new TopologyOkBody
        {
            InReplyTo = inReplyTo,
            MsgId = _pl.NextMsgId()
        });
    }

    private static void Log(string m) => Console.Error.WriteLine(m);
}