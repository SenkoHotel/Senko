package senkohotel.senko.command;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.SlashCommand;

public class SaySlashCommand extends SlashCommand {
    public SaySlashCommand() {
        name = "say";
        description = "Says something";
        options.add(new OptionData(OptionType.STRING, "message", "The message to say", true));
        options.add(new OptionData(OptionType.STRING, "messageid", "The message id to reply to", false));
    }

    @Override
    public void exec(SlashCommandInteraction interact) {
        if (!interact.getMember().getRoles().contains(interact.getGuild().getRoleById("812582931792134154"))) {
            interact.reply("no").setEphemeral(true).complete();
            return;
        }

        String message = interact.getOption("message").getAsString();
        String messageid = interact.getOption("messageid") != null ? interact.getOption("messageid").getAsString() : null;

        try {
            if (messageid != null) {
                interact.getChannel().retrieveMessageById(messageid).queue(msg -> {
                    msg.reply(message).queue();
                });
            } else {
                interact.getChannel().sendMessage(message).complete();
            }

            interact.reply("Sent message!").setEphemeral(true).complete();
            Main.LOG.info(interact.getUser().getAsTag() + " said '" + message + (messageid != null ? "' replying to " + messageid : "'") + " in " + interact.getChannel().getName() + " (" + interact.getChannel().getId() + ")");
        } catch (Exception ex) {
            interact.reply("Failed to send message!").setEphemeral(true).complete();
        }

    }
}
