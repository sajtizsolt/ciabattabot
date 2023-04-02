package bot.actualcommands.textcommands;

import bot.commandmanagement.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PollCommand implements ICommand {

    List<String> answerList = new ArrayList<>();
    List<String> thinkingList = new ArrayList<>();
    int numberOfEmojis = 9;

    public PollCommand() {
        thinkingList.add("https://tenor.com/view/think-emoji-thinking-in-thought-rotate-gif-8083088");
        thinkingList.add("https://tenor.com/view/emoji-thinking-gif-8387963");
        thinkingList.add("https://tenor.com/view/fidget-spinner-gif-13433278");
        thinkingList.add("https://tenor.com/view/solar-hmm-emoji-hmm-emoji-sun-sun-hmm-emoji-galaxy-hmm-emoji-galaxy-hmm-gif-13772501");
        thinkingList.add("https://tenor.com/view/suspecting-thinking-emoji-gif-10489093");
        thinkingList.add("https://tenor.com/view/think-emote-emoji-thinking-gif-14404237");
        thinkingList.add("https://tenor.com/view/hmm-thinking-emoji-gif-9901919");
    }

    @Override
    public String command() {
        return "poll";
    }

    @Override
    public String help() {
        return "Usage:\n`!poll <Question>? Answer1; Answer2; ... Answern;` (n < 10)\n";
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        answerList.clear();
        var question = new StringBuilder();
        var answersStart = 0;
        //Get the question, and set where the answers start
        for (var i = 1; !args[i - 1].endsWith("?"); i++) {
            question.append(args[i]).append(" ");
            answersStart = i;
        }

        try {
            getAnswers(args, ++answersStart);
        } catch (Exception e) {
            e.printStackTrace();
        }

        event.getChannel().sendMessageEmbeds(createEmbed(question.toString()).build()).queue(message -> {
            // TODO
            // for (var i = 0; i < answerList.size() && i < numberOfEmojis; i++)
                // message.addReaction((i + 1) + "\u20E3").queue();
        });
    }

    private void getAnswers(String[] args, int answerStart) {
        if (answerStart >= args.length)
            return;

        var answer = new StringBuilder();
        var i = answerStart;
        for (; i == answerStart || (!args[i - 1].endsWith(";") && i < args.length); i++) {
            if (args[i].endsWith(";"))
                answer.append(args[i], 0, args[i].length() - 1);
            else
                answer.append(args[i]).append(" ");
        }
        answerList.add(answer.toString());
        getAnswers(args, i);
    }

    private EmbedBuilder createEmbed(String q) {
        var ret = new EmbedBuilder();

        ret.setAuthor("QUESTION");
        ret.setTitle(q);
        ret.setColor(Color.RED);
        ret.setThumbnail(thinkingList.get(new Random().nextInt(thinkingList.size())));

        //Add answers
        for (var i = 0; i < answerList.size() && i < numberOfEmojis; i++) {
            ret.addField((i + 1) + "\u20E3", answerList.get(i), true);
        }

        return ret;
    }
}
