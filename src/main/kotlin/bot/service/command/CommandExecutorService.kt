package bot.service.command

import bot.extension.closeAudioConnection
import bot.extension.openAudioConnection
import bot.service.AudioQueueService
import bot.service.audio.GuildAudioPlayerService
import bot.service.discord.ChannelService

object CommandExecutorService {

    fun joinVoiceChannel(guildId: Long, userId: Long) {
        val voiceChannel = ChannelService.getVoiceChannelByGuildAndActiveUser(
            guildId = guildId,
            userId = userId,
        )
        voiceChannel.openAudioConnection()
    }

    fun leaveVoiceChannel(guildId: Long, userId: Long) {
        val voiceChannel = ChannelService.getVoiceChannelByGuildAndActiveUser(
            guildId = guildId,
            userId = userId,
        )
        voiceChannel.closeAudioConnection()
    }

    fun pauseSong(guildId: Long) {
        GuildAudioPlayerService.pauseSong(
            guildId = guildId,
        )
    }

    fun playSong(
        authorId: Long,
        channelId: Long,
        guildId: Long,
        vararg searchString: String,
    ) {
        AudioQueueService.offer(
            guildId = guildId,
            url = searchString.joinToString(" "),
        )
        GuildAudioPlayerService.playSong(
            guildId = guildId,
        )
    }

    fun resumeSong(guildId: Long) {
        GuildAudioPlayerService.resumeSong(
            guildId = guildId,
        )
    }

    fun skipSong(guildId: Long) {
        GuildAudioPlayerService.skipSong(
            guildId = guildId,
        )
    }
}
