package bot.provider.jda

import bot.eventListener.DiscordEventListener
import bot.service.configuration.ConfigurationService
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag

object JdaProvider {

    private lateinit var jda: JDA

    fun getOrCreateInstance(): JDA {
        if (!JdaProvider::jda.isInitialized) {
            jda = buildJda()
        }
        return jda
    }

    private fun buildJda(): JDA {
        val jda = JDABuilder
            .create(ConfigurationService.getDiscordBotToken(), buildIntents())
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT)
            .disableCache(CacheFlag.SCHEDULED_EVENTS)
            .addEventListeners(DiscordEventListener())
            .build()
            .awaitReady()
        jda.presence.activity = Activity.listening("your commands")
        return jda
    }

    // TODO: Review which intents are really needed
    private fun buildIntents() =
        setOf(
            GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_PRESENCES,
            GatewayIntent.GUILD_VOICE_STATES,
        )
}
