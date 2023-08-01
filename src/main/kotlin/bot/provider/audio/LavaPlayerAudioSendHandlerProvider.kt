package bot.provider.audio

import bot.service.audio.LavaPlayerAudioSendHandler
import net.dv8tion.jda.api.audio.AudioSendHandler

object LavaPlayerAudioSendHandlerProvider {

    private val audioSendHandlers = hashMapOf<Long, AudioSendHandler>()

    fun getOrCreateInstance(guildId: Long): AudioSendHandler {
        return audioSendHandlers.getOrPut(guildId) {
            LavaPlayerAudioSendHandler(
                audioPlayer = LavaPlayerAudioPlayerProvider.getOrCreateInstance(guildId),
            )
        }
    }
}
