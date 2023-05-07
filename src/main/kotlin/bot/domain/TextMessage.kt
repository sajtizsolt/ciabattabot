package bot.domain

data class TextMessage(
    val authorId: Long,
    val channelId: Long,
    val guildId: Long,
    val rawContent: String,
)
