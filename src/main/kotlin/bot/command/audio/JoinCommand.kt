package bot.command.audio

import bot.command.Command
import bot.domain.TextMessage
import bot.extension.openAudioConnection
import bot.service.discord.ChannelService
import bot.service.discord.GuildService
import bot.service.discord.UserService
import bot.service.jda.JdaProviderService

class JoinCommand : Command() {

    override val aliases = setOf(
        "join",
        "j",
    )

    override fun execute(textMessage: TextMessage) {
        val voiceChannel = ChannelService.getVoiceChannelByGuildAndActiveUser(
            guildId = textMessage.guildId,
            userId = textMessage.authorId,
        )
        voiceChannel.openAudioConnection()
    }
}
