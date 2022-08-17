package senkohotel.hotelbot;

import com.google.gson.JsonParser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import senkohotel.hotelbot.commands.CommandList;
import senkohotel.hotelbot.commands.SlashCommandList;
import senkohotel.hotelbot.listeners.MessageListener;
import senkohotel.hotelbot.listeners.SlashCommandListener;

import javax.security.auth.login.LoginException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;

public class Main {
    public static String[] prefix = {"senko ", "semk ", "s?"};
    public static JDA bot;
    public static int accentColor = 0xfdc964;
    public static Logger LOG = LoggerFactory.getLogger("senko");
    public static boolean debug = false; // just adds the slash commands instantly

    public static void main(String[] args) throws LoginException {
        CommandList.initList();
        SlashCommandList.initList();

        JDABuilder jda = JDABuilder.createDefault(loadToken());
        jda.enableIntents(EnumSet.allOf(GatewayIntent.class));
        jda.setRawEventsEnabled(true);
        bot = jda.build();
        bot.addEventListener(new MessageListener());
        bot.addEventListener(new SlashCommandListener());

        SlashCommandList.initGlobal();
    }

    static String loadToken() {
        try {
            return JsonParser.parseString(Files.readString(Path.of("config/senko.json"))).getAsJsonObject().get("token").getAsString();
        } catch (Exception ex) {
            System.out.println("Failed to load token");
            return "";
        }
    }
}
