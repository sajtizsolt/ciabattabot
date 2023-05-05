package bot.extension

import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel

internal fun VoiceChannel.openAudioConnection() =
    this.guild.audioManager.openAudioConnection(this)

internal fun VoiceChannel.closeAudioConnection() =
    this.guild.audioManager.closeAudioConnection()
