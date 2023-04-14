package bot.service.jda

import bot.service.configuration.ConfigurationService
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy

object JdaProviderService {

    private lateinit var jda: JDA

    fun getInstance(): JDA {
        if (!::jda.isInitialized) {
            jda = buildJda()
        }
        return jda
    }

    private fun buildJda(vararg listeners: EventListener): JDA {
        val jda = JDABuilder
            .create(ConfigurationService.getDiscordBotToken(), buildIntents())
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT)
            .addEventListeners(listeners)
            .build()
            .awaitReady()
        jda.presence.activity = Activity.listening("your commands")
        return jda
    }

    // TODO: Review which intents are needed
    private fun buildIntents() =
        setOf(
            GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_PRESENCES,
            GatewayIntent.GUILD_VOICE_STATES,
        )
}
