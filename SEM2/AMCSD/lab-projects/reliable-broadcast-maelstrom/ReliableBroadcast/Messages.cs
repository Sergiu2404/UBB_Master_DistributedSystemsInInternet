using System;
using System.Collections.Generic;
using System.Text;

namespace ReliableBroadcast;

using System.Text.Json;
using System.Text.Json.Serialization;

public record MaelstromMessage
{
    [JsonPropertyName("src")] public string Src { get; init; } = "";
    [JsonPropertyName("dest")] public string Dest { get; init; } = "";
    [JsonPropertyName("body")] public JsonElement Body { get; init; }
}

public static class BodyHelper
{
    public static string GetType(JsonElement body) =>
        body.TryGetProperty("type", out var t) ? t.GetString() ?? "" : "";

    public static int? GetMsgId(JsonElement body) =>
        body.TryGetProperty("msg_id", out var m) && m.ValueKind == JsonValueKind.Number
            ? m.GetInt32() : null;

    public static int? GetInReplyTo(JsonElement body) =>
        body.TryGetProperty("in_reply_to", out var r) && r.ValueKind == JsonValueKind.Number
            ? r.GetInt32() : null;
}

//data that beb carries, for rb
// serialised into the rb_data field of malestom body
public record BebPayload(
    [property: JsonPropertyName("sender")] string Sender,
    [property: JsonPropertyName("value")] int Value);

// outbound bodies
public record InitOkBody
{
    [JsonPropertyName("type")] public string Type { get; init; } = "init_ok";
    [JsonPropertyName("in_reply_to")] public int InReplyTo { get; init; }
    [JsonPropertyName("msg_id")] public int MsgId { get; init; }
}

public record BroadcastOkBody
{
    [JsonPropertyName("type")] public string Type { get; init; } = "broadcast_ok";
    [JsonPropertyName("in_reply_to")] public int InReplyTo { get; init; }
    [JsonPropertyName("msg_id")] public int MsgId { get; init; }
}

public record ReadOkBody
{
    [JsonPropertyName("type")] public string Type { get; init; } = "read_ok";
    [JsonPropertyName("in_reply_to")] public int InReplyTo { get; init; }
    [JsonPropertyName("msg_id")] public int MsgId { get; init; }
    [JsonPropertyName("messages")] public List<int> Messages { get; init; } = new();
}

public record TopologyOkBody
{
    [JsonPropertyName("type")] public string Type { get; init; } = "topology_ok";
    [JsonPropertyName("in_reply_to")] public int InReplyTo { get; init; }
    [JsonPropertyName("msg_id")] public int MsgId { get; init; }
}

// beb flood
public record BebMessageBody
{
    [JsonPropertyName("type")] public string Type { get; init; } = "beb_broadcast";
    [JsonPropertyName("msg_id")] public int MsgId { get; init; }
    [JsonPropertyName("rb_data")] public BebPayload RbData { get; init; } = null!;
}

// heartbeat req sent by perf fail det
public record HeartbeatRequestBody
{
    [JsonPropertyName("type")] public string Type { get; init; } = "hb_request";
    [JsonPropertyName("msg_id")] public int MsgId { get; init; }
}

// heartpeat reply sent by perf fail det
public record HeartbeatReplyBody
{
    [JsonPropertyName("type")] public string Type { get; init; } = "hb_reply";
    [JsonPropertyName("in_reply_to")] public int InReplyTo { get; init; }
    [JsonPropertyName("msg_id")] public int MsgId { get; init; }
}
