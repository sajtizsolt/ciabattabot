package bot.service.command

import bot.extension.closeAudioConnection
import bot.extension.openAudioConnection
import bot.service.AudioTrackQueue
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

    fun pauseAudioTrack(guildId: Long) {
        GuildAudioPlayerService.pauseAudioTrack(
            guildId = guildId,
        )
    }

    fun playAudioTrack(
        guildId: Long,
        vararg audioTrackUri: String,
    ) {
        AudioTrackQueue.offer(
            guildId = guildId,
            url = audioTrackUri.joinToString(" "),
        )
        GuildAudioPlayerService.playAudioTrack(
            guildId = guildId,
        )
    }

    fun resumeAudioTrack(guildId: Long) {
        GuildAudioPlayerService.resumeAudioTrack(
            guildId = guildId,
        )
    }

    fun skipAudioTrack(guildId: Long) {
        GuildAudioPlayerService.skipAudioTrack(
            guildId = guildId,
        )
    }
}
