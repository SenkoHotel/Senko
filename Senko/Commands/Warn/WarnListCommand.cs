using DSharpPlus;
using DSharpPlus.Entities;
using HotelLib;
using HotelLib.Commands;
using HotelLib.Utils;
using Senko.Database.Helpers;

namespace Senko.Commands.Warn;

public class WarnListCommand : SlashCommand
{
    public override string Name => "list";
    public override string Description => "List the warns of a user.";

    public override List<SlashOption> Options => new()
    {
        new SlashOption("user", "The user to list the warns.", ApplicationCommandOptionType.User, true)
    };

    public override async Task Handle(HotelBot bot, DiscordInteraction interaction)
    {
        var user = await interaction.GetMember("user");

        if (user is null)
        {
            await interaction.Reply("Invalid arguments.", true);
            return;
        }

        var warns = WarnHelper.Get(user.Id);

        if (warns.Count == 0)
        {
            await interaction.Reply("No warns found.", true);
            return;
        }

        var embed = new DiscordEmbedBuilder
        {
            Title = $"Warns of {user.Username}",
            Color = bot.AccentColor
        };

        foreach (var warn in warns)
            embed.AddField($"#{warn.ID} {warn.Date:yyyy-MM-dd} ({warn.UserID})", warn.Reason);

        await interaction.ReplyEmbed(embed);
    }
}
