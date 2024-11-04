package bot.domain

data class TextMessage(
    val guildId: Long,
    val channelId: Long,
    val authorId: Long,
    val messageId: Long,
    val rawContent: String,
)
