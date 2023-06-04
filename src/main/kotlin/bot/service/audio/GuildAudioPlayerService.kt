package bot.service.audio

import bot.provider.audio.AudioPlayerManagerProvider
import bot.provider.audio.GuildAudioPlayerProvider
import bot.service.eventHandler.LavaPlayerAudioLoadResultHandler

object GuildAudioPlayerService {

    fun addSongToQueue(guildId: Long, url: String) {
        val guildAudioPlayer = GuildAudioPlayerProvider.getOrCreateInstance(guildId)
        val audioPlayerManager = AudioPlayerManagerProvider.getOrCreateInstance()
        audioPlayerManager.loadItemOrdered(
            guildAudioPlayer,
            url, // TODO: Do we need this? &c=TVHTML5&cver=7.20190319"
            LavaPlayerAudioLoadResultHandler(guildAudioPlayer), // TODO: Don't create new handler every time
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
        val audioPlayerManager = AudioPlayerManagerProvider.getOrCreateInstance()
        val guildAudioPlayer = GuildAudioPlayerProvider.getOrCreateInstance(guildId)
        guildAudioPlayer.stopSong()
    }
}
