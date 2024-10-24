using DSharpPlus.Entities;
using HotelLib;

namespace Senko.Constants;

public static class EmbedPresets
{
    public static DiscordEmbedBuilder CreateModLogBan(HotelBot bot, DiscordUser user, DiscordUser moderator, string reason)
    {
        return new DiscordEmbedBuilder()
               .WithTitle("User banned! <:SK_cultured:792020838801997854>")
               .AddField("User", user.Mention, true)
               .AddField("Banned by", moderator.Mention, true)
               .AddField("Reason", reason, true)
               .WithColor(bot.AccentColor);
    }

    public static DiscordEmbedBuilder CreatePublicLogBan(HotelBot bot, DiscordUser user, string reason)
    {
        return new DiscordEmbedBuilder()
               .WithTitle("User banned! <:SK_cultured:792020838801997854>")
               .AddField("User", user.Mention, true)
               .AddField("Reason", reason, true)
               .WithColor(bot.AccentColor);
    }
}
