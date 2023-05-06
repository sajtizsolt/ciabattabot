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

    fun playSong(track: AudioTrack) {
        if (audioPlayer.startTrack(track, false)) {
            logger.info { "Playing song: ${track.info.title}" }
        } else {
            logger.error { "Failed to play song: ${track.info.title}" }
        }
    }
}
