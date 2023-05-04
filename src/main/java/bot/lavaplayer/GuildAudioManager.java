package bot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrameBufferFactory;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GuildAudioManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuildAudioManager.class);

    AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private final Map<Long, GuildMusicManager> musicManagers = new HashMap<>();
    private GuildMusicManager musicManager;

    public GuildAudioManager() {

        playerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        playerManager.getConfiguration().setOpusEncodingQuality(10);

        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public void loadAndPlay(String url, MessageReceivedEvent event, boolean canInterrupt) {
        Guild guild = event.getGuild();

        musicManager = getGuildAudioPlayer(guild);

        playerManager.loadItemOrdered(musicManager, url, new LoadResultHandler(musicManager, event, canInterrupt));
    }

    public void stop(MessageReceivedEvent event) {
        loadAndPlay("data/sounds/batta_shutdown.mp3", event, true);

        Guild guild = event.getGuild();

        try {
            Thread.sleep(4000); // xd
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        musicManagers.clear();

        guild.getAudioManager().closeAudioConnection();
    }

    public GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());

        GuildMusicManager musicManager = musicManagers.get(guildId);
        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void skip(Guild guild) {
        musicManager = getGuildAudioPlayer(guild);
        musicManager.scheduler.nextTrack();
    }

    public void setVolume(Guild guild, int volume) {
        musicManager = getGuildAudioPlayer(guild);
        musicManager.player.setVolume(volume);
    }

    public int getVolume(Guild guild) {
        musicManager = getGuildAudioPlayer(guild);
        return musicManager.player.getVolume();
    }

    public void pause(Guild guild, boolean pause) {
        musicManager = getGuildAudioPlayer(guild);
        musicManager.player.setPaused(pause);
    }

    public AudioPlayerManager getPlayerManager() {
        return playerManager;
    }

    public GuildMusicManager getMusicManager() {
        return musicManager;
    }
}
