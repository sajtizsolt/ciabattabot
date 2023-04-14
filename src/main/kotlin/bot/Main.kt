package bot

import bot.service.configuration.ConfigurationService
import bot.service.jda.JdaProviderService

fun main(vararg arguments: String) {
    try {
        ConfigurationService.validateEnvironmentVariables()
        JdaProviderService.getInstance()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
