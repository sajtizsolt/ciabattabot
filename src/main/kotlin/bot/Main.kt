package bot

import bot.service.configuration.ConfigurationService
import bot.service.jda.JdaProviderService

fun main() {
    try {
        ConfigurationService.validateEnvironmentVariables()
        JdaProviderService.getOrCreateInstance()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
