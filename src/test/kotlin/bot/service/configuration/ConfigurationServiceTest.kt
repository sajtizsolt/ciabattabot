package bot.service.configuration

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ConfigurationServiceTest {

    @Test
    fun `when every mandatory environment variable is set, then no exception is thrown on validation`() {
        // Given
        mockkObject(ConfigurationService)
        ConfigurationService.MANDATORY_VARIABLES.forEach {
            every { ConfigurationService.getEnvironmentVariable(it) } returns "non-null-value"
        }

        // When & Then
        assertDoesNotThrow { ConfigurationService.validateEnvironmentVariables() }
    }

    @Test
    fun `when mandatory environment variables are missing, then an exception is thrown on validation`() {
        // Given
        mockkObject(ConfigurationService)
        every { ConfigurationService.getEnvironmentVariable(any()) } returns null

        // When & Then
        assertThrows<IllegalStateException> { ConfigurationService.validateEnvironmentVariables() }
    }

    @Test
    fun `when discord bot command prefix is set, then its value is returned by the getter`() {
        // Given
        val expectedReturnValue = "discord-bot-command-prefix-value"
        mockkObject(ConfigurationService)
        every { ConfigurationService.getEnvironmentVariable(ConfigurationService.DISCORD_BOT_COMMAND_PREFIX_VARIABLE_NAME) } returns expectedReturnValue

        // When
        val discordBotCommandPrefix = ConfigurationService.getDiscordBotCommandPrefix()

        // Then
        assertEquals(expectedReturnValue, discordBotCommandPrefix)
    }

    @Test
    fun `when discord bot command prefix is missing, then then the default value is returned by the getter`() {
        // Given
        mockkObject(ConfigurationService)
        every { ConfigurationService.getEnvironmentVariable(ConfigurationService.DISCORD_BOT_COMMAND_PREFIX_VARIABLE_NAME) } returns null

        // When
        val discordBotCommandPrefix = ConfigurationService.getDiscordBotCommandPrefix()

        // Then
        assertEquals(ConfigurationService.DISCORD_BOT_COMMAND_PREFIX_DEFAULT_VALUE, discordBotCommandPrefix)
    }

    @Test
    fun `when discord bot token is set, then its value is returned by the getter`() {
        // Given
        val expectedReturnValue = "discord-bot-token-value"
        mockkObject(ConfigurationService)
        every { ConfigurationService.getEnvironmentVariable(ConfigurationService.DISCORD_BOT_TOKEN_VARIABLE_NAME) } returns expectedReturnValue

        // When
        val discordBotToken = ConfigurationService.getDiscordBotToken()

        // Then
        assertEquals(expectedReturnValue, discordBotToken)
    }

    @Test
    fun `when discord bot token is missing, then an exception is thrown by the getter`() {
        // Given
        mockkObject(ConfigurationService)
        every { ConfigurationService.getEnvironmentVariable(ConfigurationService.DISCORD_BOT_TOKEN_VARIABLE_NAME) } returns null

        // When & Then
        assertThrows<IllegalStateException> { ConfigurationService.getDiscordBotToken() }
    }
}
