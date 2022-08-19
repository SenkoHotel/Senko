package senkohotel.senko.command;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import senkohotel.hotelbot.commands.SlashCommand;
import senkohotel.senko.fun.canvas.Canvas;

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

        reply(interact, "Applying changes to canvas...");

        Canvas.setPixel(x, y, color);
        Canvas.saveCanvas();
        Canvas.render(interact.getChannel());
    }
}
