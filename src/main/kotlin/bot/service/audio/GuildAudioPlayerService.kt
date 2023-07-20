package bot.service.audio

import bot.provider.audio.AudioPlayerManagerProvider
import bot.provider.audio.GuildAudioPlayerProvider
import bot.provider.audio.LavaPlayerAudioLoadResultHandlerProvider
import bot.service.eventHandler.LavaPlayerAudioLoadResultHandler

object GuildAudioPlayerService {

    fun addSongToQueue(guildId: Long, url: String) {
        val audioPlayerManager = AudioPlayerManagerProvider.getOrCreateInstance()
        audioPlayerManager.loadItemOrdered(
            guildId,
            url, // TODO: Do we need this? &c=TVHTML5&cver=7.20190319"
            LavaPlayerAudioLoadResultHandlerProvider.getOrCreateInstance(guildId),
        )
    }

    fun pauseSong(guildId: Long) {
        val guildAudioPlayer = GuildAudioPlayerProvider.getOrCreateInstance(guildId)
        guildAudioPlayer.pauseSong()
    }

    fun resumeSong(guildId: Long) {
        val guildAudioPlayer = GuildAudioPlayerProvider.getOrCreateInstance(guildId)
        guildAudioPlayer.resumeSong()
    }

    fun skipSong(guildId: Long) {
        val guildAudioPlayer = GuildAudioPlayerProvider.getOrCreateInstance(guildId)
        guildAudioPlayer.stopSong()
    }
}
