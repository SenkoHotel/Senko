package senkohotel.senko.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.SlashCommand;
import senkohotel.hotelbot.utils.MessageUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

public class TopicSlashCommand extends SlashCommand {
    static JsonArray topics = null;

    public TopicSlashCommand() {
        name = "topic";
        description = "Get a random topic to talk about.";
    }

    public void exec(SlashCommandInteraction interact) {
        if (topics == null) { // if empty fetch the topic list from my website
            try {
                Main.LOG.info("[TopicCommand] Fetching topic list...");
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest req = HttpRequest.newBuilder(
                        URI.create("https://flustix.foxes4life.net/assets/topics.json")
                ).header("Accept", "application/json, text/plain, /").build();

                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
                topics = JsonParser.parseString(res.body()).getAsJsonObject().get("topics").getAsJsonArray();
                Main.LOG.info("[TopicCommand] Done!");
            } catch (Exception e) {
                e.printStackTrace();
                reply(interact, "An error occurred while fetching the topic list.");
                return;
            }
        }

        Random rng = new Random();
        int selected = rng.nextInt(topics.size());

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Random Topic")
                .addField("Topic", topics.get(selected).getAsString(), false)
                .setFooter("Requested by " + interact.getUser().getAsTag(), interact.getUser().getEffectiveAvatarUrl())
                .setColor(Main.accentColor);

        reply(interact, embed);
    }
}
