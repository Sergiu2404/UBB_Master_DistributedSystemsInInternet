using ReliableBroadcast;
using System.Collections.Concurrent;
using static System.Net.Mime.MediaTypeNames;

// ─── Global event queue ───────────────────────────────────────────────────────
//
// All inter-layer communication that crosses thread boundaries flows through
// this single queue.
//
// Producers:
//   1. STDIN reader thread  → PlDeliverEvent
//   2. PFD timer thread     → PfdTimeoutEvent
//   3. Event-processor itself (nested): PfdCrashEvent, BebDeliverEvent
//
// Consumer (single): event-processor thread (main thread)
//
var eventQueue = new BlockingCollection<Event>(boundedCapacity: 4096);

// ─── Build the abstraction stack (bottom → top) ───────────────────────────────
//
//   APP  →  RB  →  PFD  →  PL
//               →  BEB  →  PL
//
var pl = new PerfectLink(eventQueue);
var pfd = new PerfectFailureDetector(pl, eventQueue);
var beb = new BestEffortBroadcast(pl, eventQueue);
var rb = new RbLayer(beb);
var app = new Application(rb, pl);

var processor = new EventProcessor(eventQueue, pl, pfd, beb, rb, app);

// ─── Thread 1: STDIN reader ───────────────────────────────────────────────────
pl.StartReaderThread();

// ─── Thread 2 (main): Event processor ────────────────────────────────────────
//
// All events are processed serially — no concurrency within the stack.
// The PFD timer and STDIN reader are the only other threads; they only
// enqueue events and never touch shared state directly.
//
Console.Error.WriteLine("[Main] ReliableBroadcast node starting (Algorithm 3.2)");
processor.Run();
Console.Error.WriteLine("[Main] Event queue closed — exiting");