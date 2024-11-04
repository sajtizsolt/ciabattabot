package bot.service.configuration

import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory

object ConfigurationService {

    private val LOGGER = LoggerFactory.getLogger(ConfigurationService::class.java)

    internal const val DISCORD_BOT_COMMAND_PREFIX_VARIABLE_NAME = "CIABATTA_BOT_COMMAND_PREFIX"
    internal const val DISCORD_BOT_TOKEN_VARIABLE_NAME = "CIABATTA_BOT_DISCORD_BOT_TOKEN"
    internal const val PO_TOKEN_VARIABLE_NAME = "CIABATTA_BOT_PO_TOKEN"
    internal const val PO_VISITOR_DATA_VARIABLE_NAME = "CIABATTA_BOT_PO_VISITOR_DATA"

    internal const val DISCORD_BOT_COMMAND_PREFIX_DEFAULT_VALUE = "!"

    internal val MANDATORY_VARIABLES = setOf(
        DISCORD_BOT_TOKEN_VARIABLE_NAME,
        PO_TOKEN_VARIABLE_NAME,
        PO_VISITOR_DATA_VARIABLE_NAME,
    )

    fun validateEnvironmentVariables() {
        val missingMandatoryVariables = MANDATORY_VARIABLES.filter { StringUtils.isBlank(getEnvironmentVariable(it)) }
        if (missingMandatoryVariables.isNotEmpty()) {
            throw IllegalStateException("Missing mandatory environment variables: $missingMandatoryVariables")
        }
    }

    fun getDiscordBotCommandPrefix(): String =
        getEnvironmentVariable(DISCORD_BOT_COMMAND_PREFIX_VARIABLE_NAME) ?: DISCORD_BOT_COMMAND_PREFIX_DEFAULT_VALUE

    fun getDiscordBotToken(): String =
        getValueOrThrowException(DISCORD_BOT_TOKEN_VARIABLE_NAME)

    fun getPoToken(): String =
        getValueOrThrowException(PO_TOKEN_VARIABLE_NAME)

    fun getPoVisitorData(): String =
        getValueOrThrowException(PO_VISITOR_DATA_VARIABLE_NAME)

    internal fun getEnvironmentVariable(variableName: String): String? =
        System.getenv(variableName)

    private fun getValueOrThrowException(variableName: String): String =
        getEnvironmentVariable(variableName) ?: throwIllegalStateException(listOf(variableName))

    private fun throwIllegalStateException(missingMandatoryVariables: List<String>): String {
        LOGGER.error("Missing mandatory environment variables: $missingMandatoryVariables")
        throw IllegalStateException("Missing mandatory environment variables: $missingMandatoryVariables")
    }
}
