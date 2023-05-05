package bot.command.audio

import bot.command.Command
import bot.domain.TextMessage
import bot.service.command.CommandExecutorService

class JoinCommand : Command() {

    override val aliases = setOf(
        "join",
        "j",
    )

    override fun execute(textMessage: TextMessage) {
        CommandExecutorService.joinVoiceChannel(
            guildId = textMessage.guildId,
            userId = textMessage.authorId,
        )
    }
}
