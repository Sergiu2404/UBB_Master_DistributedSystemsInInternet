using System;
using System.Collections.Generic;
using System.Text;

namespace ReliableBroadcast;

// ─── Base event ───────────────────────────────────────────────────────────────

public abstract record Event;

// ─── PL (Perfect Link) events ─────────────────────────────────────────────────

/// <summary>Fired when a raw Maelstrom message arrives on STDIN.</summary>
public record PlDeliverEvent(MaelstromMessage Message) : Event;

// ─── BEB (Best-Effort Broadcast) events ───────────────────────────────────────

/// <summary>Request from RB layer: flood a message to all processes.</summary>
public record BebBroadcastEvent(BebPayload Payload) : Event;

/// <summary>BEB delivers a received beb message to the RB layer.</summary>
public record BebDeliverEvent(string From, BebPayload Payload) : Event;

// ─── PFD (Perfect Failure Detector) events ────────────────────────────────────

/// <summary>Timer tick – PFD checks for missing heartbeats.</summary>
public record PfdTimeoutEvent : Event;

/// <summary>PFD notifies the RB layer that a process has crashed.</summary>
public record PfdCrashEvent(string ProcessId) : Event;

// ─── RB (Reliable Broadcast) events ──────────────────────────────────────────

/// <summary>APP → RB: broadcast a value to all processes.</summary>
public record RbBroadcastEvent(int Value) : Event;

/// <summary>RB → APP: a value has been reliably delivered.</summary>
public record RbDeliverEvent(string From, int Value) : Event;

// ─── APP events (Maelstrom workload) ──────────────────────────────────────────

/// <summary>Client sends a "broadcast" RPC to this node.</summary>
public record AppBroadcastRequestEvent(MaelstromMessage OriginalMessage, int Value) : Event;

/// <summary>Client sends a "read" RPC – return all delivered values.</summary>
public record AppReadRequestEvent(MaelstromMessage OriginalMessage) : Event;

/// <summary>Client sends a "topology" RPC – we store it but ignore for RB.</summary>
public record AppTopologyRequestEvent(MaelstromMessage OriginalMessage) : Event;
