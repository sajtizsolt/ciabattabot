package bot.service.audio

import bot.eventListener.LavaPlayerAudioEventListener
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager

object AudioPlayerProviderService {

    private val audioPlayers = hashMapOf<Long, AudioPlayer>()

    private lateinit var audioPlayerManager: AudioPlayerManager

    fun getOrCreateInstance(guildId: Long): AudioPlayer {
        if (!::audioPlayerManager.isInitialized) {
            initializeAudioPlayerManager()
        }
        return audioPlayers.getOrPut(guildId) {
            val audioPlayer = audioPlayerManager.createPlayer()
            audioPlayer.addListener { LavaPlayerAudioEventListener() }
            audioPlayer
        }
    }

    private fun initializeAudioPlayerManager() {
        audioPlayerManager = DefaultAudioPlayerManager()
        // TODO: Move this to configuration
        audioPlayerManager.configuration.opusEncodingQuality = 10
        // TODO: Move this to configuration
        audioPlayerManager.configuration.resamplingQuality = AudioConfiguration.ResamplingQuality.HIGH
    }
}
