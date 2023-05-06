package bot.extension

import net.dv8tion.jda.api.audio.AudioSendHandler
import net.dv8tion.jda.api.entities.Guild

internal fun Guild.setAudioSendHandler(audioSendHandler: AudioSendHandler) {
    this.audioManager.sendingHandler = audioSendHandler
}
