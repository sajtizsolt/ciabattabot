package bot.provider.audio

import bot.eventListener.LavaPlayerAudioEventListener
import bot.extension.setAudioSendHandler
import bot.lavaplayer.AudioPlayerSendHandler
import bot.service.audio.GuildAudioPlayer
import bot.service.discord.GuildService

object GuildAudioPlayerProvider {

    private val audioPlayers = hashMapOf<Long, GuildAudioPlayer>()

    fun getOrCreateInstance(guildId: Long): GuildAudioPlayer {
        val audioPlayerManager = AudioPlayerManagerProvider.getOrCreateInstance()
        return audioPlayers.getOrPut(guildId) {
            val audioPlayer = audioPlayerManager.createPlayer()
            val lavaPlayerAudioEventListener = LavaPlayerAudioEventListener(guildId)
            audioPlayer.addListener(lavaPlayerAudioEventListener)
            GuildService.getGuildById(guildId).setAudioSendHandler(AudioPlayerSendHandler(audioPlayer))
            GuildAudioPlayer(
                audioPlayer = audioPlayer,
            )
        }
    }
}
