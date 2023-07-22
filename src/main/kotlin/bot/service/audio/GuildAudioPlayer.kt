package bot.service.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import mu.KotlinLogging

class GuildAudioPlayer(
    private val audioPlayer: AudioPlayer,
) {

    private val logger = KotlinLogging.logger {}

    fun isPlayingTrack(): Boolean {
        return audioPlayer.playingTrack != null
    }

    fun pauseAudioTrack() {
        audioPlayer.isPaused = true
        logger.info { "Player is paused" }
    }

    fun playAudioTrack(track: AudioTrack) {
        if (audioPlayer.startTrack(track, true)) {
            logger.info { "Playing audio track: ${track.identifier}" }
        } else {
            logger.error { "Failed to play audio track: ${track.identifier}" }
        }
    }

    fun resumeAudioTrack() {
        audioPlayer.isPaused = false
        logger.info { "Player is resumed" }
    }

    fun stopAudioTrack() {
        audioPlayer.stopTrack()
        logger.info { "Player is stopped" }
    }
}
