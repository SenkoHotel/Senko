package senkohotel.senko.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.Command;
import senkohotel.hotelbot.utils.MessageUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TopicCommand extends Command {
    static JsonArray topics = null;

    public TopicCommand() {
        name = "topic";
        desc = "Get a random topic to talk about.";
    }

    public void exec(MessageReceivedEvent msg, String[] args) throws Exception {
        super.exec(msg, args);

        if (topics == null) { // if empty fetch the topic list from my website
            Main.LOG.info("[TopicCommand] Fetching topic list...");
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder(
                    URI.create("https://flustix.foxes4life.net/assets/topics.json")
            ).header("Accept", "application/json, text/plain, /").build();

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            topics = JsonParser.parseString(res.body()).getAsJsonObject().get("topics").getAsJsonArray();
            Main.LOG.info("[TopicCommand] Done!");
        }

        Random rng = new Random();
        int selected = rng.nextInt(topics.size());

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Random Topic")
                .addField("Topic", topics.get(selected).getAsString(), false)
                .setColor(Main.accentColor);

        MessageUtils.reply(msg, embed);
    }
}
