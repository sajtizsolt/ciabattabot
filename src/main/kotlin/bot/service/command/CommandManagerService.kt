package bot.service.command

import bot.command.Command
import bot.commandmanagement.GeneralCommandManager
import bot.domain.TextMessage

object CommandManagerService {

    private val generalCommandManager = GeneralCommandManager()

    private val commands = setOf<Command>()

    fun handleCommand(textMessage: TextMessage) {
        generalCommandManager.handleCommand(textMessage.event)
    }
}
