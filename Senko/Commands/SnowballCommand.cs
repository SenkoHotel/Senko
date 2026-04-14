using System.Diagnostics;
using DSharpPlus.Entities;
using HotelLib;
using HotelLib.Commands;
using HotelLib.Logging;
using HotelLib.Utils;
using MongoDB.Driver;
using Senko.Components;

namespace Senko.Commands;

public class SnowballCommand : SlashCommand
{
    public override string Name => "snowball";
    public override string Description => "Throw a snowball at someone.";

    public override List<SlashOption> Options => new() {
        new("target", "The user you want to aim at.", DSharpPlus.ApplicationCommandOptionType.User, true)
    };

    public override async Task Handle(HotelBot bot, DiscordInteraction interaction)
    {
        var target = await interaction.GetUser("target");
        var rng = Random.Shared.NextSingle();

        Debug.Assert(target != null);

        var cur = getUser(interaction.User.Id);
        var time = DateTimeOffset.UtcNow.ToUnixTimeSeconds();

        if (time - cur.LastThrown < 60) {
            await interaction.Reply("Please wait a bit before throwing another snowball.", true);
            return;
        }

        var miss = rng <= 0.2f; // 20%
        var landmine = rng <= 0.004; // 1 in 250
        Logger.Log($"{rng}");

        updateUser(interaction.User.Id, x => {
            x.SnowballsThrown++;
            x.LastThrown = time;
            if (landmine) x.LandminesHit++;
        });

        if (!miss) updateUser(target.Id, x => x.SnowballsRecieved++);
        var member = await interaction.Guild.GetMemberAsync(interaction.User.Id);

        if (landmine) {
            await interaction.ReplyEmbed(new DiscordEmbedBuilder()
                .WithDescription($"{interaction.User.Mention} threw a snowball at {target.Mention} but missed... And hit a LANDMINE??")
                .WithImageUrl("https://share.flux.moe/winterfest/kaf-kafu.gif")
                .WithColor(DiscordColor.Red));

            await member.TimeoutAsync(DateTimeOffset.Now.AddMinutes(10), "Hit a landmine.");
        } else if (miss) {
            await interaction.ReplyEmbed(new DiscordEmbedBuilder()
                .WithDescription($"{interaction.User.Mention} threw a snowball at {target.Mention} but missed...")
                .WithColor(DiscordColor.Red));
        } else { // hits
            var images = new List<string> {
                "https://share.flux.moe/winterfest/senko-san-snowball.gif",
                "https://share.flux.moe/winterfest/senko-senko-san.gif",
                "https://share.flux.moe/winterfest/rezero-petra.gif",
                "https://share.flux.moe/winterfest/jellyfish-can%27t-swim-at-the-night-anime.gif",
                "https://share.flux.moe/winterfest/anime.gif",
                "https://share.flux.moe/winterfest/vn-visual-novel.gif",
                "https://share.flux.moe/winterfest/oniichan-wa-oshimai-snowball.gif",
                "https://share.flux.moe/winterfest/darker-than-black-dtb.gif"
            };

            await interaction.ReplyEmbed(new DiscordEmbedBuilder()
                .WithDescription($"{interaction.User.Mention} threw a snowball at {target.Mention}!")
                .WithImageUrl(images[Random.Shared.Next(images.Count)])
                .WithColor(bot.AccentColor));
        }

        if (getUser(interaction.User.Id).SnowballsThrown == 10) {
            await member.GrantRoleAsync(interaction.Guild.GetRole(1453022573796397135));
        }
    }

    private static WinterfestUser getUser(ulong id) {
        var coll = MongoDatabase.GetCollection<WinterfestUser>("winterfest");
        var user = coll.Find(x => x.ID == id).FirstOrDefault();

        if (user is null) {
            user = new WinterfestUser() { ID = id };
            coll.InsertOne(user);
        }

        return user;
    }

    private static void updateUser(ulong id, Action<WinterfestUser> act) {
        var coll = MongoDatabase.GetCollection<WinterfestUser>("winterfest");
        var user = getUser(id);
        act(user);
        coll.ReplaceOne(x => x.ID == id, user);
    }
}

