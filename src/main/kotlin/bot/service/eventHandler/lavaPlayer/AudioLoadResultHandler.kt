package bot.service.eventHandler.lavaPlayer

import bot.service.audio.GuildAudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import org.slf4j.LoggerFactory

class AudioLoadResultHandler(
    private val guildAudioPlayer: GuildAudioPlayer,
) : AudioLoadResultHandler {

    override fun trackLoaded(track: AudioTrack?) {
        LOGGER.info("trackLoaded")
        track?.let { guildAudioPlayer.playAudioTrack(track) }
    }

    override fun playlistLoaded(playlist: AudioPlaylist?) {
        LOGGER.info("playlistLoaded")
    }

    override fun noMatches() {
        LOGGER.info("noMatches")
    }

    override fun loadFailed(exception: FriendlyException?) {
        LOGGER.info("loadFailed")
        LOGGER.error(exception?.stackTrace.contentToString())
    }
    
    companion object {
        private val LOGGER = LoggerFactory.getLogger(AudioLoadResultHandler::class.java)
    }
}
