package bot

import bot.service.configuration.ConfigurationService
import bot.provider.jda.JdaProvider

fun main() {
    try {
        ConfigurationService.validateEnvironmentVariables()
        JdaProvider.getOrCreateInstance()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
