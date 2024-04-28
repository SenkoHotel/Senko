using MongoDB.Bson.Serialization.Attributes;

namespace Senko.Components;

public class UserWarning
{
    [BsonId]
    public long ID { get; set; }

    [BsonElement("user")]
    public ulong UserID { get; set; }

    [BsonElement("reason")]
    public string Reason { get; set; } = "";

    [BsonElement("moderator")]
    public ulong ModeratorID { get; set; }

    [BsonElement("date")]
    public DateTime Date { get; set; }
}
