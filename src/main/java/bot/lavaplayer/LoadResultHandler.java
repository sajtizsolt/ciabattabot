package bot.lavaplayer;

import bot.utils.Constants;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadResultHandler implements AudioLoadResultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadResultHandler.class);

    private GuildMusicManager musicManager;
    private MessageReceivedEvent event;
    private boolean canInterrupt;

    public LoadResultHandler(GuildMusicManager musicManager, MessageReceivedEvent event, boolean interrupt) {
        this.musicManager = musicManager;
        this.event = event;
        canInterrupt = interrupt;
        musicManager.scheduler.setEvent(event);
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        LOGGER.info("track loaded");
        connect(event.getMember());
        musicManager.scheduler.queue(track, canInterrupt);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        connect(event.getMember());
        if (playlist.isSearchResult()) {
            musicManager.scheduler.queue(playlist.getTracks().get(0), canInterrupt);
            LOGGER.info("search result loaded");
        } else {
            event.getChannel().sendMessage("Playlist **" + playlist.getName() + "** added to queue!").queue();
            for (AudioTrack track : playlist.getTracks()) {
                musicManager.scheduler.queue(track, canInterrupt);
            }
            LOGGER.info("playlist loaded");
        }
    }

    @Override
    public void noMatches() {
        event.getChannel().sendMessage("No matches found " + Constants.PENSIVE).queue();
        LOGGER.info("no matches");
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        event.getChannel().sendMessage("Load failed " + Constants.PENSIVE).queue();
        LOGGER.info("load failed");
    }

    private void connect(Member callingMember) {
        Guild guild = callingMember.getGuild();

        if (!guild.getAudioManager().isConnected()) {
            int nonEmptyVoiceChannels = 0;
            VoiceChannel nonEmptyVC = null;
            for (VoiceChannel channel : guild.getVoiceChannels()) {
                if (!channel.getMembers().isEmpty()) {
                    if (channel.getMembers().contains(callingMember)) {
                        nonEmptyVoiceChannels++;
                        guild.getAudioManager().openAudioConnection(channel);
                        break;
                    } else {
                        nonEmptyVoiceChannels++;
                        nonEmptyVC = channel;
                    }
                }
            }

            if (!guild.getAudioManager().isConnected() && nonEmptyVC != null && nonEmptyVoiceChannels == 1) {
                guild.getAudioManager().openAudioConnection(nonEmptyVC);
            } else if (nonEmptyVoiceChannels == 0) {
                guild.getAudioManager().openAudioConnection(guild.getVoiceChannels().get(0));
            }
        }
    }

    public GuildMusicManager getMusicManager() {
        return musicManager;
    }

    public void setMusicManager(GuildMusicManager musicManager) {
        this.musicManager = musicManager;
    }
}
