using System.Text;
using DSharpPlus;
using DSharpPlus.Entities;
using HotelLib;
using HotelLib.Commands;
using HotelLib.Logging;
using HotelLib.Utils;
using Senko.Constants;

namespace Senko.Commands;

public class BanCommand : SlashCommand
{
    public override string Name => "ban";
    public override string Description => "Bans a user from the server.";
    public override Permissions? Permission => Permissions.BanMembers;

    public override List<SlashOption> Options => new()
    {
        new SlashOption("user", "The user to ban.", ApplicationCommandOptionType.User, true),
        new SlashOption("reason", "The reason for the ban.", ApplicationCommandOptionType.String, false),
        new SlashOption("delete", "Delete amount of days of messages.", ApplicationCommandOptionType.Integer, false)
    };

    public override async Task Handle(HotelBot bot, DiscordInteraction interaction)
    {
        var member = interaction.GetMember("user").Result!;
        var reason = interaction.GetString("reason") ?? "No reason provided.";
        var delete = interaction.GetInt("delete") ?? 3;

        if (member.Id == interaction.User.Id)
        {
            await interaction.Reply("You can't ban yourself silly.", true);
            return;
        }

        var sentDm = true;

        try
        {
            var sb = new StringBuilder();
            sb.AppendLine("## You have been banned.");
            sb.AppendLine($"You have been banned from **{interaction.Guild.Name}**!");
            sb.AppendLine();
            sb.AppendLine($"**Reason**: {reason}");
            sb.AppendLine();
            sb.AppendLine("You can appeal your ban at https://appeal.gg/senko-san.");

            await member.SendMessageAsync(sb.ToString());
        }
        catch (Exception e)
        {
            sentDm = false;
        }

        try
        {
            await member.BanAsync(delete, reason);

            var sb = new StringBuilder();
            sb.AppendLine($"## Banned {member.Username}.");
            sb.AppendLine($"**User ID**: {member.Id}");
            sb.AppendLine($"**Reason**: {reason}");
            sb.AppendLine($"**Sent DM**: {sentDm}");
            await interaction.Reply(sb.ToString(), true);

            if (Program.ModLogChannel != null)
                await Program.ModLogChannel.SendMessageAsync(EmbedPresets.CreateModLogBan(bot, member, interaction.User, reason));
        }
        catch (Exception e)
        {
            await interaction.Reply($"Failed to ban {member.Username} ({member.Id}).");
            Logger.Log(e, "Failed to ban user.");
        }
    }
}
