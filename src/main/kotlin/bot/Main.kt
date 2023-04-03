package bot

import bot.service.configuration.ConfigurationService

fun main() =
    try {
        ConfigurationService.validateEnvironmentVariables()

        App.main(emptyArray())
    } catch (e: Exception) {
        e.printStackTrace()
    }
