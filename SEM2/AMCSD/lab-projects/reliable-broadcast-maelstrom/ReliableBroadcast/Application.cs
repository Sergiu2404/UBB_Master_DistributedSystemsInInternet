using System;
using System.Collections.Generic;
using System.Text;

namespace ReliableBroadcast;

// handles maelstrom workload RPCs:
// topology - acknowledge (use beb, ignoring the hint)
// rbDeliver is called synchronously via rb.OnRbDeliver callback, so values are recorded before broadcast_ok sent
public sealed class Application
{
    private readonly RbLayer _rb;
    private readonly PerfectLink _pl;

    private string _selfId = "";

    // all values rbDelivered to this node
    private readonly SortedSet<int> _deliveredValues = new();

    public Application(RbLayer rb, PerfectLink pl)
    {
        _rb = rb;
        _pl = pl;
    }

    public void Init(string selfId) => _selfId = selfId;

    public void HandleBroadcastRequest(AppBroadcastRequestEvent ev)
    {
        Log($"[APP] broadcast request value={ev.Value}");

        // so _deliveredValues already contains ev.Value when we reach reply
        _rb.Broadcast(ev.Value);

        var inReplyTo = BodyHelper.GetMsgId(ev.OriginalMessage.Body) ?? 0;
        // send ok to maelstrom client proc
        _pl.Send(_selfId, ev.OriginalMessage.Src, new BroadcastOkBody
        {
            InReplyTo = inReplyTo,
            MsgId = _pl.NextMsgId()
        });
    }

    public void HandleRbDeliver(string from, int value)
    {
        Log($"[APP] rbDeliver from={from} value={value}");
        _deliveredValues.Add(value);
    }

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