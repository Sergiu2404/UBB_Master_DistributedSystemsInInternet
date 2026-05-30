using System;
using System.Collections.Generic;
using System.Text;

namespace ReliableBroadcast;

using System.Collections.Concurrent;
using System.Text.Json;

// pl - bottom of abstractions stack
// read json from stdin, each message enqueued to global events queue
// maelstrom handles retransmission
public sealed class PerfectLink
{
    private readonly BlockingCollection<Event> _eventQueue;
    private readonly object _stdoutLock = new();
    private int _msgIdCounter;

    public PerfectLink(BlockingCollection<Event> eventQueue)
    {
        _eventQueue = eventQueue;
    }

    public void StartReaderThread()
    {
        var t = new Thread(ReaderLoop) { Name = "stdin-reader", IsBackground = true };
        t.Start();
    }

    private void ReaderLoop()
    {
        string? line;
        // loop until stdin closed by maelstrom killing node proc
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

        Log("[PL] STDIN closed");
        _eventQueue.CompleteAdding(); // exit queue loop, so Run() terminates thread and program ends
    }

    // send json to stdout
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