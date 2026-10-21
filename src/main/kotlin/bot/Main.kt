package bot

import bot.provider.audio.lavaPlayer.AudioPlayerManagerProvider
import bot.provider.jda.JdaProvider
import bot.service.configuration.ConfigurationService

fun main() {
    try {
        ConfigurationService.validateEnvironmentVariables()
        AudioPlayerManagerProvider.getOrCreateInstance()
        JdaProvider.getOrCreateInstance()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
