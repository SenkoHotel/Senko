package senkohotel.senko.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.SlashCommand;
import senkohotel.hotelbot.utils.MessageUtils;
import senkohotel.senko.util.UserUtils;

public class KickSlashCommand extends SlashCommand {
    public KickSlashCommand() {
        name = "kick";
        description = "Kicks a user from the server";
        options.add(new OptionData(OptionType.USER, "user", "The User to be banned.", true));
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
            User toKick = interact.getOption("user").getAsUser();

            interact.getGuild().kick(toKick).complete();

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("User kicked! <:SK_cultured:792020838801997854>")
                    .addField("User", toKick.getName() + "#" + toKick.getDiscriminator(), true)
                    .addField("Kicked by", interact.getUser().getName() + "#" + interact.getUser().getDiscriminator(), true)
                    .setColor(Main.accentColor);

            MessageUtils.send("825336003018489867", embed); // #public-logs
            MessageUtils.send("898580934440394752", embed); // #mod-logs
            reply(interact, embed);
        } catch (Exception ex) {
            ex.printStackTrace();
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("An error occurred while trying to kick this user!")
                    .addField("Stacktrace", "```java\n" + ex.getMessage() + "```", false)
                    .setColor(0xFF5555);
            reply(interact, embed);
        }
    }
}
