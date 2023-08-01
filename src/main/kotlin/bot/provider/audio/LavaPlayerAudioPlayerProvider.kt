package bot.provider.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer

object LavaPlayerAudioPlayerProvider {

    private val audioPlayers = hashMapOf<Long, AudioPlayer>()

    fun getOrCreateInstance(guildId: Long): AudioPlayer {
        return audioPlayers.getOrPut(guildId) {
            AudioPlayerManagerProvider.getOrCreateInstance().createPlayer()
        }
    }
}
