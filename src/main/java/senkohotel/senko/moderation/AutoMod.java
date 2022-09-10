package senkohotel.senko.moderation;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.hotelbot.utils.MessageUtils;

// mostly only used for invite links
public class AutoMod {
    public static boolean check(MessageReceivedEvent msg) {
        if (isInvite(msg.getMessage().getContentRaw()) && !msg.getChannel().getId().equals("800398421654503426")) {
            msg.getMessage().delete().complete();
            MessageUtils.reply(msg, msg.getAuthor().getAsMention() + "please dont send any discord invite links!");
            return true;
        }
        return false;
    }

    public static boolean isInvite(String message) {
        return message.contains("discord.gg/")
                || message.contains("discord.com/invite/")
                || message.contains("disboard.org/")
                || message.contains("dsc.gg/")
                || message.contains("discord.co/invite/");
    }
}
