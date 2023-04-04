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
    fun `when discord bot token is set, then its value is returned by the getter`() {
        // Given
        val returnValue = "discord-bot-token-value"
        mockkObject(ConfigurationService)
        every { ConfigurationService.getEnvironmentVariable(ConfigurationService.DISCORD_BOT_TOKEN_VARIABLE_NAME) } returns returnValue

        // When
        val discordBotToken = ConfigurationService.getDiscordBotToken()

        // Then
        assertEquals(returnValue, discordBotToken)
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
