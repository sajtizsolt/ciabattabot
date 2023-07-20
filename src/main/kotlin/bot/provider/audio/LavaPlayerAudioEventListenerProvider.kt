package bot.provider.audio

import bot.eventListener.LavaPlayerAudioEventListener

object LavaPlayerAudioEventListenerProvider {

    private val eventListeners = hashMapOf<Long, LavaPlayerAudioEventListener>()

    fun getOrCreateInstance(guildId: Long): LavaPlayerAudioEventListener =
        eventListeners.getOrPut(guildId) { LavaPlayerAudioEventListener(guildId) }
}
