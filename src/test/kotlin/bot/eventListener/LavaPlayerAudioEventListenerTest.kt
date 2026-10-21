package bot.eventListener

import bot.service.audio.GuildAudioPlayerService
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class LavaPlayerAudioEventListenerTest {

    private val guildId = 12345L
    private val listener = LavaPlayerAudioEventListener(guildId)

    @Test
    fun `when onTrackStart is called, then it executes without throwing`() {
        val player = mockk<AudioPlayer>()
        val track = mockk<AudioTrack>()
        every { track.identifier } returns "test-track-id"

        assertDoesNotThrow {
            listener.onTrackStart(player, track)
        }
    }

    @Test
    fun `when onTrackEnd is called, then it plays next audio track`() {
        mockkObject(GuildAudioPlayerService)
        every { GuildAudioPlayerService.playNextAudioTrack(guildId) } returns Unit

        val player = mockk<AudioPlayer>()
        val track = mockk<AudioTrack>()
        every { track.identifier } returns "test-track-id"

        listener.onTrackEnd(player, track, AudioTrackEndReason.FINISHED)

        verify(exactly = 1) { GuildAudioPlayerService.playNextAudioTrack(guildId) }
    }

    @Test
    fun `when onTrackException is called, then it logs and handles exception without throwing`() {
        val player = mockk<AudioPlayer>()
        val track = mockk<AudioTrack>()
        every { track.identifier } returns "test-track-id"
        val exception = FriendlyException("Test failure", FriendlyException.Severity.COMMON, RuntimeException("Cause"))

        assertDoesNotThrow {
            listener.onTrackException(player, track, exception)
        }
    }
}
