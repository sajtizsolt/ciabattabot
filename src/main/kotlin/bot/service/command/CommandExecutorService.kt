package bot.service.command

import bot.extension.closeAudioConnection
import bot.extension.openAudioConnection
import bot.service.audio.AudioTrackQueue
import bot.service.audio.GuildAudioPlayerService
import bot.service.discord.ChannelService
import org.slf4j.LoggerFactory

object CommandExecutorService {

    private val LOGGER = LoggerFactory.getLogger(CommandExecutorService::class.java)

    fun joinVoiceChannel(guildId: Long, userId: Long) {
        val voiceChannel = ChannelService.getVoiceChannelByGuildAndActiveUser(
            guildId = guildId,
            userId = userId,
        )
        LOGGER.info("Joining voice channel with ID {}", voiceChannel.id)
        voiceChannel.openAudioConnection()
    }

    fun leaveVoiceChannel(guildId: Long, userId: Long) {
        val voiceChannel = ChannelService.getVoiceChannelByGuildAndActiveUser(
            guildId = guildId,
            userId = userId,
        )
        LOGGER.info("Leaving voice channel with ID {}", voiceChannel.id)
        voiceChannel.closeAudioConnection()
    }

    fun pauseAudioTrack(guildId: Long) {
        LOGGER.info("Pausing audio track for guild with ID {}", guildId)
        GuildAudioPlayerService.pauseAudioTrack(
            guildId = guildId,
        )
    }

    fun playAudioTrack(
        guildId: Long,
        vararg audioTrackUri: String,
    ) {
        val urlOrSearchString = audioTrackUri.joinToString(" ")
        LOGGER.info("Playing audio track {} for guild with ID {}", urlOrSearchString, guildId)
        AudioTrackQueue.offer(
            guildId = guildId,
            url = urlOrSearchString,
        )
        GuildAudioPlayerService.playNextAudioTrack(
            guildId = guildId,
        )
    }

    fun resumeAudioTrack(guildId: Long) {
        LOGGER.info("Resuming audio track for guild with ID {}", guildId)
        GuildAudioPlayerService.resumeAudioTrack(
            guildId = guildId,
        )
    }

    fun skipAudioTrack(guildId: Long) {
        LOGGER.info("Skipping audio track for guild with ID {}", guildId)
        GuildAudioPlayerService.skipAudioTrack(
            guildId = guildId,
        )
    }
}
