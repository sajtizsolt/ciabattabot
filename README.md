# :baguette_bread: CiabattaBot

CiabattaBot is a simple open-source Discord bot running on the JVM, implemented
using [Java Discord API (JDA)](https://github.com/DV8FromTheWorld/JDA)
and [LavaPlayer (fork)](https://github.com/Walkyst/lavaplayer-fork).
The bot provides features that allow server members to play audio in voice channels.

## Used technologies

- Gradle
- Java 25
- Kotlin
- Docker

## How to run

CiabattaBot is designed to get its configuration from environment variables.
As you might want to run the bot using Docker, the ideal way to provide configuration is to set some variables in your
`gradle.properties` file.
The following table shows all available configuration keys.

| `gradle.properties` key               | Environment variable                      | Required?          | Default value | Description                                                                                                    |
|---------------------------------------|-------------------------------------------|--------------------|---------------|----------------------------------------------------------------------------------------------------------------|
| `ciabattaBotDiscordBotToken`          | `CIABATTA_BOT_DISCORD_BOT_TOKEN`          | :heavy_check_mark: | -             | The bot token of your Discord bot.                                                                             |
| `ciabattaBotCommandPrefix`            | `CIABATTA_BOT_COMMAND_PREFIX`             | :x:                | `!`           | The command prefix of your Discord bot.                                                                        |
| `ciabattaBotYoutubeOauthRefreshToken` | `CIABATTA_BOT_YOUTUBE_OAUTH_REFRESH_TOKEN`| :x:                | -             | Optional YouTube OAuth2 refresh token to authenticate YouTube playback.                                        |
| `ciabattaBotEnableYoutubeOauth`       | `CIABATTA_BOT_ENABLE_YOUTUBE_OAUTH`        | :x:                | `false`       | Set to `true` to initiate YouTube OAuth2 device authorization flow on startup.                                 |

You can find your `gradle.properties` file in the following locations, depending on your operating system:

- Windows: `%USERPROFILE%\.gradle\gradle.properties`
- Linux: `~/.gradle/gradle.properties`
- macOS: `~/.gradle/gradle.properties`

After you have set the required configuration keys, you can run build and run the Docker image using the following
command:

```shell
./gradlew dockerComposeUp
```

### YouTube OAuth2 Authentication

If YouTube blocks playback (e.g. when hosting on datacenter IPs), you can authenticate via OAuth2:

1. Set `ciabattaBotEnableYoutubeOauth=true` (or `CIABATTA_BOT_ENABLE_YOUTUBE_OAUTH=true`).
2. Start the bot and check the console logs for the device authorization prompt:
   ```
   Go to https://www.google.com/device and enter code: XXXX-XXXX
   ```
3. Open the link in a browser, enter the code, and sign in with a Google account.
4. The bot will log your refresh token. Save it to your `gradle.properties` (`ciabattaBotYoutubeOauthRefreshToken=<refresh-token>`) or environment (`CIABATTA_BOT_YOUTUBE_OAUTH_REFRESH_TOKEN`), then set `ciabattaBotEnableYoutubeOauth=false`.

## Commands

As CiabattaBot is still in development, the list of available commands is not yet complete.
Currently, the bot supports only the basic commands that are required to play music.
The following table shows all available commands.

| Command   | Description                                                                                           |
|-----------|-------------------------------------------------------------------------------------------------------|
| `!join`   | Join the voice channel of the user.                                                                   |
| `!leave`  | Leave the voice channel of the user.                                                                  |
| `!pause`  | Pause the audio track which is currently playing.                                                     |
| `!play`   | Add an audio track to the queue and start playing if it is empty.                                     |
| `!resume` | Resume the audio track which is currently playing.                                                    |
| `!skip`   | Skip the audio track which is currently playing and start playing the next if the queue is not empty. |
