package bot.service.discord

import bot.provider.jda.JdaProvider

object ChannelService {

    fun getVoiceChannelById(id: Long) =
        JdaProvider.getOrCreateInstance().getVoiceChannelById(id)
            ?: throw IllegalArgumentException("Voice channel with id $id not found")

    fun getVoiceChannelByGuildAndActiveUser(guildId: Long, userId: Long) =
        JdaProvider.getOrCreateInstance().voiceChannels
            .firstOrNull { it.guild.idLong == guildId && it.members.any { member -> member.idLong == userId } }
            ?: throw IllegalArgumentException("Voice channel with active user $userId not found")
}
