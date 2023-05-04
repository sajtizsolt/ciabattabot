package bot.service.eventHandler

import bot.service.command.CommandManagerService
import bot.constant.Constants
import bot.mapper.toTextMessage
import bot.service.configuration.ConfigurationService
import mu.KotlinLogging
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

object EventHandlerService {

    private val logger = KotlinLogging.logger {}

    fun handleMessageReceivedEvent(event: MessageReceivedEvent) {
        if (event.author.isBot) {
            logger.debug { "Ignoring message from bot: ${event.author.name}" }
            return
        }
        val rawMessageContent = event.message.contentRaw
        if (Constants.ZERO_WIDTH_SPACES.any { rawMessageContent.startsWith(it) }) {
            logger.debug { "Ignoring message which starts width a zero width space character" }
            return
        }
        if (rawMessageContent.startsWith(ConfigurationService.getDiscordBotCommandPrefix())) {
            CommandManagerService.handleCommand(event.toTextMessage())
        }
    }
}
