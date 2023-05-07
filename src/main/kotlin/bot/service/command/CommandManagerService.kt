package bot.service.command

import bot.command.audio.JoinCommand
import bot.command.audio.LeaveCommand
import bot.command.audio.PlayCommand
import bot.domain.TextMessage
import bot.service.configuration.ConfigurationService

object CommandManagerService {

    private val commands = setOf(
        JoinCommand(),
        LeaveCommand(),
        PlayCommand(),
    )

    fun handleCommand(textMessage: TextMessage) {
        val (command, _) = textMessage.rawContent.split(" ")
        val commandWithoutPrefix = command.removePrefix(ConfigurationService.getDiscordBotCommandPrefix())
        commands
            .find { it.aliases.contains(commandWithoutPrefix) }
            .let { it?.execute(textMessage) }
    }
}
