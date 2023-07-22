package bot

import bot.provider.jda.JdaProvider
import bot.service.configuration.ConfigurationService

fun main() {
    try {
        ConfigurationService.validateEnvironmentVariables()
        JdaProvider.getOrCreateInstance()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
