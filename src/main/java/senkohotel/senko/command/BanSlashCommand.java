package senkohotel.senko.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.SlashCommand;
import senkohotel.hotelbot.utils.MessageUtils;
import senkohotel.senko.util.UserUtils;

public class BanSlashCommand extends SlashCommand {
    public BanSlashCommand() {
        name = "ban";
        description = "Bans a user from the server";
        options.add(new OptionData(OptionType.USER, "user", "The User to be banned.", true));
        options.add(new OptionData(OptionType.STRING, "reason", "The reason this user was banned.", false));
    }

    public void exec(SlashCommandInteraction interact) {
        if (!UserUtils.hasRole(interact.getMember(), "792173231040757780")) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Only moderators can use this command!")
                    .setColor(0xFF5555);
            reply(interact, embed);
            return;
        }

        try {
            User toBan = interact.getOption("user").getAsUser();

            String reason = interact.getOption("reason").getAsString();
            if (reason.isEmpty()) {
                reason = "No reason provided.";
            }

            interact.getGuild().ban(toBan, 7, reason).complete();

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("User banned! <:SK_cultured:792020838801997854>")
                    .addField("User", toBan.getName() + "#" + toBan.getDiscriminator(), true)
                    .addField("Banned by", interact.getUser().getName() + "#" + interact.getUser().getDiscriminator(), true)
                    .addField("Reason", reason, false)
                    .setColor(Main.accentColor);

            MessageUtils.send("825336003018489867", embed.setColor(0xFF5555)); // #public-logs
            MessageUtils.send("898580934440394752", embed.setColor(0xFF5555)); // #mod-logs
            reply(interact, embed);
        } catch (Exception ex) {
            ex.printStackTrace();
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("An error occurred while trying to ban this user!")
                    .addField("Stacktrace", "```java\n" + ex.getMessage() + "```", false)
                    .setColor(0xFF5555);
            reply(interact, embed);
        }
    }
}
