using HotelLib;
using MongoDB.Driver;
using Senko.Components;

namespace Senko.Database.Helpers;

public static class WarnHelper
{
    private static IMongoCollection<UserWarning> collection => MongoDatabase.GetCollection<UserWarning>("warnings");

    public static long NextId
    {
        get
        {
            var warn = collection.Find(m => true)
                                 .SortByDescending(m => m.ID)
                                 .FirstOrDefault();

            return warn?.ID + 1 ?? 1;
        }
    }

    public static void Add(UserWarning warn)
    {
        warn.ID = NextId;
        collection.InsertOne(warn);
    }

    public static UserWarning? Get(int warnId)
        => collection.Find(m => m.ID == warnId).FirstOrDefault();

    public static List<UserWarning> Get(ulong userId)
        => collection.Find(m => m.UserID == userId).ToList();

    public static void Remove(UserWarning warn)
        => collection.DeleteOne(x => x.ID == warn.ID);
}
