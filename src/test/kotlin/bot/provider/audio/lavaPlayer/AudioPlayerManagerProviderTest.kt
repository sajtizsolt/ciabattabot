package bot.provider.audio.lavaPlayer

import bot.service.configuration.ConfigurationService
import dev.lavalink.youtube.YoutubeAudioSourceManager
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AudioPlayerManagerProviderTest {

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `when getOrCreateInstance is called, then audio player manager is created and registered with youtube source`() {
        mockkObject(ConfigurationService)
        every { ConfigurationService.getYoutubeOauthRefreshToken() } returns null
        every { ConfigurationService.isYoutubeOauthEnabled() } returns false

        val manager = AudioPlayerManagerProvider.getOrCreateInstance()

        assertNotNull(manager)
        val sourceManager = manager.source(YoutubeAudioSourceManager::class.java)
        assertNotNull(sourceManager)
        assertTrue(sourceManager is YoutubeAudioSourceManager)
    }
}
