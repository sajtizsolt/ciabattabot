package bot.service.audio

import bot.service.eventHandler.LavaPlayerAudioLoadResultHandler

object GuildAudioPlayerService {

    fun addSongToQueue(guildId: Long, url: String) {
        val guildAudioPlayer = GuildAudioPlayerProvider.getOrCreateInstance(guildId)
        val audioPlayerManager = AudioPlayerManagerProvider.getOrCreateInstance()
        audioPlayerManager.loadItemOrdered(
            guildAudioPlayer,
            "$url&c=TVHTML5&cver=7.20190319",
            LavaPlayerAudioLoadResultHandler(guildAudioPlayer),
        )
        audioPlayerManager.loadItemOrdered(
            guildAudioPlayer,
            url,
            LavaPlayerAudioLoadResultHandler(guildAudioPlayer),
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
}
