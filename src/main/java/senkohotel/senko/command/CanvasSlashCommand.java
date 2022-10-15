package senkohotel.senko.command;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import senkohotel.hotelbot.commands.SlashCommand;
import senkohotel.senko.fun.canvas.Canvas;

import java.io.File;

public class CanvasSlashCommand extends SlashCommand {
    public CanvasSlashCommand() {
        name = "canvas";
        description = "Draw a pixel to an image";
        options.add(new OptionData(OptionType.INTEGER, "x", "Horizontal Position", true));
        options.add(new OptionData(OptionType.INTEGER, "y", "Vertical Position", true));
        options.add(new OptionData(OptionType.STRING, "color", "Hex Color (eg. #FFFFFF)", true));
    }

    public void exec(SlashCommandInteraction interact) {
        int x = interact.getOption("x").getAsInt();
        int y = interact.getOption("y").getAsInt();
        String colorStr = interact.getOption("color").getAsString();
        if (colorStr.startsWith("#"))
            colorStr = colorStr.substring(1);
        int color = Integer.parseInt(colorStr, 16);

        interact.reply("Drawing...").queue((hook)->{
            boolean done = Canvas.setPixel(x, y, color);

            if (done) {
                Canvas.render();
                hook.editOriginal("").addFile(Canvas.canvasFile, "canvas.png").queue();
            } else {
                hook.editOriginal("Pixel out of bounds! Max is " + Canvas.canvasSize + "px").queue();
            }
        });
    }
}
