package bot.service.audio

import bot.eventListener.LavaPlayerAudioEventListener
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import mu.KotlinLogging

class GuildAudioPlayer(
    private val audioPlayer: AudioPlayer,
    private val lavaPlayerAudioEventListener: LavaPlayerAudioEventListener,
) {

    private val logger = KotlinLogging.logger {}

    fun pauseSong() {
        audioPlayer.isPaused = true
        logger.info { "Player is paused" }
    }

    fun playSong(track: AudioTrack) {
        if (audioPlayer.startTrack(track, true)) {
            logger.info { "Playing song: ${track.info.title}" }
        } else {
            logger.error { "Failed to play song: ${track.info.title}" }
        }
    }

    fun resumeSong() {
        audioPlayer.isPaused = false
        logger.info { "Player is resumed" }
    }

    fun stopSong() {
        audioPlayer.stopTrack()
        logger.info { "Player is stopped" }
    }
}
