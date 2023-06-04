package bot.service.discord

import bot.provider.jda.JdaProvider

object UserService {

    fun getUserById(id: Long) =
        JdaProvider.getOrCreateInstance().getUserById(id)
            ?: throw IllegalArgumentException("User with id $id not found")
}
