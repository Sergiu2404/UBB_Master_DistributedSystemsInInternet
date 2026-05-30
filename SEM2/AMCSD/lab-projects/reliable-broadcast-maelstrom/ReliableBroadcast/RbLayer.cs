using System;
using System.Collections.Generic;
using System.Text;

using System.Collections.Concurrent;

namespace ReliableBroadcast;


public sealed class RbLayer
{
    private readonly BestEffortBroadcast _beb;

    private string _selfId = "";

    // messages already rbDeliv: (originalSender, value)
    private readonly HashSet<(string, int)> _delivered = new();

    // all messages seen by current
    private readonly HashSet<(string Sender, int Value)> _forward = new();

    // to be invoked on rbDeliv(originalSender, value)
    public Action<string, int>? OnRbDeliver { get; set; }

    public RbLayer(BestEffortBroadcast beb)
    {
        _beb = beb;
    }

    public void Init(string selfId) => _selfId = selfId;

    // rb broadcast
    public void Broadcast(int value)
    {
        Log($"[RB] rbBroadcast value={value}");
        var payload = new BebPayload(_selfId, value);
        // if current node crash, other nodes rebroadcast this value and to know original sender
        _forward.Add((_selfId, value));
        var selfEv = _beb.Broadcast(payload);
        if (selfEv != null) HandleBebDeliver(selfEv);
    }

    public void HandleBebDeliver(BebDeliverEvent ev)
    {
        var payload = ev.Payload;
        var key = (payload.Sender, payload.Value);

        // deduplication check (to not receive same message more than one time from rebroadcasts)
        if (_delivered.Contains(key)) return;

        _delivered.Add(key);
        Log($"[RB] rbDeliver from={payload.Sender} value={payload.Value}");
        OnRbDeliver?.Invoke(payload.Sender, payload.Value);

        // first time seeing, avoid infinite rebroadcast
        if (_forward.Add(key))
        {
            Log($"[RB] forward & re-bebBroadcast sender={payload.Sender} value={payload.Value}");
            var selfEv = _beb.Broadcast(payload);
            // self already in _delivered
            if (selfEv != null && !_delivered.Contains((selfEv.Payload.Sender, selfEv.Payload.Value)))
                HandleBebDeliver(selfEv);
        }
    }

    public void HandleCrash(string crashedProcess)
    {
        Log($"[RB] handling crash of {crashedProcess} — re-broadcasting {_forward.Count} forwarded messages");
        foreach (var (sender, value) in _forward.ToList())
        {
            var selfEv = _beb.Broadcast(new BebPayload(sender, value));
            if (selfEv != null) HandleBebDeliver(selfEv);
        }
    }

    private static void Log(string m) => Console.Error.WriteLine(m);
}
