package bot.eventListener

import bot.service.audio.GuildAudioPlayerService
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import mu.KotlinLogging

class LavaPlayerAudioEventListener(
    private val guildId: Long,
) : AudioEventAdapter() {

    private val logger = KotlinLogging.logger {}

    override fun onPlayerResume(player: AudioPlayer?) {
        logger.info { "Player [guildId=$guildId] resumed" }
    }

    override fun onPlayerPause(player: AudioPlayer?) {
        logger.info { "Player [guildId=$guildId] paused" }
    }

    override fun onTrackStart(player: AudioPlayer?, track: AudioTrack?) {
        track?.let {
            logger.info { "Player [guildId=$guildId] started playing ${it.identifier}" }
        }
    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack?, endReason: AudioTrackEndReason?) {
        track?.let {
            logger.info { "Player [guildId=$guildId] finished playing ${it.identifier}" }
            GuildAudioPlayerService.playAudioTrack(guildId)
        }
    }

    override fun onTrackException(player: AudioPlayer?, track: AudioTrack?, exception: FriendlyException?) {
        track?.let {
            logger.error { "Player [guildId=$guildId] throw an exception while playing ${it.identifier}" }
            logger.error { "Exception stacktrace ${exception?.stackTrace}" }
        }
    }

    override fun onTrackStuck(player: AudioPlayer?, track: AudioTrack?, thresholdMs: Long) {
        track?.let {
            logger.warn {
                "Player [guildId=$guildId] stuck while playing ${it.identifier}"
            }
        }
    }
}
