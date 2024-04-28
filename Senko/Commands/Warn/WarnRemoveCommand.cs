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

    public override void Handle(HotelBot bot, DiscordInteraction interaction)
    {
        var warnId = interaction.GetInt("warn");

        if (warnId is null)
        {
            interaction.Reply("Invalid arguments.", true);
            return;
        }

        var warn = WarnHelper.Get(warnId.Value);

        if (warn is null)
        {
            interaction.Reply("Warn not found.", true);
            return;
        }

        WarnHelper.Remove(warn);
        interaction.Reply("Warn removed.", true);
    }
}
