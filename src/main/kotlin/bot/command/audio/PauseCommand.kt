package bot.command.audio

import bot.command.Command
import bot.domain.TextMessage
import bot.service.command.CommandExecutorService

class PauseCommand : Command() {

    override val aliases = setOf(
        "pause",
    )

    override fun execute(textMessage: TextMessage) {
        CommandExecutorService.pauseAudioTrack(
            guildId = textMessage.guildId,
        )
    }
}
