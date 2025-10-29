using DSharpPlus;
using DSharpPlus.Entities;
using DSharpPlus.EventArgs;
using HotelLib;
using HotelLib.Commands;
using HotelLib.Logging;
using MongoDB.Driver;
using Senko.Commands;
using Senko.Components;
using Senko.Constants;

namespace Senko;

public static class Program
{
    private static readonly ulong[] suggestion_channels = { 825400708316397628, 901062083217604638 };

    public const ulong MOD_LOG = 898580934440394752;
    public const ulong PUBLIC_LOG = 825336003018489867;
    public const ulong STARBOARD = 1278894932169461891;

    private const string star_unicode = "⭐";

    public static DiscordChannel? PublicLogChannel => bot.Client.GetChannelAsync(PUBLIC_LOG).Result;
    public static DiscordChannel? ModLogChannel => bot.Client.GetChannelAsync(MOD_LOG).Result;
    public static DiscordChannel? StarboardChannel => bot.Client.GetChannelAsync(STARBOARD).Result;

    private static HotelBot bot = null!;

    public static async Task Main()
    {
        MongoDatabase.Initialize("senko");

        bot = new HotelBot
        {
            AccentColor = new DiscordColor("#fdca64"),
            Commands = new List<SlashCommand>
            {
                new BanCommand(),
                new ChatReviveCommand(),
                new TimeoutCommand(),
                new WarnCommand()
            }
        };

        bot.Client.GuildBanAdded += onBan;
        bot.Client.MessageCreated += onMessage;
        bot.Client.MessageReactionAdded += onReactionAdd;
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
                        .WithColor(bot.AccentColor)
                        .WithDescription(args.Message.Content);

            var message = await args.Channel.SendMessageAsync(embed.Build());

            await message.CreateReactionAsync(DiscordEmoji.FromName(sender, ":white_check_mark:"));
            await message.CreateReactionAsync(DiscordEmoji.FromName(sender, ":x:"));

            await args.Message.DeleteAsync();
        }
    }

    private static async Task onReactionAdd(DiscordClient sender, MessageReactionAddEventArgs args)
    {
        try
        {
            if (StarboardChannel is null)
                return;

            var star = DiscordEmoji.FromUnicode(star_unicode);

            if (args.Emoji != star)
                return;

            var message = await args.Channel.GetMessageAsync(args.Message.Id);

            var reactions = await message.GetReactionsAsync(star, 50);

            if (reactions is null)
                return;

            var count = reactions.Count;

            var collection = MongoDatabase.GetCollection<StarboardMessage>("starboard");
            var existing = collection.Find(x => x.MessageID == message.Id).FirstOrDefault();

            if (existing is not null)
            {
                var sb = await StarboardChannel.GetMessageAsync(existing.StarboardMessageID);

                if (sb is null)
                    return;

                await sb.ModifyAsync(builder =>
                {
                    builder.Content = $"**{count}** {star_unicode}";
                });

                existing.Count = count;
                await collection.ReplaceOneAsync(msg => msg.MessageID == existing.MessageID, existing);
                return;
            }

            const int star_requirement = 4;

            if (reactions.Count < star_requirement)
                return;

            var embed = new DiscordEmbedBuilder
            {
                Description = message.Content,
                Author = new DiscordEmbedBuilder.EmbedAuthor
                {
                    IconUrl = message.Author.AvatarUrl,
                    Name = message.Author.Username
                }
            };

            embed.AddField("Channel", $"<#{args.Channel.Id}>", true);
            embed.AddField("Original Message", $"[Click to jump!]({message.JumpLink})", true);

            var builder = new DiscordMessageBuilder
            {
                Content = $"**{count}** {star_unicode}\n",
                Embed = embed
            };

            foreach (var attachment in message.Attachments)
            {
                try
                {
                    using var http = new HttpClient();
                    using var res = await http.GetAsync(attachment.Url);
                    var ms = new MemoryStream();
                    await res.Content.CopyToAsync(ms);
                    ms.Seek(0, SeekOrigin.Begin);
                    builder.AddFile(attachment.FileName, ms);
                }
                catch (Exception ex)
                {
                    Logger.Log(ex, "Failed to download attachment!");
                }
            }

            var result = await StarboardChannel.SendMessageAsync(builder);

            if (result is null)
            {
                Logger.Log($"Failed to send message!", LogLevel.Error);
                return;
            }

            await collection.InsertOneAsync(new StarboardMessage
            {
                MessageID = message.Id,
                StarboardMessageID = result.Id,
                Count = count
            });
        }
        catch (Exception e)
        {
            Logger.Log(e);
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
            await PublicLogChannel.SendMessageAsync(EmbedPresets.CreatePublicLogBan(bot, args.Member, reason));

        // if the ban didn't come from the bot, try to log it in the mod log
        if (entry.UserResponsible.Id != bot.Client.CurrentUser.Id && ModLogChannel != null)
            await ModLogChannel.SendMessageAsync(EmbedPresets.CreateModLogBan(bot, args.Member, entry.UserResponsible, reason));
    }
}
