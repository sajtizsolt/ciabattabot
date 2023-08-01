package bot.provider.audio

import bot.extension.setAudioSendHandler
import bot.service.audio.GuildAudioPlayer
import bot.service.discord.GuildService

object GuildAudioPlayerProvider {

    private val audioPlayers = hashMapOf<Long, GuildAudioPlayer>()

    fun getOrCreateInstance(guildId: Long): GuildAudioPlayer {
        return audioPlayers.getOrPut(guildId) {
            val audioPlayer = LavaPlayerAudioPlayerProvider.getOrCreateInstance(guildId)
            val lavaPlayerAudioEventListener = LavaPlayerAudioEventListenerProvider.getOrCreateInstance(guildId)
            audioPlayer.addListener(lavaPlayerAudioEventListener)
            val audioSendHandler = LavaPlayerAudioSendHandlerProvider.getOrCreateInstance(guildId)
            GuildService.getGuildById(guildId).setAudioSendHandler(audioSendHandler)
            GuildAudioPlayer(
                audioPlayer = audioPlayer,
            )
        }
    }
}
