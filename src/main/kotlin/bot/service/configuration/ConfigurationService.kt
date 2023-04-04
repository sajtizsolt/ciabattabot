package bot.service.configuration

import org.apache.commons.lang3.StringUtils

object ConfigurationService {

    internal const val DISCORD_BOT_TOKEN_VARIABLE_NAME = "DISCORD_BOT_TOKEN"

    internal val MANDATORY_VARIABLES = setOf(
        DISCORD_BOT_TOKEN_VARIABLE_NAME,
    )

    fun validateEnvironmentVariables() {
        val missingMandatoryVariables = MANDATORY_VARIABLES.filter { StringUtils.isBlank(getEnvironmentVariable(it)) }
        if (missingMandatoryVariables.isNotEmpty()) {
            throw IllegalStateException("Missing mandatory environment variables: $missingMandatoryVariables")
        }
    }

    fun getDiscordBotToken(): String =
        getEnvironmentVariable(DISCORD_BOT_TOKEN_VARIABLE_NAME)
            ?: throw IllegalStateException("Missing environment variable: $DISCORD_BOT_TOKEN_VARIABLE_NAME")

    internal fun getEnvironmentVariable(variableName: String): String? =
        System.getenv(variableName)
}
