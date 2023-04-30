package bot.command

import bot.commandmanagement.GeneralCommandManager
import bot.domain.TextMessage

object CommandManagerService {

    private val generalCommandManager = GeneralCommandManager()

    fun handleCommand(textMessage: TextMessage) {
        generalCommandManager.handleCommand(textMessage.event)
    }
}
