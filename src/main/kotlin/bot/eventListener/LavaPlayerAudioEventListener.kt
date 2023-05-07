package bot.eventListener

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import mu.KotlinLogging

class LavaPlayerAudioEventListener : AudioEventAdapter() {

    private val logger = KotlinLogging.logger {}

    override fun onPlayerResume(player: AudioPlayer?) {
        logger.info { "onPlayerResume" }
    }

    override fun onPlayerPause(player: AudioPlayer?) {
        logger.info { "onPlayerPause" }
    }

    override fun onTrackStart(player: AudioPlayer?, track: AudioTrack?) {
        logger.info { "onTrackStart" }
    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack?, endReason: AudioTrackEndReason?) {
        logger.info { "onTrackEnd" }
    }

    override fun onTrackException(player: AudioPlayer?, track: AudioTrack?, exception: FriendlyException?) {
        logger.info { "onTrackException" }
    }

    override fun onTrackStuck(player: AudioPlayer?, track: AudioTrack?, thresholdMs: Long) {
        logger.info { "onTrackStuck" }
    }

    override fun onTrackStuck(
        player: AudioPlayer?,
        track: AudioTrack?,
        thresholdMs: Long,
        stackTrace: Array<StackTraceElement?>?
    ) {
        onTrackStuck(player, track, thresholdMs)
    }
}
