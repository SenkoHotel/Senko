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

    private static readonly ulong[] suggestion_channels = { 825400708316397628, 901062083217604638 };

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
        bot.Client.MessageCreated += onMessage;
        await bot.Start();
    }

    private static async Task onMessage(DiscordClient sender, MessageCreateEventArgs args)
    {
        if (suggestion_channels.Contains(args.Channel.Id))
        {
            var embed = new DiscordEmbedBuilder()
                        .WithAuthor(args.Author.Username, iconUrl: args.Author.AvatarUrl)
                        .WithDescription(args.Message.Content);

            var message = await args.Channel.SendMessageAsync(embed.Build());

            await message.CreateReactionAsync(DiscordEmoji.FromName(sender, ":white_check_mark:"));
            await message.CreateReactionAsync(DiscordEmoji.FromName(sender, ":x:"));

            await args.Message.DeleteAsync();
        }
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
