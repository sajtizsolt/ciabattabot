package bot.service.audio

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers

object AudioPlayerManagerProvider {

    private lateinit var audioPlayerManager: AudioPlayerManager

    fun getOrCreateInstance(): AudioPlayerManager {
        if (!::audioPlayerManager.isInitialized) {
            initializeAudioPlayerManager()
        }
        return audioPlayerManager
    }

    private fun initializeAudioPlayerManager() {
        audioPlayerManager = DefaultAudioPlayerManager()

        // TODO: Move this to configuration
        audioPlayerManager.configuration.opusEncodingQuality = 10
        // TODO: Move this to configuration
        audioPlayerManager.configuration.resamplingQuality = AudioConfiguration.ResamplingQuality.HIGH

        AudioSourceManagers.registerRemoteSources(audioPlayerManager)
    }
}
