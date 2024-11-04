package bot.service.eventHandler

import bot.constant.Constants
import bot.mapper.toTextMessage
import bot.service.command.CommandManagerService
import bot.service.configuration.ConfigurationService
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.slf4j.LoggerFactory

object DiscordEventHandlerService {

    private val LOGGER = LoggerFactory.getLogger(DiscordEventHandlerService::class.java)

    fun handleMessageReceivedEvent(event: MessageReceivedEvent) {
        LOGGER.debug("Message received from {}: {}", event.author.idLong, event.message.contentRaw)
        if (event.author.isBot) {
            LOGGER.warn("Ignoring message from bot with id: {}", event.author.name)
            return
        }
        val rawMessageContent = event.message.contentRaw
        if (Constants.ZERO_WIDTH_SPACES.any { rawMessageContent.startsWith(it) }) {
            LOGGER.warn("Ignoring message which starts width a zero width space character")
            return
        }
        if (rawMessageContent.startsWith(ConfigurationService.getDiscordBotCommandPrefix())) {
            CommandManagerService.handleCommand(event.toTextMessage())
        }
    }
}
