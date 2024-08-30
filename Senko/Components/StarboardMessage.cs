using MongoDB.Bson.Serialization.Attributes;

namespace Senko.Components;

public class StarboardMessage
{
    [BsonId]
    public ulong MessageID { get; set; }

    [BsonElement("board-id")]
    public ulong StarboardMessageID { get; set; }

    [BsonElement("count")]
    public int Count { get; set; }
}
