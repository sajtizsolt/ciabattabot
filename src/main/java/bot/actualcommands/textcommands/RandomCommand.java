package bot.actualcommands.textcommands;

import bot.commandmanagement.ICommand;
import bot.utils.Constants;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.ThreadLocalRandom;

public class RandomCommand implements ICommand {
    @Override
    public String command() {
        return "random";
    }

    @Override
    public String help() {
        return "Usage: " + Constants.PREFIX + command() + " <number>\n"
                + "If no number is given, a number between 1 and 10 is returned.";
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {

        MessageChannel channel = event.getChannel();

        try {
            long result;
            if (args.length > 1)
                result = getRandomNumber(Long.parseLong(args[1]));
            else
                result = getRandomNumber(10);

            channel.sendMessage(Long.toString(result)).queue();
        } catch (Exception e) {
            channel.sendMessage("Internal error " + Constants.PENSIVE_CHAIN).queue();
        }

    }

    private long getRandomNumber(long max) {
        return ThreadLocalRandom.current().nextLong(max) + 1;
    }

}
