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

        MessageUtils.reply(msg, "This has been moved to a slash command. Please use /afk instead.");
    }
}
