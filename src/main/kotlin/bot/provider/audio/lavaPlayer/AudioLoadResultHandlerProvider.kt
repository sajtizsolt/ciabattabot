package bot.provider.audio.lavaPlayer

import bot.provider.audio.GuildAudioPlayerProvider
import bot.service.eventHandler.lavaPlayer.AudioLoadResultHandler

object AudioLoadResultHandlerProvider {

    private val loadResultHandlers = hashMapOf<Long, AudioLoadResultHandler>()

    fun getOrCreateInstance(guildId: Long): AudioLoadResultHandler {
        return loadResultHandlers.getOrPut(guildId) {
            AudioLoadResultHandler(
                guildAudioPlayer = GuildAudioPlayerProvider.getOrCreateInstance(guildId),
            )
        }
    }
}
