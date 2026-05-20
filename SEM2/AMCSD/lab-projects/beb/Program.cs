using System.Net;
using System.Net.Sockets;
using System.Security.Cryptography;
using System.Text;

const int MessageSize = 1024;
const int RandomOffset = 1;
const int RandomLength = 1003;
const int HashOffset = 1004;
const int HashLength = 20;

int messages = 0;
int nodeIndex;

List<IPEndPoint> nodes = new();

int received = 0;
int sent = 0;

DateTime lastReceive = DateTime.UtcNow;

object lockObj = new();

StreamWriter log = null!;
StreamWriter error = null!;

string ToHex(byte[] bytes)
{
    var hexContent = new StringBuilder(bytes.Length * 2);
    foreach (var b in bytes)
        hexContent.Append(b.ToString("x2"));
    return hexContent.ToString();
}

void ReadConfig(string path)
{
    var lines = File
        .ReadAllLines(path)
        .Select(line => line.Trim())
        .Where(line => !string.IsNullOrWhiteSpace(line))
        .ToArray();

    messages = int.Parse(lines[0]);

    for (int i = 1; i < lines.Length; i++)
    {
        var parts = lines[i].Split(' ', StringSplitOptions.RemoveEmptyEntries);
        if (parts.Length >= 2)
        {
            if (IPAddress.TryParse(parts[0], out var ip) && int.TryParse(parts[1], out var port))
            {
                nodes.Add(new IPEndPoint(ip, port));
            }
            else
            {
                Console.WriteLine($"Invalid config line ignored");
            }
        }
    }
}

async Task SendLoop(UdpClient client)
{
    for (int i = 0; i < messages; i++)
    {
        var msg = new byte[MessageSize];
        msg[0] = (byte)nodeIndex;

        RandomNumberGenerator.Fill(msg.AsSpan(RandomOffset, RandomLength));
        var sha = SHA1.HashData(msg.AsSpan(0, HashOffset));
        sha.CopyTo(msg, HashOffset);

        await Task.WhenAll(nodes.Select(async node =>
        {
            try
            {
                using var token = new CancellationTokenSource(TimeSpan.FromSeconds(5));
                await client.SendAsync(msg, node, token.Token);
            }
            catch (Exception ex)
            {
                error.WriteLine($"send error {node}: {ex.Message}");
            }
        }));

        lock (lockObj)
        {
            sent++;
        }

        await Task.Delay(1);
    }
}
async Task ReceiveLoop(UdpClient client)
{
    int expected = messages * nodes.Count;

    while (true)
    {
        lock (lockObj)
        {
            if (sent >= messages && (received >= expected || DateTime.UtcNow - lastReceive > TimeSpan.FromSeconds(5)))
            {
                break;
            }
        }

        try
        {
            using var token = new CancellationTokenSource(TimeSpan.FromSeconds(5));

            var result = await client.ReceiveAsync(token.Token);
            var msg = result.Buffer;
            if (msg.Length != MessageSize)
            {
                error.WriteLine(
                    $"wrong size {msg.Length}"
                );
                continue;
            }

            var source = msg[0];

            var sentSha = msg[HashOffset..(HashOffset + HashLength)];
            var calcSha = SHA1.HashData( msg.AsSpan(0, HashOffset));
            var ok = sentSha.SequenceEqual(calcSha) ? "OK" : "FAIL";

            await log.WriteLineAsync($"{ok} {source} {ToHex(sentSha)} {ToHex(calcSha)}");

            lock (lockObj)
            {
                received++;
                lastReceive = DateTime.UtcNow;
            }
        }
        catch (OperationCanceledException)
        {
            //timeout
        }
        catch (Exception ex)
        {
            error.WriteLine(
                $"receive error {ex.Message}"
            );
        }
    }
}

var config = args[0];
nodeIndex = int.Parse(args[1]);

ReadConfig(config);

log = new StreamWriter($"node_{nodeIndex}.log", append: false)
{ 
    AutoFlush = true 
};

error = new StreamWriter($"node_{nodeIndex}_error.log", append: false)
{ 
    AutoFlush = true 
};

using var client = new UdpClient(nodes[nodeIndex]);

Console.WriteLine($"node {nodeIndex} started");
var receiveTask = ReceiveLoop(client);

Console.WriteLine("node waiting 15 s...");
await Task.Delay(15000);

var sendTask = SendLoop(client);

await Task.WhenAll(receiveTask, sendTask);

log.Close();
error.Close();

Console.WriteLine(
    $"node {nodeIndex} done " +
    $"sent={sent} received={received}"
);