package senkohotel.hotelbot.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.CommandList;
import senkohotel.hotelbot.utils.MessageUtils;
import senkohotel.senko.moderation.AutoMod;
import senkohotel.senko.util.AfkUtils;

import java.util.concurrent.TimeUnit;

public class MessageListener extends ListenerAdapter {
    public void onMessageReceived(@NotNull MessageReceivedEvent msg) {
        if (AutoMod.check(msg))
            return;

        if (!AfkUtils.isAfk(msg.getAuthor().getId()).equals("")) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(msg.getAuthor().getAsTag() + " is no longer afk!")
                    .setColor(Main.accentColor)
                    .setFooter("This message will be deleted in 10 seconds.");

            AfkUtils.removeUser(msg.getAuthor().getId());

            msg.getChannel().sendMessageEmbeds(embed.build()).queue((message) -> {
                message.delete().queueAfter(10, TimeUnit.SECONDS);
            });
        }

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

        for (IMentionable mention : msg.getMessage().getMentions().getMentions(Message.MentionType.USER)) {
            String reason = AfkUtils.isAfk(mention.getId());
            if (!reason.equals("")) {
                User u = Main.bot.getUserById(mention.getId());
                if (u == null)
                    u = Main.bot.retrieveUserById(mention.getId()).complete();

                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(u.getAsTag() + " is currently AFK!")
                        .setColor(Main.accentColor)
                        .addField("Reason", reason, false)
                        .setFooter("This message will be deleted in 10 seconds.");


                msg.getChannel().sendMessageEmbeds(embed.build()).queue((message) -> {
                    message.delete().queueAfter(10, TimeUnit.SECONDS);
                });
            }
        }
    }
}