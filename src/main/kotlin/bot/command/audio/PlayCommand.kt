package bot.command.audio

import bot.command.Command
import bot.domain.TextMessage
import bot.service.command.CommandExecutorService

class PlayCommand : Command() {

    override val aliases = setOf(
        "play",
        "p",
    )

    override fun execute(textMessage: TextMessage) {
        CommandExecutorService.joinVoiceChannel(
            guildId = textMessage.guildId,
            userId = textMessage.authorId,
        )
        CommandExecutorService.playAudioTrack(
            guildId = textMessage.guildId,
            audioTrackUri = textMessage.rawContent.split(" ").drop(1).toTypedArray(),
        )
    }
}
