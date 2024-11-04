package bot.service.command

import bot.command.audio.*
import bot.domain.TextMessage
import bot.service.configuration.ConfigurationService
import org.slf4j.MDC

object CommandManagerService {

    private val commands = setOf(
        JoinCommand(),
        LeaveCommand(),
        PauseCommand(),
        PlayCommand(),
        ResumeCommand(),
        SkipCommand(),
    )

    fun handleCommand(textMessage: TextMessage) {
        MDC.put("guildId", textMessage.guildId.toString())
        MDC.put("channelId", textMessage.channelId.toString())
        MDC.put("authorId", textMessage.authorId.toString())
        MDC.put("messageId", textMessage.messageId.toString())

        val (command, _) = textMessage.rawContent.split(" ")
        val commandWithoutPrefix = command.removePrefix(ConfigurationService.getDiscordBotCommandPrefix())
        commands
            .find { it.aliases.contains(commandWithoutPrefix) }
            .let { it?.execute(textMessage) }
    }
}
