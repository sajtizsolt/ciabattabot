package bot.provider.audio

import bot.service.eventHandler.LavaPlayerAudioLoadResultHandler

object LavaPlayerAudioLoadResultHandlerProvider {

    private val loadResultHandlers = hashMapOf<Long, LavaPlayerAudioLoadResultHandler>()

    fun getOrCreateInstance(guildId: Long): LavaPlayerAudioLoadResultHandler {
        val guildAudioPlayer = GuildAudioPlayerProvider.getOrCreateInstance(guildId)
        return loadResultHandlers.getOrPut(guildId) {
            LavaPlayerAudioLoadResultHandler(
                guildAudioPlayer = GuildAudioPlayerProvider.getOrCreateInstance(guildId),
            )
        }
    }
}
