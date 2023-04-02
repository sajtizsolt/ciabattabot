package bot.actualcommands.textcommands;

import bot.commandmanagement.ICommand;
import bot.service.Hangman;
import bot.utils.Constants;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class HangmanCommand implements ICommand {

    Hangman hangman;
    public static boolean gameInProgress = false;

    @Override
    public String command() {
        return "hangman";
    }

    @Override
    public String help() {
        return "Usage:\n" +
                "Start: `!hangman/hm`\n" +
                "Afterwards: `!hangman/hm <your letter>`\n" +
                "Stop: `!hangman/hm stop`\n" +
                "Change the maximum error count: `!hangman/hm errorcount <your number>`\n";
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {

        MessageChannel channel = event.getChannel();

        if (!gameInProgress) {
            if (args.length > 1) {
                if (args[1].toLowerCase().equals("errorcount")) {
                    try {
                        Hangman.errorCountLimit = Integer.parseInt(args[2]);
                        channel.sendMessage("Error count changed!!").queue();
                    } catch (NumberFormatException e) {
                        channel.sendMessage("Please give a number " + Constants.PENSIVE_CHAIN).queue();
                    }
                }
            } else {
                startGame(channel);
            }
        } else {
            if (args[1].length() > 1) {
                if (args[1].toLowerCase().equals("stop")) {
                    gameInProgress = false;
                    channel.sendMessage("Game Over!").queue();
                } else if (args[1].toLowerCase().equals("restart")) {
                    try {
                        hangman.restart();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (args[1].length() == 1) {
                char guess = Character.toLowerCase(args[1].charAt(0));
                hangman.setCurrentGuess(guess);
            } else {
                startGame(channel);
            }
        }
    }

    private void startGame(MessageChannel channel) {
        try {
            if (hangman == null) {
                hangman = new Hangman(channel);
                hangman.statuszCheck();
            } else {
                hangman.restart();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getAliases() {
        return List.of("hm");
    }
}
