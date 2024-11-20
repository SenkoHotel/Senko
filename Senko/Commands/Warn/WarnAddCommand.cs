using DSharpPlus;
using DSharpPlus.Entities;
using HotelLib;
using HotelLib.Commands;
using HotelLib.Utils;
using Senko.Components;
using Senko.Database.Helpers;

namespace Senko.Commands.Warn;

public class WarnAddCommand : SlashCommand
{
    public override string Name => "add";
    public override string Description => "Add a warn to a user.";

    public override List<SlashOption> Options => new()
    {
        new SlashOption("user", "The user to warn.", ApplicationCommandOptionType.User, true),
        new SlashOption("reason", "The reason of the warn.", ApplicationCommandOptionType.String, true),
        new SlashOption("dm", "Whether to send a DM to the user.", ApplicationCommandOptionType.Boolean, true)
    };

    public override async Task Handle(HotelBot bot, DiscordInteraction interaction)
    {
        await interaction.AcknowledgeEphemeral();

        var user = await interaction.GetMember("user");
        var reason = interaction.GetString("reason");
        var dm = interaction.GetBool("dm") ?? false;

        if (user is null || reason is null)
        {
            interaction.Followup("Invalid arguments.", true);
            return;
        }

        var warn = new UserWarning
        {
            UserID = user.Id,
            Reason = reason,
            ModeratorID = interaction.User.Id,
            Date = DateTime.UtcNow
        };

        WarnHelper.Add(warn);

        var dmFailed = false;

        if (dm)
        {
            try
            {
                var dmEmbed = new DiscordEmbedBuilder()
                              .WithTitle("You have been warned.")
                              .WithDescription($"You have been warned in **{interaction.Guild.Name}**!")
                              .AddField("Reason", reason)
                              .WithColor(bot.AccentColor);

                await user.SendMessageAsync(embed: dmEmbed.Build());
            }
            catch
            {
                dmFailed = true;
            }
        }

        var logEmbed = new DiscordEmbedBuilder()
                       .WithTitle("User Warned")
                       .AddField("User", user.Mention)
                       .AddField("Moderator", interaction.User.Mention)
                       .AddField("Reason", reason)
                       .WithColor(bot.AccentColor);

        var logChannel = interaction.Guild.GetChannel(Program.MOD_LOG);

        if (logChannel != null)
            await logChannel.SendMessageAsync(embed: logEmbed.Build());

        interaction.Followup($"Warned {user.Mention} for `{reason}`." + (dmFailed ? " (DM failed)" : ""), true);
    }
}
