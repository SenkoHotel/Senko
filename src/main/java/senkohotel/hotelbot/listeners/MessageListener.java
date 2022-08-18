package senkohotel.hotelbot.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.CommandList;
import senkohotel.hotelbot.utils.MessageUtils;

public class MessageListener extends ListenerAdapter {
    public void onMessageReceived(@NotNull MessageReceivedEvent msg) {
        String content = msg.getMessage().getContentRaw().toLowerCase();
        for (String prefix : Main.prefix) {
            if (content.startsWith(prefix))
                CommandList.check(msg, prefix);
        }

        if (msg.getAuthor().isBot())
            return;

        if (msg.getChannel().getId().equals("825400708316397628") || msg.getChannel().getId().equals("901062083217604638")) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor(msg.getAuthor().getAsTag(), null, msg.getAuthor().getAvatarUrl())
                    .setColor(Main.accentColor)
                    .setDescription(msg.getMessage().getContentRaw());

            msg.getMessage().delete().complete();

            Message sentEmbed = MessageUtils.send(msg.getChannel().getId(), embed);
            sentEmbed.addReaction(Emoji.fromUnicode("\u2705")).complete();
            sentEmbed.addReaction(Emoji.fromUnicode("\u274c")).complete();
        }
    }
}