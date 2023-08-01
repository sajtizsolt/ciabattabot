package bot.service.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame
import net.dv8tion.jda.api.audio.AudioSendHandler
import java.nio.ByteBuffer

class LavaPlayerAudioSendHandler(
    private val audioPlayer: AudioPlayer,
) : AudioSendHandler {

    // TODO: Move capacity to configuration
    private val buffer = ByteBuffer.allocate(1024)

    private val frame = MutableAudioFrame()

    init {
        frame.setBuffer(buffer)
    }

    override fun canProvide(): Boolean =
        audioPlayer.provide(frame)

    override fun provide20MsAudio(): ByteBuffer? =
        buffer.flip()

    override fun isOpus(): Boolean =
        true
}
