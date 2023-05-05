package bot.command.audio

import bot.command.Command
import bot.domain.TextMessage
import bot.service.command.CommandExecutorService

class LeaveCommand : Command() {

    override val aliases = setOf(
        "leave",
        "l",
    )

    override fun execute(textMessage: TextMessage) {
        CommandExecutorService.leaveVoiceChannel(
            guildId = textMessage.guildId,
            userId = textMessage.authorId,
        )
    }
}
