using DSharpPlus;
using DSharpPlus.Entities;
using HotelLib;
using HotelLib.Commands;
using HotelLib.Utils;

namespace Senko.Commands;

public class ChatReviveCommand : SlashCommand
{
    public override string Name => "revive";
    public override string Description => "Pings chat revive.";

    private long last;

    public override async Task Handle(HotelBot bot, DiscordInteraction interaction)
    {
        var current = DateTimeOffset.Now.ToUnixTimeSeconds();
        var hour = TimeSpan.FromHours(1).TotalSeconds;

        if (current - last < hour)
        {
            await interaction.Reply($"Chat Revive ping is on cooldown. Next one is ready <t:{last + hour}:R>.", true);
            return;
        }

        last = current;
        await interaction.CreateResponseAsync(InteractionResponseType.ChannelMessageWithSource, new DiscordInteractionResponseBuilder
        {
            Content = "<@&806802654189715486>"
        }.AddMention(new RoleMention(806802654189715486)));
    }
}
