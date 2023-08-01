package bot.provider.audio.lavaPlayer

import bot.eventListener.LavaPlayerAudioEventListener

object AudioEventListenerProvider {

    private val eventListeners = hashMapOf<Long, LavaPlayerAudioEventListener>()

    fun getOrCreateInstance(guildId: Long): LavaPlayerAudioEventListener =
        eventListeners.getOrPut(guildId) { LavaPlayerAudioEventListener(guildId) }
}
