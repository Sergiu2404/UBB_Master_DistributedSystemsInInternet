using ReliableBroadcast;
using System.Collections.Concurrent;
using static System.Net.Mime.MediaTypeNames;

var eventQueue = new BlockingCollection<Event>(boundedCapacity: 4096);

var pl = new PerfectLink(eventQueue);
var pfd = new PerfectFailureDetector(pl, eventQueue);
var beb = new BestEffortBroadcast(pl, eventQueue);
var rb = new RbLayer(beb);
var app = new ReliableBroadcast.Application(rb, pl);

var processor = new EventProcessor(eventQueue, pl, pfd, beb, rb, app);

pl.StartReaderThread();

Console.Error.WriteLine("[Main] ReliableBroadcast node starting (Algorithm 3.2)");
processor.Run();
Console.Error.WriteLine("[Main] Event queue closed — exiting");