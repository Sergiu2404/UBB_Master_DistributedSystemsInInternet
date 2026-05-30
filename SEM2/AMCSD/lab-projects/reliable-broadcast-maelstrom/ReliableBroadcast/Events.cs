using System;
using System.Collections.Generic;
using System.Text;

namespace ReliableBroadcast;

public abstract record Event;

// fired when raw maelstrom message arrives on stdin
public record PlDeliverEvent(MaelstromMessage Message) : Event;

// req from rb layer, flood a message to all procs
public record BebBroadcastEvent(BebPayload Payload) : Event;
// beb delivers a received beb message to the rb layer
public record BebDeliverEvent(string From, BebPayload Payload) : Event;

// pfd checks missing heartbeats
public record PfdTimeoutEvent : Event;
// notifies rb layer that a proc crashed
public record PfdCrashEvent(string ProcessId) : Event;

// app -> rb broadcast value to all procs
public record RbBroadcastEvent(int Value) : Event;
// app -> rb a value has been reliably delivered
// public record RbDeliverEvent(string From, int Value) : Event;

// client sends broadcast rpc to this node
public record AppBroadcastRequestEvent(MaelstromMessage OriginalMessage, int Value) : Event;

// client sends a 'read' rpc and gets all delivered values
public record AppReadRequestEvent(MaelstromMessage OriginalMessage) : Event;

// client sends a 'topology' rpc, i store it but ignore for rb
public record AppTopologyRequestEvent(MaelstromMessage OriginalMessage) : Event;
