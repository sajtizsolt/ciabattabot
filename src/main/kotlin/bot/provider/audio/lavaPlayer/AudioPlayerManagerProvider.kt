package bot.provider.audio.lavaPlayer

import bot.service.configuration.ConfigurationService
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import dev.lavalink.youtube.YoutubeAudioSourceManager
import dev.lavalink.youtube.clients.Android
import dev.lavalink.youtube.clients.AndroidVr
import dev.lavalink.youtube.clients.ClientOptions
import dev.lavalink.youtube.clients.Music
import dev.lavalink.youtube.clients.Tv
import dev.lavalink.youtube.clients.Web
import dev.lavalink.youtube.clients.WebEmbedded
import dev.lavalink.youtube.clients.skeleton.Client
import org.slf4j.LoggerFactory

object AudioPlayerManagerProvider {

    private val LOGGER = LoggerFactory.getLogger(AudioPlayerManagerProvider::class.java)
    private lateinit var audioPlayerManager: AudioPlayerManager

    private class AndroidClient(options: ClientOptions = ClientOptions.DEFAULT) : Android(options, false)

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

        configureYouTubeSourceManager(audioPlayerManager)

        AudioSourceManagers.registerRemoteSources(
            audioPlayerManager,
            com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager::class.java,
        )
    }

    private fun configureYouTubeSourceManager(playerManager: AudioPlayerManager) {
        val oauthRefreshToken = ConfigurationService.getYoutubeOauthRefreshToken()
        val oauthEnabled = ConfigurationService.isYoutubeOauthEnabled() || !oauthRefreshToken.isNullOrBlank()

        val androidVersion = "21.02.35"
        Android.BASE_CONFIG
            .withUserAgent("com.google.android.youtube/$androidVersion (Linux; U; Android ${Android.ANDROID_VERSION.osVersion}) gzip")
            .withClientField("clientVersion", androidVersion)

        Tv.BASE_CONFIG.withUserAgent("Mozilla/5.0 (PlayStation; PlayStation 4/12.00) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Safari/605.1.15")

        val clients = if (oauthEnabled) {
            arrayOf<Client>(Music(), AndroidClient(), Tv(), AndroidVr(), WebEmbedded(), Web())
        } else {
            arrayOf<Client>(Music(), AndroidClient(), AndroidVr(), WebEmbedded(), Web())
        }

        val youtubeAudioSourceManager = YoutubeAudioSourceManager(*clients)

        if (oauthEnabled) {
            if (!oauthRefreshToken.isNullOrBlank()) {
                LOGGER.info("Initializing YouTube OAuth with existing refresh token")
                youtubeAudioSourceManager.useOauth2(oauthRefreshToken, true)
            } else {
                LOGGER.info("Initializing YouTube OAuth device flow. Check logs for authentication code.")
                youtubeAudioSourceManager.useOauth2(null, false)
            }
        }

        playerManager.registerSourceManager(youtubeAudioSourceManager)
    }
}
