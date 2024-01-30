using DSharpPlus;
using DSharpPlus.Entities;
using HotelLib;
using HotelLib.Commands;
using HotelLib.Utils;

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

    public override void Handle(HotelBot bot, DiscordInteraction interaction)
    {
        var member = interaction.GetMember("user").Result!;
        var reason = interaction.GetString("reason") ?? "No reason provided.";
        var delete = interaction.GetInt("delete") ?? 3;

        try
        {
            member.BanAsync(delete, reason);
            interaction.Reply($"Banned {member.Username} ({member.Id}) for **{reason}**.", true);

            var embed = new DiscordEmbedBuilder()
                        .WithTitle("User banned! <:SK_cultured:792020838801997854>")
                        .AddField("User", member.Mention, true)
                        .AddField("Banned by", interaction.User.Mention, true)
                        .AddField("Reason", reason, true);

            var channel = interaction.Guild.GetChannel(Program.MOD_LOG);
            channel?.SendMessageAsync(embed.Build());
        }
        catch (Exception e)
        {
            interaction.Reply($"Failed to ban {member.Username} ({member.Id}).");
            Console.WriteLine(e);
        }
    }
}
