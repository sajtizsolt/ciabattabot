package bot;

import bot.commandmanagement.GeneralCommandManager;
import bot.utils.Constants;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MyEventListener extends ListenerAdapter {

    private final GeneralCommandManager generalCommandManager = new GeneralCommandManager();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String content = event.getMessage().getContentRaw();
        if (content.startsWith(Constants.PREFIX)) generalCommandManager.handleCommand(event);

    }
}
