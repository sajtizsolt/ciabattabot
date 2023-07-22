package bot.service

import java.util.Queue
import java.util.concurrent.ArrayBlockingQueue

object AudioQueueService {

    private val queues = hashMapOf<Long, Queue<String>>()

    private fun getOrCreateInstance(guildId: Long): Queue<String> = queues.getOrPut(guildId) { ArrayBlockingQueue(1) }

    fun offer(guildId: Long, url: String): Boolean =
        getOrCreateInstance(guildId).offer(url)

    fun poll(guildId: Long): String? =
        getOrCreateInstance(guildId).poll()
}
