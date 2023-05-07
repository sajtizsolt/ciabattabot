package bot.service.audio

import bot.eventListener.LavaPlayerAudioEventListener
import bot.extension.setAudioSendHandler
import bot.lavaplayer.AudioPlayerSendHandler
import bot.service.discord.GuildService

object GuildAudioPlayerProvider {

    private val audioPlayers = hashMapOf<Long, GuildAudioPlayer>()

    fun getOrCreateInstance(guildId: Long): GuildAudioPlayer {
        val audioPlayerManager = AudioPlayerManagerProvider.getOrCreateInstance()
        return audioPlayers.getOrPut(guildId) {
            val audioPlayer = audioPlayerManager.createPlayer()
            val lavaPlayerAudioEventListener = LavaPlayerAudioEventListener()
            audioPlayer.addListener(lavaPlayerAudioEventListener) // TODO: Should we add this here?
            GuildService.getGuildById(guildId).setAudioSendHandler(AudioPlayerSendHandler(audioPlayer))
            GuildAudioPlayer(
                audioPlayer = audioPlayer,
                lavaPlayerAudioEventListener = lavaPlayerAudioEventListener,
            )
        }
    }
}