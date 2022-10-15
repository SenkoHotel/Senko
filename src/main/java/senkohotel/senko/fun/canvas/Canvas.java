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
    private static final List<List<Integer>> canvas = new ArrayList<>();
    private static final int scaleFactor = 4;

    public static boolean loaded = false;
    public final static int canvasSize = 64;
    public final static File canvasFile = new File("canvas.png");

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
        try {
            BufferedImage image = ImageIO.read(canvasFile);
            for (int y = 0; y < canvasSize; y++) {
                List<Integer> row = new ArrayList<>();

                for (int x = 0; x < canvasSize; x++) {
                    row.add(image.getRGB(x * scaleFactor, y * scaleFactor));
                }

                canvas.set(y, row);
            }
            loaded = true;
        } catch (Exception ex) {
            Main.LOG.error("Failed to read canvas file");
        }
    }

    public static boolean setPixel(int x, int y, int color) {
        if (x < 0 || x >= canvasSize || y < 0 || y >= canvasSize) return false;
        canvas.get(y).set(x, color);
        return true;
    }

    public static void render() {
        BufferedImage img = new BufferedImage(canvasSize, canvasSize, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = img.createGraphics();

        for (int y = 0; y < canvasSize; y++) {
            for (int x = 0; x < canvasSize; x++) {
                g.setColor(new Color(canvas.get(y).get(x)));
                g.drawRect(x, y, 0, 0);
            }
        }

        g.dispose();
        Image imgScaled = img.getScaledInstance(canvasSize * scaleFactor, canvasSize * scaleFactor, Image.SCALE_DEFAULT);
        BufferedImage imgScaledBuffer = new BufferedImage(canvasSize * scaleFactor, canvasSize * scaleFactor, BufferedImage.TYPE_INT_RGB);
        imgScaledBuffer.getGraphics().drawImage(imgScaled, 0, 0, null);

        try {
            ImageIO.write(imgScaledBuffer, "png", canvasFile);
        } catch (Exception ignored) {
        }
    }
}
