using DSharpPlus;
using DSharpPlus.Entities;
using HotelLib;
using HotelLib.Commands;
using HotelLib.Utils;
using Senko.Database.Helpers;

namespace Senko.Commands.Warn;

public class WarnRemoveCommand : SlashCommand
{
    public override string Name => "remove";
    public override string Description => "Remove a warn from a user.";

    public override List<SlashOption> Options => new()
    {
        new SlashOption("warn", "The warn to remove.", ApplicationCommandOptionType.Integer, true)
    };

    public override async Task Handle(HotelBot bot, DiscordInteraction interaction)
    {
        var warnId = interaction.GetInt("warn");

        if (warnId is null)
        {
            await interaction.Reply("Invalid arguments.", true);
            return;
        }

        var warn = WarnHelper.Get(warnId.Value);

        if (warn is null)
        {
            await interaction.Reply("Warn not found.", true);
            return;
        }

        WarnHelper.Remove(warn);
        await interaction.Reply("Warn removed.", true);
    }
}
