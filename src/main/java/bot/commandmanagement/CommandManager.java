package bot.commandmanagement;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CommandManager {

    protected final Map<String, ICommand> commands = new HashMap<>();

    public void add(ICommand command) {
        if (!commands.containsKey(command.command()))
            commands.put(command.command(), command);
    }

    public abstract void handleCommand(MessageReceivedEvent event);

    protected void listCommands(MessageChannel channel, String title) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title + ":");
        eb.setColor(title.contains("Text") ? Color.RED : title.contains("Audio") ? new Color(81, 32, 138) : Color.GRAY);

        StringBuilder sb = eb.getDescriptionBuilder();

        List<String> sortedCommands = new ArrayList<>();

        if (title.startsWith("Text")) {
            sortedCommands.add("*audiocommands*");
            sortedCommands.add("*piccommands*\n");
        }
        for (var cmd : commands.values())
            sortedCommands.add(cmd.command());
        Collections.sort(sortedCommands);

        sortedCommands.forEach((cmd) -> sb.append("- ").append(cmd).append("\n"));

        channel.sendMessageEmbeds(eb.build()).queue();
    }

}
