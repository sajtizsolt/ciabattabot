package bot.service.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import org.slf4j.LoggerFactory

class GuildAudioPlayer(
    private val audioPlayer: AudioPlayer,
) {
    fun isPlayingTrack(): Boolean {
        return audioPlayer.playingTrack != null
    }

    fun pauseAudioTrack() {
        audioPlayer.isPaused = true
        LOGGER.info("Player is paused")
    }

    fun playAudioTrack(track: AudioTrack) {
        if (audioPlayer.startTrack(track, true)) {
            LOGGER.info("Playing audio track: {}", track.identifier)
        } else {
            LOGGER.error("Failed to play audio track: {}", track.identifier)
        }
    }

    fun resumeAudioTrack() {
        audioPlayer.isPaused = false
        LOGGER.info("Player is resumed")
    }

    fun stopAudioTrack() {
        audioPlayer.stopTrack()
        LOGGER.info("Player is stopped")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(GuildAudioPlayer::class.java)
    }
}
