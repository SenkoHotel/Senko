package senkohotel.senko.command;

import com.google.gson.JsonArray;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.hotelbot.commands.Command;
import senkohotel.hotelbot.utils.MessageUtils;

public class TopicCommand extends Command {
    public TopicCommand() {
        name = "topic";
        desc = "Get a random topic to talk about.";
    }

    public void exec(MessageReceivedEvent msg, String[] args) throws Exception {
        super.exec(msg, args);

        MessageUtils.reply(msg, "This has been moved to a slash command. Please use /topic instead.");
    }
}
