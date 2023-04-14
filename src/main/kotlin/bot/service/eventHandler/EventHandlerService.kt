package bot.service.eventHandler

import bot.commandmanagement.GeneralCommandManager
import bot.utils.Constants
import mu.KotlinLogging
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

object EventHandlerService {

    private val logger = KotlinLogging.logger {}

    private val generalCommandManager = GeneralCommandManager()

    fun handleMessageReceivedEvent(event: MessageReceivedEvent) {
        if (event.author.isBot) {
            logger.debug { "Ignoring message from bot: ${event.author.name}" }
            return
        }
        if (event.message.contentRaw.startsWith(Constants.PREFIX)) {
            generalCommandManager.handleCommand(event)
        }
    }
}
