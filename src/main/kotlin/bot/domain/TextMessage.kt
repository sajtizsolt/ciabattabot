package bot.domain

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

data class TextMessage(
    val authorId: Long,
    val channelId: Long,
    val guildId: Long,
    val rawContent: String,
)
