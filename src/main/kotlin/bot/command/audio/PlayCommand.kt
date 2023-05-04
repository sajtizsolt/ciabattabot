package bot.command.audio

import bot.command.Command
import bot.domain.TextMessage

class PlayCommand : Command() {

    override val aliases = setOf(
        "play",
        "p",
    )

    override fun execute(textMessage: TextMessage) {
        TODO()
    }
}
