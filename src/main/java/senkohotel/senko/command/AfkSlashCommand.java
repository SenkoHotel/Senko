package senkohotel.senko.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.SlashCommand;
import senkohotel.hotelbot.utils.MessageUtils;
import senkohotel.senko.util.AfkUtils;

public class AfkSlashCommand extends SlashCommand {
    public AfkSlashCommand() {
        name = "afk";
        description = "Sets your AFK status.";
        options.add(new OptionData(OptionType.STRING, "reason", "The reason to go AFK.", false));
    }

    public void exec(SlashCommandInteraction interact) {
        OptionMapping reasonMapping = interact.getOption("reason");

        String reason = "No reason provided.";
        if (reasonMapping != null) reason = reasonMapping.getAsString();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(interact.getUser().getAsTag() + " went AFK!")
                .setColor(Main.accentColor)
                .addField("Reason", reason, false);

        AfkUtils.addUser(interact.getUser().getId(), reason);

        reply(interact, embed);
    }
}
