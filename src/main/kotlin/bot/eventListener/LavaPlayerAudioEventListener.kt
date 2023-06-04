package bot.eventListener

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
        logger.info { "$guildId player resumed" }
    }

    override fun onPlayerPause(player: AudioPlayer?) {
        logger.info { "$guildId player paused" }
    }

    override fun onTrackStart(player: AudioPlayer?, track: AudioTrack?) {
        track?.let {
            logger.info { "$guildId player started playing ${it.info.title}" }
        }
    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack?, endReason: AudioTrackEndReason?) {
        track?.let {
            logger.info { "$guildId player finished playing ${it.info.title}" }
        }
    }

    override fun onTrackException(player: AudioPlayer?, track: AudioTrack?, exception: FriendlyException?) {
        track?.let {
            logger.error { "$guildId player throw an exception while playing ${it.info.title}" }
            logger.error { "Exception stacktrace ${exception?.stackTrace}" }
        }
    }

    override fun onTrackStuck(player: AudioPlayer?, track: AudioTrack?, thresholdMs: Long) {
        track?.let {
            logger.warn {
                "$guildId player stuck while playing ${it.info.title}"
            }
        }
    }
}
