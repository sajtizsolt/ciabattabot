package bot.provider.audio

import bot.service.eventHandler.LavaPlayerAudioLoadResultHandler

object LavaPlayerAudioLoadResultHandlerProvider {

    private val loadResultHandlers = hashMapOf<Long, LavaPlayerAudioLoadResultHandler>()

    fun getOrCreateInstance(guildId: Long): LavaPlayerAudioLoadResultHandler {
        return loadResultHandlers.getOrPut(guildId) {
            LavaPlayerAudioLoadResultHandler(
                guildAudioPlayer = GuildAudioPlayerProvider.getOrCreateInstance(guildId),
            )
        }
    }
}
