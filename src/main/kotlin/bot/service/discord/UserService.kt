package bot.service.discord

import bot.service.jda.JdaProviderService

object UserService {

    fun getUserById(id: Long) =
        JdaProviderService.getOrCreateInstance().getUserById(id)
            ?: throw IllegalArgumentException("User with id $id not found")
}
