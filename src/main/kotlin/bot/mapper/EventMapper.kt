package bot.mapper

import bot.domain.TextMessage
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

internal fun MessageReceivedEvent.toTextMessage() =
    TextMessage(
        messageId = message.idLong,
        authorId = author.idLong,
        channelId = channel.idLong,
        guildId = guild.idLong,
        rawContent = message.contentRaw,
    )
