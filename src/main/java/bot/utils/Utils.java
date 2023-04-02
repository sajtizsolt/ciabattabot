package bot.utils;

import bot.service.Translator;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.apache.commons.lang.StringEscapeUtils;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

public class Utils {

    public static EmbedBuilder createBasicReactionEmbed(String title, List<String> fields) {
        EmbedBuilder ret = new EmbedBuilder();
        ret.setTitle(StringEscapeUtils.unescapeHtml(title));
        for (var f : fields) {
            ret.addField((fields.indexOf(f) + 1) + "\u20E3 " + StringEscapeUtils.unescapeHtml(f), "", false);
        }
        return ret;
    }

    public static EmbedBuilder createBasicReactionEmbed(String title, List<String> fields, String translateTo) {
        EmbedBuilder ret = new EmbedBuilder();
        ret.setTitle(Translator.getInstance().translate("en", translateTo, StringEscapeUtils.unescapeHtml(title)));
        for (var f : fields) {
            ret.addField((fields.indexOf(f) + 1) + "\u20E3 " + Translator.getInstance().translate("en", translateTo, StringEscapeUtils.unescapeHtml(f)), "", false);
        }
        return ret;
    }

    private static String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char) (i + 'A')) : null;
    }

    public static String getJsonFromAPI(String url) {
        var receivedContent = new StringBuilder();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            int status = connection.getResponseCode();
            if (status == 200) {
                var in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String receivedLine;
                while ((receivedLine = in.readLine()) != null)
                    receivedContent.append(receivedLine);
                in.close();
                connection.disconnect();
            } else {
                throw new RuntimeException("Response: " + status);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receivedContent.toString();
    }

    public static String getJsonPropertyValue(String json, String value) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(json).getAsJsonObject();
        return obj.get(value).getAsString();
    }

    public static Integer sendHttpRequest(String url, String type, @Nullable String body) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(type);
            if (body != null) {
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Content-length", Integer.toString(body.length()));
                connection.setDoOutput(true);
                connection.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
            }
            int status = connection.getResponseCode();
            connection.disconnect();
            return status;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 69;
    }

    public static Boolean isActualUserLeftInVoiceChannel(VoiceChannel channel) {
        if (channel == null) return false;
        int userCount = 0;
        for (var m : channel.getMembers()) {
            if (!m.getUser().isBot())
                userCount++;
        }
        return userCount != 0;
    }

    public static Properties getPropertiesFromResourceFile(String fileInResourcesFolder) {
        Properties properties = null;
        try {
            FileReader reader = new FileReader("src/main/resources/" + fileInResourcesFolder);
            properties = new Properties();
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
