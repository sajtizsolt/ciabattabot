package bot.actualcommands.textcommands;

import bot.commandmanagement.ICommand;
import bot.utils.Constants;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Map;

public class HelpCommand implements ICommand {

    private final Map<String, ICommand> commands;

    public HelpCommand(Map<String, ICommand> map) {
        commands = map;
    }

    @Override
    public String command() {
        return "help";
    }

    @Override
    public String help() {
        return "Tells what the command in the 2nd parameter does\nUsage: `" + Constants.PREFIX + command() + " <command>`\nList of commands: `!commands`";
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        if (args.length < 2) {
            channel.sendMessage(help()).queue();
        } else {
            String command = args[1].toLowerCase();
            if (commands.containsKey(command)) {
                channel.sendMessage(commands.get(command).help()).queue();
            }
        }
    }
}
