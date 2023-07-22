package bot.command.audio

import bot.command.Command
import bot.domain.TextMessage
import bot.service.command.CommandExecutorService

class SkipCommand : Command() {

    override val aliases = setOf(
        "skip",
    )

    override fun execute(textMessage: TextMessage) {
        CommandExecutorService.skipAudioTrack(
            guildId = textMessage.guildId,
        )
    }
}
