package bot.eventListener

import bot.service.eventHandler.EventHandlerService
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class MainEventListener : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        EventHandlerService.handleMessageReceivedEvent(event)
    }
}
