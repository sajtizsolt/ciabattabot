package bot.provider.audio.lavaPlayer

object AudioSendHandlerProvider {

    private val audioSendHandlers = hashMapOf<Long, net.dv8tion.jda.api.audio.AudioSendHandler>()

    fun getOrCreateInstance(guildId: Long): net.dv8tion.jda.api.audio.AudioSendHandler {
        return audioSendHandlers.getOrPut(guildId) {
            bot.service.audio.lavaPlayer.AudioSendHandler(
                audioPlayer = AudioPlayerProvider.getOrCreateInstance(guildId),
            )
        }
    }
}
