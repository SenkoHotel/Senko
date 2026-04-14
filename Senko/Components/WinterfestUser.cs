using MongoDB.Bson.Serialization.Attributes;

namespace Senko.Components;

public class WinterfestUser
{
    [BsonId]
    public ulong ID { get; set; }

    [BsonElement("thrown")]
    public uint SnowballsThrown { get; set; }

    [BsonElement("recieved")]
    public uint SnowballsRecieved { get; set; }

    [BsonElement("landmines")]
    public uint LandminesHit { get; set; }

    [BsonElement("last")]
    public long LastThrown { get; set; }
}
