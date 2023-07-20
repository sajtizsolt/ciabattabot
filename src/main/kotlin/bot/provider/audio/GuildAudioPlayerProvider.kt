package bot.provider.audio

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
            val lavaPlayerAudioEventListener = LavaPlayerAudioEventListenerProvider.getOrCreateInstance(guildId)
            audioPlayer.addListener(lavaPlayerAudioEventListener)
            // TODO: Create Kotlin implementation of AudioSendHandler
            GuildService.getGuildById(guildId).setAudioSendHandler(AudioPlayerSendHandler(audioPlayer))
            GuildAudioPlayer(
                audioPlayer = audioPlayer,
            )
        }
    }
}
