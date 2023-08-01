package bot.provider.audio.lavaPlayer

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer

object AudioPlayerProvider {

    private val audioPlayers = hashMapOf<Long, AudioPlayer>()

    fun getOrCreateInstance(guildId: Long): AudioPlayer {
        return audioPlayers.getOrPut(guildId) {
            AudioPlayerManagerProvider.getOrCreateInstance().createPlayer()
        }
    }
}
