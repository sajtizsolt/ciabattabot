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
        TODO()
    }

    override fun onPlayerPause(player: AudioPlayer?) {
        TODO()
    }

    override fun onTrackStart(player: AudioPlayer?, track: AudioTrack?) {
        TODO()
    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack?, endReason: AudioTrackEndReason?) {
        // Adapter dummy method
    }

    override fun onTrackException(player: AudioPlayer?, track: AudioTrack?, exception: FriendlyException?) {
        // Adapter dummy method
    }

    override fun onTrackStuck(player: AudioPlayer?, track: AudioTrack?, thresholdMs: Long) {
        // Adapter dummy method
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
