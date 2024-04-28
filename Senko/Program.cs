using DSharpPlus;
using DSharpPlus.Entities;
using DSharpPlus.EventArgs;
using HotelLib;
using HotelLib.Commands;
using Senko.Commands;
using Senko.Constants;
using Senko.Database;

namespace Senko;

public static class Program
{
    public const ulong MOD_LOG = 898580934440394752;
    public const ulong PUBLIC_LOG = 825336003018489867;
    private static readonly ulong[] suggestion_channels = { 825400708316397628, 901062083217604638 };

    public static DiscordColor AccentColor => new("#fdca64");
    public static DiscordChannel? PublicLogChannel => bot.Client.GetChannelAsync(PUBLIC_LOG).Result;
    public static DiscordChannel? ModLogChannel => bot.Client.GetChannelAsync(MOD_LOG).Result;

    private static HotelBot bot = null!;

    public static async Task Main()
    {
        var config = HotelBot.LoadConfig<Config>();
        MongoDatabase.Initialize(config.MongoConnectionString, config.MongoDatabaseName);

        bot = new HotelBot(config.Token)
        {
            Commands = new List<SlashCommand>
            {
                new BanCommand(),
                new TimeoutCommand(),
                new WarnCommand()
            }
        };

        bot.Client.GuildBanAdded += onBan;
        bot.Client.MessageCreated += onMessage;
        await bot.Start();
    }

    private static async Task onMessage(DiscordClient sender, MessageCreateEventArgs args)
    {
        if (args.Author.IsBot)
            return;

        if (suggestion_channels.Contains(args.Channel.Id))
        {
            var embed = new DiscordEmbedBuilder()
                        .WithAuthor(args.Author.Username, iconUrl: args.Author.AvatarUrl)
                        .WithColor(AccentColor)
                        .WithDescription(args.Message.Content);

            var message = await args.Channel.SendMessageAsync(embed.Build());

            await message.CreateReactionAsync(DiscordEmoji.FromName(sender, ":white_check_mark:"));
            await message.CreateReactionAsync(DiscordEmoji.FromName(sender, ":x:"));

            await args.Message.DeleteAsync();
        }
    }

    private static async Task onBan(DiscordClient sender, GuildBanAddEventArgs args)
    {
        // wait for audit log to update
        // yes this is stupid. blame discord.
        await Task.Delay(5000);

        var audit = await args.Guild.GetAuditLogsAsync(1, null, AuditLogActionType.Ban);

        // uh, weird edge case?
        if (audit.Count == 0)
            return;

        var entry = audit[0];
        var reason = entry.Reason ?? "*No reason provided.*";

        if (PublicLogChannel != null)
            await PublicLogChannel.SendMessageAsync(EmbedPresets.CreatePublicLogBan(args.Member, reason));

        // if the ban didn't come from the bot, try to log it in the mod log
        if (entry.UserResponsible.Id != bot.Client.CurrentUser.Id && ModLogChannel != null)
            await ModLogChannel.SendMessageAsync(EmbedPresets.CreateModLogBan(args.Member, entry.UserResponsible, reason));
    }
}
