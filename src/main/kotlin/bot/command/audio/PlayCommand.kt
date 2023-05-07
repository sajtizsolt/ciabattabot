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
        CommandExecutorService.playSong(
            authorId = textMessage.authorId,
            channelId = textMessage.channelId,
            guildId = textMessage.guildId,
            searchString = textMessage.rawContent.split(" ").drop(1).toTypedArray(),
        )
    }
}
