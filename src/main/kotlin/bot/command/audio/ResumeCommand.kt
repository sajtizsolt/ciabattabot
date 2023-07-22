package bot.command.audio

import bot.command.Command
import bot.domain.TextMessage
import bot.service.command.CommandExecutorService

class ResumeCommand : Command() {

    override val aliases = setOf(
        "resume",
    )

    override fun execute(textMessage: TextMessage) {
        CommandExecutorService.resumeAudioTrack(
            guildId = textMessage.guildId,
        )
    }
}
