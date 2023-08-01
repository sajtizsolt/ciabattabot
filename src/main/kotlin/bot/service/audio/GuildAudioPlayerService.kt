package bot.service.audio

import bot.provider.audio.lavaPlayer.AudioPlayerManagerProvider
import bot.provider.audio.GuildAudioPlayerProvider
import bot.provider.audio.lavaPlayer.AudioLoadResultHandlerProvider

object GuildAudioPlayerService {

    fun pauseAudioTrack(guildId: Long) {
        val guildAudioPlayer = GuildAudioPlayerProvider.getOrCreateInstance(guildId)
        guildAudioPlayer.pauseAudioTrack()
    }

    fun playNextAudioTrack(guildId: Long) {
        val guildAudioPlayer = GuildAudioPlayerProvider.getOrCreateInstance(guildId)
        if (!guildAudioPlayer.isPlayingTrack()) {
            val audioPlayerManager = AudioPlayerManagerProvider.getOrCreateInstance()
            audioPlayerManager.loadItemOrdered(
                guildId,
                AudioTrackQueue.poll(guildId), // TODO: Do we need this? &c=TVHTML5&cver=7.20190319"
                AudioLoadResultHandlerProvider.getOrCreateInstance(guildId),
            )
        }
    }

    fun resumeAudioTrack(guildId: Long) {
        val guildAudioPlayer = GuildAudioPlayerProvider.getOrCreateInstance(guildId)
        guildAudioPlayer.resumeAudioTrack()
    }

    fun skipAudioTrack(guildId: Long) {
        val guildAudioPlayer = GuildAudioPlayerProvider.getOrCreateInstance(guildId)
        guildAudioPlayer.stopAudioTrack()
    }
}
