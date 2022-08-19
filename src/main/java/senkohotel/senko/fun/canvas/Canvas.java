package senkohotel.senko.fun.canvas;

import net.dv8tion.jda.api.entities.MessageChannel;
import senkohotel.hotelbot.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Canvas {
    static List<List<Integer>> canvas = new ArrayList<>();

    static int canvasSize = 64;

    public static void createEmpty() {
        for (int y = 0; y < canvasSize; y++) {
            List<Integer> row = new ArrayList<>();

            for (int x = 0; x < canvasSize; x++) {
                row.add(0xFFFFFF);
            }

            canvas.add(row);
        }
    }

    public static void readFile() {
        String raw;
        try {
            raw = Files.readString(Path.of("canvas.csv"));
        } catch (Exception ex) {
            Main.LOG.error("Failed to read canvas", ex);
            return;
        }

        String[] lines = raw.split("\n");
        int x = 0;
        int y = 0;

        for (String line : lines) {
            for (String pixel : line.split(",")) {
                int color = Integer.parseInt(pixel);
                canvas.get(y).set(x, color);
                x++;
            }
            x = 0;
            y++;
        }
    }

    public static void setPixel(int x, int y, int color) {
        canvas.get(y).set(x, color);
    }

    public static void saveCanvas() {
        int x = 0;
        int y = 0;

        String canvasFile = "";

        for (List<Integer> row : canvas) {
            for (Integer pixel : row) {
                if (x != 0)
                    canvasFile += ",";

                canvasFile += pixel + "";

                x++;
            }
            x = 0;
            y++;

            if (y != canvasSize)
                canvasFile += "\n";
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("canvas.csv"));
            writer.write(canvasFile);
            writer.close();
        } catch (Exception ex) {
            Main.LOG.error("Couldn't save the canvas!", ex);
        }
    }

    public static void render(MessageChannel channel) {
        BufferedImage img = new BufferedImage(canvasSize, canvasSize, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = img.createGraphics();

        for (int y = 0; y < canvasSize; y++) {
            for (int x = 0; x < canvasSize; x++) {
                g.setColor(new Color(canvas.get(y).get(x)));
                g.drawRect(x, y, 0, 0);
            }
        }

        g.dispose();
        Image imgScaled = img.getScaledInstance(canvasSize * 4, canvasSize * 4, Image.SCALE_DEFAULT);
        BufferedImage imgScaledBuffer = new BufferedImage(canvasSize * 4, canvasSize * 4, BufferedImage.TYPE_INT_RGB);
        imgScaledBuffer.getGraphics().drawImage(imgScaled, 0, 0, null);

        File file = new File("canvas.png");

        try {
            ImageIO.write(imgScaledBuffer, "png", file);
        } catch (Exception ignored) {
        }

        channel.sendFile(new File("canvas.png")).complete();
    }
}
