package bot.command

import bot.domain.TextMessage

abstract class Command {

    abstract val aliases: Set<String>

    abstract fun execute(textMessage: TextMessage)
}
