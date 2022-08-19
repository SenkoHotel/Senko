package senkohotel.senko.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.Command;
import senkohotel.hotelbot.utils.MessageUtils;
import senkohotel.senko.util.AfkUtils;

public class AfkCommand extends Command {
    public AfkCommand() {
        name = "afk";
        desc = "find a good description";
    }

    public void exec(MessageReceivedEvent msg, String[] args) throws Exception {
        super.exec(msg, args);

        String reason = "No reason provided.";

        if (args.length > 0) {
            reason = String.join(" ", args);
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(msg.getAuthor().getAsTag() + " went AFK!")
                .setColor(Main.accentColor)
                .addField("Reason", reason, false);

        AfkUtils.addUser(msg.getAuthor().getId(), reason);

        MessageUtils.reply(msg, embed);
    }
}
