package senkohotel.senko.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.SlashCommand;
import senkohotel.senko.util.UserUtils;

public class SlowmodeSlashCommand extends SlashCommand {
    public SlowmodeSlashCommand() {
        name = "slowmode";
        description = "Sets the slowmode in the current channel.";
        options.add(new OptionData(OptionType.INTEGER, "time", "Slowmode time. (in seconds, max 21600)", true));
    }

    public void exec(SlashCommandInteraction interact) {
        if (!UserUtils.hasRole(interact.getMember(), "792173231040757780")) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Only moderators can use this command!")
                    .setColor(0xFF5555);
            reply(interact, embed);
            return;
        }

        int time = interact.getOption("time").getAsInt();

        if (time > TextChannel.MAX_SLOWMODE) {
            reply(interact, "Value to high! Please select something below " + TextChannel.MAX_SLOWMODE);
            return;
        }

        interact.getChannel().asTextChannel().getManager().setSlowmode(time).complete();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Set slowmode in this channel!")
                .setColor(Main.accentColor)
                .addField("Time", time + " seconds", false);

        reply(interact, embed);
    }
}
