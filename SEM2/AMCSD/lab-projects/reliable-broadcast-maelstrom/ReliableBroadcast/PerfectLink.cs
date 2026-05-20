using System;
using System.Collections.Generic;
using System.Text;

namespace ReliableBroadcast;

using System.Collections.Concurrent;
using System.Text.Json;

/// <summary>
/// Perfect Link (PL) — bottom of the abstraction stack.
///
/// Responsibilities:
///   • Read newline-delimited JSON from STDIN, wrap each message in a
///     <see cref="PlDeliverEvent"/> and enqueue it in the global event queue.
///   • Provide <see cref="Send"/> which serialises an object to JSON and
///     writes it to STDOUT (thread-safe via a lock).
///
/// PL does NOT do retransmission or deduplication — that is Maelstrom's job.
/// </summary>
public sealed class PerfectLink
{
    private readonly BlockingCollection<Event> _eventQueue;
    private readonly object _stdoutLock = new();
    private int _msgIdCounter;

    public PerfectLink(BlockingCollection<Event> eventQueue)
    {
        _eventQueue = eventQueue;
    }

    // ── STDIN reader (runs on its own dedicated thread) ──────────────────────

    public void StartReaderThread()
    {
        var t = new Thread(ReaderLoop) { Name = "stdin-reader", IsBackground = true };
        t.Start();
    }

    private void ReaderLoop()
    {
        string? line;
        while ((line = Console.ReadLine()) != null)
        {
            if (string.IsNullOrWhiteSpace(line)) continue;
            try
            {
                var msg = JsonSerializer.Deserialize<MaelstromMessage>(line)!;
                _eventQueue.Add(new PlDeliverEvent(msg));
            }
            catch (Exception ex)
            {
                Log($"[PL] Failed to parse message: {ex.Message} | raw={line}");
            }
        }
        // STDIN closed (Maelstrom shut us down) — nothing more to do.
        Log("[PL] STDIN closed");
        _eventQueue.CompleteAdding();
    }

    // ── STDOUT writer ─────────────────────────────────────────────────────────

    /// <summary>Send a typed body from <paramref name="src"/> to <paramref name="dest"/>.</summary>
    public void Send<TBody>(string src, string dest, TBody body)
    {
        var envelope = new { src, dest, body };
        var json = JsonSerializer.Serialize(envelope);
        lock (_stdoutLock)
        {
            Console.WriteLine(json);
            Console.Out.Flush();
        }
    }

    public int NextMsgId() => Interlocked.Increment(ref _msgIdCounter);

    private static void Log(string msg) => Console.Error.WriteLine(msg);
}