package senkohotel.senko.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.SlashCommand;

public class AvatarSlashCommand extends SlashCommand {
    public AvatarSlashCommand() {
        name = "avatar";
        description = "Displays a users avatar.";
        options.add(new OptionData(OptionType.USER, "user", "The user you want the avatar of (leave empty for your own avatar)"));
    }

    public void exec(SlashCommandInteraction interact) {
        OptionMapping userOption = interact.getOption("user"); // .getAsUser() is a NonNull that may produce a NullPointer?????
        User user;

        if (userOption == null)
            user = interact.getUser();
        else
            user = userOption.getAsUser();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(user.getAsTag() + "'s Avatar")
                .setColor(Main.accentColor)
                .setImage(user.getEffectiveAvatarUrl() + "?size=4096");

        reply(interact, embed);
    }
}
