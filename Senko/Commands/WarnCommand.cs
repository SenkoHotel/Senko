using DSharpPlus;
using HotelLib.Commands;
using Senko.Commands.Warn;

namespace Senko.Commands;

public class WarnCommand : SlashCommandGroup
{
    public override string Name => "warn";
    public override string Description => "Warn a user.";
    public override Permissions? Permission => Permissions.ModerateMembers;

    public override List<SlashCommand> SubCommands => new()
    {
        new WarnAddCommand(),
        new WarnRemoveCommand(),
        new WarnListCommand()
    };
}
