package bot.service.discord

import bot.service.jda.JdaProviderService

object GuildService {

    fun getGuildById(id: Long) =
        JdaProviderService.getOrCreateInstance().getGuildById(id)
            ?: throw IllegalArgumentException("Guild with id $id not found")
}
