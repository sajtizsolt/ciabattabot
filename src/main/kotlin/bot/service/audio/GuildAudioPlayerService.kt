package bot.service.audio

import bot.provider.audio.AudioPlayerManagerProvider
import bot.provider.audio.GuildAudioPlayerProvider
import bot.provider.audio.LavaPlayerAudioLoadResultHandlerProvider
import bot.service.AudioQueueService

object GuildAudioPlayerService {

    fun pauseSong(guildId: Long) {
        val guildAudioPlayer = GuildAudioPlayerProvider.getOrCreateInstance(guildId)
        guildAudioPlayer.pauseSong()
    }

    fun playSong(guildId: Long) {
        val guildAudioPlayer = GuildAudioPlayerProvider.getOrCreateInstance(guildId)
        if (!guildAudioPlayer.isPlaying()) {
            val audioPlayerManager = AudioPlayerManagerProvider.getOrCreateInstance()
            audioPlayerManager.loadItemOrdered(
                guildId,
                AudioQueueService.poll(guildId), // TODO: Do we need this? &c=TVHTML5&cver=7.20190319"
                LavaPlayerAudioLoadResultHandlerProvider.getOrCreateInstance(guildId),
            )
        }
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
