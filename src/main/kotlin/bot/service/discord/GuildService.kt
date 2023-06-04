package bot.service.discord

import bot.provider.jda.JdaProvider

object GuildService {

    fun getGuildById(id: Long) =
        JdaProvider.getOrCreateInstance().getGuildById(id)
            ?: throw IllegalArgumentException("Guild with id $id not found")
}
