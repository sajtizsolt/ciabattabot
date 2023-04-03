package bot;

import bot.utils.Constants;
import bot.utils.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private static JDA jda;

    private static String readToken() {
        String token = System.getenv("DISCORD_BOT_TOKEN");

        if (StringUtils.isBlank(token))
            throw new IllegalArgumentException("Discord bot token is missing!");

        return token;
    }

    private static Map<Guild, TextChannel> retrieveDefaultChannels(){
        Map<Guild, TextChannel> defaultTextChannels = new HashMap<>();

        for(var guild : jda.getGuilds()){
            TextChannel defaultTextChannel = null;
            for(var channel : guild.getTextChannels()){
                if(channel.getName().toLowerCase().contains("general")){
                    defaultTextChannel = channel;
                    break;
                }
            }
            if (defaultTextChannel == null) {
                defaultTextChannel = guild.getTextChannels().get(0);
            }
            defaultTextChannels.put(guild, defaultTextChannel);
        }

        return defaultTextChannels;
    }

    private static List<GatewayIntent> getIntents() {
        List<GatewayIntent> ret = new ArrayList<>();
        ret.add(GatewayIntent.GUILD_EMOJIS_AND_STICKERS);
        ret.add(GatewayIntent.GUILD_MESSAGES);
        ret.add(GatewayIntent.GUILD_MEMBERS);
        ret.add(GatewayIntent.GUILD_PRESENCES);
        ret.add(GatewayIntent.GUILD_VOICE_STATES);

        return ret;
    }

    public static void main(String[] args) {

        try {

            jda = JDABuilder
                    .create(readToken(), getIntents()).setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .build()
                    .awaitReady();

            Constants.setDefaultTextChannels(Collections.unmodifiableMap(retrieveDefaultChannels()));

            jda.getPresence().setActivity(Activity.watching("Help command: " + Constants.PREFIX + "help"));

            jda.addEventListener(new MyEventListener());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
