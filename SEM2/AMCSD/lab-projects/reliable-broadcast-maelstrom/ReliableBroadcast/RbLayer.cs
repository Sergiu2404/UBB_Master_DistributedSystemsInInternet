using System;
using System.Collections.Generic;
using System.Text;

using System.Collections.Concurrent;

namespace ReliableBroadcast;

/// <summary>
/// Reliable Broadcast (RB) — Algorithm 3.2 from
/// "Introduction to Reliable and Secure Distributed Programming" (p. 78).
///
/// Abstraction stack position:
///   APP  →  RB  →  PFD
///                 →  BEB
///
/// Algorithm 3.2 — Fail-Stop Reliable Broadcast (eager retransmit):
/// ──────────────────────────────────────────────────────────────────
///   State:
///     delivered  : set of (sender, value) pairs already rb-delivered
///     forward    : set of messages this node is responsible for retransmitting
///
///   upon rbBroadcast(m) at process p:
///     forward  ← forward ∪ { (p, m) }
///     bebBroadcast( rbData(p, m) )
///
///   upon bebDeliver(q, rbData(s, m)):
///     if (s, m) ∉ delivered:
///       delivered ← delivered ∪ { (s, m) }
///       trigger rbDeliver(s, m)
///       if (s, m) ∉ forward:
///         forward ← forward ∪ { (s, m) }
///         bebBroadcast( rbData(s, m) )
///
///   upon crash(q):
///     for each (s, m) ∈ forward:
///       bebBroadcast( rbData(s, m) )
///
/// rbDeliver is fired synchronously via the <see cref="OnRbDeliver"/> callback
/// to avoid event-queue re-ordering between rbBroadcast and the client reply.
/// </summary>
public sealed class RbLayer
{
    private readonly BestEffortBroadcast _beb;

    private string _selfId = "";

    // Messages we have already rbDelivered: (originalSender, value)
    private readonly HashSet<(string, int)> _delivered = new();

    // Messages we are forwarding
    private readonly HashSet<(string Sender, int Value)> _forward = new();

    /// <summary>Synchronous callback invoked on rbDeliver(sender, value).</summary>
    public Action<string, int>? OnRbDeliver { get; set; }

    public RbLayer(BestEffortBroadcast beb)
    {
        _beb = beb;
    }

    public void Init(string selfId) => _selfId = selfId;

    // ── rbBroadcast ──────────────────────────────────────────────────────────

    public void Broadcast(int value)
    {
        Log($"[RB] rbBroadcast value={value}");
        var payload = new BebPayload(_selfId, value);
        _forward.Add((_selfId, value));
        var selfEv = _beb.Broadcast(payload);
        if (selfEv != null) HandleBebDeliver(selfEv);   // synchronous self-delivery
    }

    // ── bebDeliver ───────────────────────────────────────────────────────────

    public void HandleBebDeliver(BebDeliverEvent ev)
    {
        var payload = ev.Payload;
        var key = (payload.Sender, payload.Value);

        if (_delivered.Contains(key)) return;

        _delivered.Add(key);
        Log($"[RB] rbDeliver from={payload.Sender} value={payload.Value}");
        OnRbDeliver?.Invoke(payload.Sender, payload.Value);

        if (_forward.Add(key))
        {
            Log($"[RB] forward & re-bebBroadcast sender={payload.Sender} value={payload.Value}");
            var selfEv = _beb.Broadcast(payload);
            // self already in _delivered → HandleBebDeliver will be a no-op, but avoid recursion
            if (selfEv != null && !_delivered.Contains((selfEv.Payload.Sender, selfEv.Payload.Value)))
                HandleBebDeliver(selfEv);
        }
    }

    // ── crash ────────────────────────────────────────────────────────────────

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
