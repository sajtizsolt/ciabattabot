package bot.provider.audio

import bot.extension.setAudioSendHandler
import bot.provider.audio.lavaPlayer.AudioEventListenerProvider
import bot.provider.audio.lavaPlayer.AudioPlayerProvider
import bot.provider.audio.lavaPlayer.AudioSendHandlerProvider
import bot.service.audio.GuildAudioPlayer
import bot.service.discord.GuildService

object GuildAudioPlayerProvider {

    private val audioPlayers = hashMapOf<Long, GuildAudioPlayer>()

    fun getOrCreateInstance(guildId: Long): GuildAudioPlayer {
        return audioPlayers.getOrPut(guildId) {
            val audioPlayer = AudioPlayerProvider.getOrCreateInstance(guildId)
            val lavaPlayerAudioEventListener = AudioEventListenerProvider.getOrCreateInstance(guildId)
            audioPlayer.addListener(lavaPlayerAudioEventListener)
            val audioSendHandler = AudioSendHandlerProvider.getOrCreateInstance(guildId)
            GuildService.getGuildById(guildId).setAudioSendHandler(audioSendHandler)
            GuildAudioPlayer(
                audioPlayer = audioPlayer,
            )
        }
    }
}
