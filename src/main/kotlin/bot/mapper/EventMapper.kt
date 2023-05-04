package bot.mapper

import bot.domain.TextMessage
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

internal fun MessageReceivedEvent.toTextMessage() =
    TextMessage(
        authorId = author.idLong,
        channelId = channel.idLong,
        guildId = guild.idLong,
        rawContent = message.contentRaw,
        event = this,
    )
