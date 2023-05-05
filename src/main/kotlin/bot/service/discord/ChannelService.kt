package bot.service.discord

import bot.service.jda.JdaProviderService

object ChannelService {

    fun getVoiceChannelById(id: Long) =
        JdaProviderService.getOrCreateInstance().getVoiceChannelById(id)
            ?: throw IllegalArgumentException("Voice channel with id $id not found")

    fun getVoiceChannelByGuildAndActiveUser(guildId: Long, userId: Long) =
        JdaProviderService.getOrCreateInstance().voiceChannels
            .firstOrNull { it.guild.idLong == guildId && it.members.any { member -> member.idLong == userId } }
            ?: throw IllegalArgumentException("Voice channel with active user $userId not found")
}
