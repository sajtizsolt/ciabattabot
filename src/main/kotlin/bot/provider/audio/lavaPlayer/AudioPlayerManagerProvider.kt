package bot.provider.audio.lavaPlayer

import bot.service.configuration.ConfigurationService
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import dev.lavalink.youtube.YoutubeAudioSourceManager
import dev.lavalink.youtube.clients.Web

object AudioPlayerManagerProvider {

    private lateinit var audioPlayerManager: AudioPlayerManager

    fun getOrCreateInstance(): AudioPlayerManager {
        if (!AudioPlayerManagerProvider::audioPlayerManager.isInitialized) {
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

        // Register up-to-date source manager for YouTube
        Web.setPoTokenAndVisitorData(
            ConfigurationService.getPoToken(),
            ConfigurationService.getPoVisitorData(),
        )
        val youtubeAudioSourceManager = YoutubeAudioSourceManager()
        audioPlayerManager.registerSourceManager(youtubeAudioSourceManager)
        AudioSourceManagers.registerRemoteSources(
            audioPlayerManager,
            com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager::class.java,
        )
    }
}
