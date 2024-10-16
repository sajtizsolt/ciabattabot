package bot.service.eventHandler.lavaPlayer

import bot.service.audio.GuildAudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import mu.KotlinLogging

class AudioLoadResultHandler(
    private val guildAudioPlayer: GuildAudioPlayer,
) : AudioLoadResultHandler {

    private val logger = KotlinLogging.logger {}

    override fun trackLoaded(track: AudioTrack?) {
        logger.info { "trackLoaded" }
        track?.let { guildAudioPlayer.playAudioTrack(track) }
    }

    override fun playlistLoaded(playlist: AudioPlaylist?) {
        logger.info { "playlistLoaded" }
    }

    override fun noMatches() {
        logger.info { "noMatches" }
    }

    override fun loadFailed(exception: FriendlyException?) {
        logger.info { "loadFailed" }
        logger.error { exception?.stackTrace.contentToString() }
    }
}
