# :baguette_bread: CiabattaBot

CiabattaBot is a simple open-source Discord bot made in Java using [Java Discord API (JDA)](https://github.com/DV8FromTheWorld/JDA) and [LavaPlayer](https://github.com/sedmelluq/lavaplayer).
The bot provides various features that range from server moderation to playing music.

## Used technologies

- Gradle
- Java 17
- Kotlin
- Docker

## How to run

CiabattaBot is designed to get its configuration from environment variables.
As you might want to run the bot using Docker, the ideal way to provide configuration is to set some variables in your `gradle.properties` file.
The following table shows all available configuration keys.

| `gradle.properties` key | Environment variable | Required?          | Description |
|-------------------------|----------------------|--------------------|-------------|
| `ciabattaBotDiscordBotToken` | `DISCORD_BOT_TOKEN` | :heavy_check_mark: | The bot token of your Discord bot. |

You can find your `gradle.properties` file in the following locations, depending on your operating system:
- Windows: `%USERPROFILE%\.gradle\gradle.properties`
- Linux: `~/.gradle/gradle.properties`
- macOS: `~/.gradle/gradle.properties`

After you have set the required configuration keys, you can run build and run the Docker image using the following command:

```shell
./gradlew dockerComposeUp
```
