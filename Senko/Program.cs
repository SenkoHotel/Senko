using DSharpPlus;
using DSharpPlus.Entities;
using DSharpPlus.EventArgs;
using HotelLib;
using HotelLib.Commands;
using Senko.Commands;

namespace Senko;

public static class Program
{
    public const ulong MOD_LOG = 898580934440394752;
    public const ulong PUBLIC_LOG = 825336003018489867;

    public static async Task Main()
    {
        var config = HotelBot.LoadConfig<Config>();

        var bot = new HotelBot(config.Token)
        {
            Commands = new List<SlashCommand>
            {
                new BanCommand(),
                new TimeoutCommand()
            }
        };

        bot.Client.GuildBanAdded += onBan;
        await bot.Start();
    }

    private static async Task onBan(DiscordClient sender, GuildBanAddEventArgs args)
    {
        var embed = new DiscordEmbedBuilder()
                    .WithTitle("User banned! <:SK_cultured:792020838801997854>")
                    .AddField("User", args.Member.Mention, true);

        var channel = args.Guild.GetChannel(PUBLIC_LOG);

        if (channel != null)
            await channel.SendMessageAsync(embed.Build());
    }
}
