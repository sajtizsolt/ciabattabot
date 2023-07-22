package bot.service.audio

import java.util.Queue
import java.util.concurrent.ArrayBlockingQueue

object AudioTrackQueue {

    private const val DEFAULT_QUEUE_CAPACITY = 32

    private val queues = hashMapOf<Long, Queue<String>>()

    private fun getOrCreateInstance(guildId: Long): Queue<String> =
        queues.getOrPut(guildId) { ArrayBlockingQueue(DEFAULT_QUEUE_CAPACITY) }

    fun offer(guildId: Long, url: String): Boolean =
        getOrCreateInstance(guildId).offer(url)

    fun poll(guildId: Long): String? =
        getOrCreateInstance(guildId).poll()
}
