using DSharpPlus;
using DSharpPlus.Entities;
using HotelLib;
using HotelLib.Commands;
using HotelLib.Logging;
using HotelLib.Utils;

namespace Senko.Commands;

public class TimeoutCommand : SlashCommand
{
    public override string Name => "timeout";
    public override string Description => "Times out a user from the server.";
    public override Permissions? Permission => Permissions.ModerateMembers;

    public override List<SlashOption> Options => new()
    {
        new SlashOption("user", "The user to timeout.", ApplicationCommandOptionType.User, true),
        new SlashOption("length", "The length of the timeout in seconds.", ApplicationCommandOptionType.Integer, true),
        new SlashOption("reason", "The reason for the timeout.", ApplicationCommandOptionType.String, false)
    };

    public override async Task Handle(HotelBot bot, DiscordInteraction interaction)
    {
        var member = interaction.GetMember("user").Result!;
        var length = interaction.GetInt("length") ?? 60;
        var reason = interaction.GetString("reason") ?? "No reason provided.";

        try
        {
            var date = DateTime.Now.AddSeconds(length);
            member.TimeoutAsync(date, reason).Wait();
            await interaction.Reply($"Timed out {member.Username} ({member.Id}) for **{reason}**.", true);

            var embed = new DiscordEmbedBuilder()
                        .WithTitle("User timed out! <:SK_cultured:792020838801997854>")
                        .AddField("User", member.Mention, true)
                        .AddField("Timed out by", interaction.User.Mention, true)
                        .AddField("Reason", reason, true);

            var channel = interaction.Guild.GetChannel(Program.MOD_LOG);
            channel?.SendMessageAsync(embed.Build());
        }
        catch (Exception e)
        {
            await interaction.Reply($"Failed to timeout {member.Username} ({member.Id}).");
            Logger.Log(e, "Failed to timeout user.");
        }
    }
}
