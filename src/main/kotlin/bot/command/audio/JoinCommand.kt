package bot.command.audio

import bot.command.Command
import bot.domain.TextMessage

class JoinCommand : Command() {

    override val aliases = setOf(
        "join",
        "j",
    )

    override fun execute(textMessage: TextMessage) {
        TODO()
    }
}
