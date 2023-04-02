package bot.actualcommands.textcommands;

import bot.commandmanagement.ICommand;
import bot.service.Reminder;
import bot.service.Translator;
import bot.utils.Constants;
import bot.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;

import static bot.utils.Utils.getJsonPropertyValue;

public class QuizCommand implements ICommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuizCommand.class);
    private static boolean QUESTION_ACTIVE = false;
    private final String db_url = "DATABASE_URL";

    private char correctAnswer;
    private String language = "en";
    private String requestedCategory = "0";
    private GuildChannel channel;
    private QuestionResponse.Question currentQuestion;
    private Long currentlyPlayingPlayerId;
    private Reminder reminder = new Reminder();
    private final long questionTimeout = 15;
    private String questionToken = "";
    private final String streakFile = "data/streaksNEW.batta";
    private final Integer questionNumberOffset = 8;

    private static class QuizStreakData implements Serializable {
        private Map<String, Map<Long, Map<String, Integer>>> userStreakDataMap = new HashMap<>();

        public Map<String, Integer> getGoalScoreMap(String category, Long userId) {
            return userStreakDataMap.get(category).get(userId);
        }

        public Map<Long, Map<String, Integer>> getUsersStreaksForCategory(String category) {
            return userStreakDataMap.get(category);
        }
    }

    private static class Categories {
        private static class Category {
            Integer id;
            String name;

            public Category(Integer id, String name) {
                this.id = id;
                this.name = name;
            }
        }

        List<Category> trivia_categories;
    }

    private static class QuestionResponse {
        private static class Question {
            public String category;
            public String type;
            public String difficulty;
            public String question;
            public String correct_answer;
            public List<String> incorrect_answers;
        }

        public Integer response_code;

        @SerializedName("results")
        public List<Question> questions;
    }

    private QuizStreakData streakData;
    private List<Categories.Category> categories;
    private Properties properties;

    public QuizCommand() {
        categories = getCategories();
        categories.add(new Categories.Category(0, "All"));

        try {
            FileReader reader = new FileReader("src/main/resources/QuizStatisticsStrings.properties");
            properties = new Properties();
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        requestToken();
    }

    @Override
    public String command() {
        return "quiz";
    }

    @Override
    public String help() {
        return "Basic command and requesting a question: `!q/!quiz`\n" +
                "Answering the question: `!q/!quiz <result number (1-4)>`\n" +
                "Querying the categories: `!q/!quiz cat/category/categories`\n" +
                "For questions exclusively from a certain category: `!q/!quiz cat/category/categories <category number (0, 9-32)>`. For all categories: `0`\n" +
                "Setting the question language: `!q/!quiz lang/language <language ISO code (more info: !help translate)>`\n" +
                "Statistics for certain categories: `!q/!quiz stats <category (optional)>` If no category is given, statistics for the category of the last queried question will be shown";
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        if (streakData == null) {
            createStreaks(event.getGuild());
        }

        Runnable timeoutNoAnswer = () -> {
            reminder.setRemindTime(LocalDateTime.now().plusSeconds(questionTimeout));
            reminder.remind(() -> {
                currentlyPlayingPlayerId = 0L;
                event.getChannel().sendMessage("It seems that" + event.getAuthor().getAsMention() + " hasn't answered their question for a while now.. The question is now open for everyone!").queue();
            }, true);
        };

        channel = event.getChannel().asTextChannel();
        if (args.length == 1 && !QUESTION_ACTIVE) {
            for (var t : reminder.getTasksWithPrescheduledJobs().entrySet())
                if (t.getValue()) // if deletable
                    t.getKey().cancel(false);

            sendQuestion();
            currentlyPlayingPlayerId = event.getAuthor().getIdLong();
            timeoutNoAnswer.run();
        } else if (QUESTION_ACTIVE && args.length > 1 && args[1].length() == 1 && (event.getAuthor().getIdLong() == currentlyPlayingPlayerId || currentlyPlayingPlayerId == 0L)) {
            for (var t : reminder.getTasksWithPrescheduledJobs().entrySet())
                if (t.getValue()) // if deletable
                    t.getKey().cancel(false);

            currentlyPlayingPlayerId = event.getAuthor().getIdLong();
            if (checkGuess(args[1].charAt(0))) {
                sendQuestion();
                timeoutNoAnswer.run();
            }
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("categories") || args[1].equalsIgnoreCase("category") || args[1].equalsIgnoreCase("cat"))
                listCategories();
            else if (args[1].equalsIgnoreCase("stats"))
                listStats(requestedCategory, event.getAuthor().getIdLong());
        } else if (args.length >= 3) {
            if (args[1].equalsIgnoreCase("lang") || args[1].equalsIgnoreCase("language"))
                changeLanguage(args[2]);
            else if (args[1].equalsIgnoreCase("categories") || args[1].equalsIgnoreCase("category") || args[1].equalsIgnoreCase("cat"))
                changeCategory(args[2]);
            else if (args[1].equalsIgnoreCase("stats"))
                listStats(args[2], event.getAuthor().getIdLong());
        }
    }

    @Override
    public List<String> getAliases() {
        return List.of("q");
    }

    private void sendQuestion() {
        QUESTION_ACTIVE = true;
        currentQuestion = requestQuestion();

        var answers = new ArrayList<>(currentQuestion.incorrect_answers);
        answers.add(currentQuestion.correct_answer);
        if (answers.size() > 2) {
            Collections.shuffle(answers);
        } else {
            if (!answers.get(0).equals("True"))
                Collections.swap(answers, 0, 1);
        }
        correctAnswer = Character.forDigit(((answers.indexOf(currentQuestion.correct_answer) + 1)), 10);
        //System.out.println("The correct answer is: " + correctAnswer);
        var embed = Utils.createBasicReactionEmbed(currentQuestion.question, new ArrayList<>(answers), language);
        embed.setDescription("Category: *" + currentQuestion.category + "*");
        embed.setColor(currentQuestion.difficulty.equals("easy") ? Color.GREEN : currentQuestion.difficulty.equals("medium") ? Color.ORANGE : Color.RED);
        ((TextChannel) channel).sendMessageEmbeds(embed.build()).queue();
    }

    private boolean checkGuess(char guess) {
        boolean isCorrect = guess == correctAnswer;
        var currentStreak = modifyAppropriateStatistics(isCorrect);
        if (isCorrect) {
            String rightMessage = "You are right! " + Constants.SMILING_FACE_WITH_3_HEARTS + "\n";
            if (currentStreak < 5)
                rightMessage += "Your current streak is now " + currentStreak + ", well done!";
            else
                rightMessage += "Holy sh#t, you are actually insane! Your current streak is " + currentStreak + " " + Constants.SMILING_FACE_WITH_3_HEARTS;
            ((TextChannel) channel).sendMessage(rightMessage).queue();
        } else {
            Runnable timeoutResetCategory = () -> {
                reminder.setRemindTime(LocalDateTime.now().plusSeconds(120));
                reminder.remind(() -> {
                    if (!requestedCategory.equals("0")) {
                        requestedCategory = "0";
                        ((TextChannel) channel).sendMessage("Question category has been reset to category `" + requestedCategory + " : " + getCategoryNameById(requestedCategory) + "`").queue();
                    }
                }, true);
            };
            ((TextChannel) channel).sendMessage("Incorrect! The correct answer was " + correctAnswer + " " + Constants.PENSIVE).queue();
            timeoutResetCategory.run();
            serializeStreaks();
        }
        QUESTION_ACTIVE = false;

        return isCorrect;
    }

    private void listCategories() {
        StringBuilder cats = new StringBuilder("```The current category is: " + requestedCategory + " : " + getCategoryNameById(requestedCategory) + "\n\n0 : All\n");

        for (var cat : getCategories()) {
            cats.append(cat.id).append(" : ").append(cat.name).append("\n");
        }
        cats.append("```");
        ((TextChannel) channel).sendMessage(cats.toString()).queue();
    }

    private void listStats(String categoryId, Long userId) {
        if (!isValidCategory(categoryId)) {
            ((TextChannel) channel).sendMessage("No such category!").queue();
            return;
        }
        var streaks = streakData.userStreakDataMap.get(getCategoryNameById(categoryId)).get(userId);//streakData.getGoalScoreMap(category, userId);
        StringBuilder msg = new StringBuilder("```");
        for (var s : streaks.entrySet()) {
            msg.append(s.getKey()).append(" : ").append(s.getValue()).append("\n");
        }
        msg.append("```");
        ((TextChannel) channel).sendMessage(msg.toString()).queue();
    }

    private void changeLanguage(String newLanguage) {
        if (!Translator.isValidLanguage(newLanguage)) {
            ((TextChannel) channel).sendMessage("\"" + newLanguage + "\" is not a valid language! See valid languages here: https://cloud.google.com/translate/docs/languages").queue();
        } else {
            language = newLanguage;
            ((TextChannel) channel).sendMessage("Language has been changed! New language: " + language).queue();
        }
    }

    private void changeCategory(String categoryId) {

        if (isValidCategory(categoryId)) {
            requestedCategory = categoryId;
            ((TextChannel) channel).sendMessage("Category has been set to category `" + categoryId + " : " + getCategoryNameById(categoryId) + "`").queue();
        } else {
            ((TextChannel) channel).sendMessage("The given category is invalid. See possible categories with `!q cat`").queue();
        }
    }

    private boolean isValidCategory(String category) {
        int nr = Integer.parseInt(category);
        return (nr > 8 && nr < 33) || nr == 0;
    }

    private List<Categories.Category> getCategories() {
        String receivedContent = Utils.getJsonFromAPI("https://opentdb.com/api_category.php");

        Gson g = new Gson();
        var categoriesResponse = g.fromJson(receivedContent, Categories.class);
        return categoriesResponse.trivia_categories;
    }

    private QuestionResponse.Question requestQuestion() {
        String url = "https://opentdb.com/api.php?amount=1";
        if (!requestedCategory.equals("0"))
            url += "&category=" + requestedCategory;

        url += "&token=" + questionToken;

        String receivedContent = Utils.getJsonFromAPI(url);

        Integer responseCode = Integer.parseInt(getJsonPropertyValue(receivedContent, "response_code"));

        if (responseCode > 0) {
            requestToken();
            url = url.substring(0, url.lastIndexOf("=") + 1) + questionToken;
            receivedContent = Utils.getJsonFromAPI(url);
        }

        Gson g = new Gson();
        QuestionResponse qr = g.fromJson(receivedContent, QuestionResponse.class);

        return qr.questions.get(0);
    }

    private String getCategoryNameById(String id) {
        for (var c : categories) {
            if (c.id == Integer.parseInt(id))
                return c.name;
        }
        return "now that's a bug";
    }

    private Integer modifyAppropriateStatistics(boolean isCorrect) {
        BiConsumer<String, Integer> setPoints = (catStr, point) -> {
            if (!streakData.userStreakDataMap.get(getCategoryNameById(requestedCategory)).containsKey(currentlyPlayingPlayerId)) {

            }

            streakData.userStreakDataMap.get(getCategoryNameById(requestedCategory)).get(currentlyPlayingPlayerId).put(catStr, point);
        };

        var point = getPoints(property("total"));
        setPoints.accept(property("total"), ++point);

        switch (currentQuestion.type) {
            case "multiple":
                point = getPoints(property("totalNonTrueFalse"));
                setPoints.accept(property("totalNonTrueFalse"), ++point);
                break;
            case "boolean":
                point = getPoints(property("totalTrueFalse"));
                setPoints.accept(property("totalTrueFalse"), ++point);
                break;
        }

        if (isCorrect) {
            point = getPoints(property("current"));
            setPoints.accept(property("current"), ++point);
            point = getPoints(property("totalCorrect"));
            setPoints.accept(property("totalCorrect"), ++point);

            switch (currentQuestion.category) {
                case "easy":
                    point = getPoints(property("correctEasy"));
                    setPoints.accept(property("correctEasy"), ++point);
                    break;
                case "medium":
                    point = getPoints(property("correctMedium"));
                    setPoints.accept(property("correctMedium"), ++point);
                    break;
                case "hard":
                    point = getPoints(property("correctHard"));
                    setPoints.accept(property("correctHard"), ++point);
                    break;
            }

            switch (currentQuestion.type) {
                case "multiple":
                    point = getPoints(property("correctNonTrueFalse"));
                    setPoints.accept(property("correctNonTrueFalse"), ++point);
                    break;
                case "boolean":
                    point = getPoints(property("correctTrueFalse"));
                    setPoints.accept(property("correctTrueFalse"), ++point);
                    break;
            }
        } else {
            point = getPoints(property("current"));
            if (point > getPoints(property("longest"))) {
                setPoints.accept(property("longest"), point);
                //updateScoreInRemoteDatabase();
            }
            setPoints.accept(property("current"), 0);
        }
        //temporary, returns current streak
        return getPoints(property("current"));
    }

    private String property(String statStr) {
        return properties.getProperty(statStr);
    }

    private void updateScoreInRemoteDatabase() {
        Integer currentStreak = getPoints(property("current"));
        Long serverId = channel.getGuild().getIdLong();
        Long userId = currentlyPlayingPlayerId;
        String category = requestedCategory.equals("0") ? "0" : Integer.toString(Integer.parseInt(requestedCategory) - questionNumberOffset);

        String updateUrl = db_url + serverId + "/" + userId + "/update";
        String jsonToSend = "{\"serverID\": " + serverId + ", \"userID\": " + userId + ", \"Points\": {\"" + category + "\": " + currentStreak + "}}";
        int status = Utils.sendHttpRequest(updateUrl, "POST", jsonToSend);
        LOGGER.info("Update request sent, received response code " + status);
    }

    private void serializeStreaks() {
        if (streakData != null) {
            try {
                var file = new FileOutputStream(streakFile);
                var out = new ObjectOutputStream(file);

                out.writeObject(streakData);

                out.close();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private QuizStreakData deserializeStreaks(File file) {
        QuizStreakData data = null;
        try {
            var fileIn = new FileInputStream(file);
            var in = new ObjectInputStream(fileIn);

            data = (QuizStreakData) in.readObject();
            LOGGER.info("Deserialized streaks!");

            in.close();
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private void createStreaks(Guild guild) {
        var file = new File(streakFile);
        if (!file.exists()) {
            streakData = new QuizStreakData();
            for (var c : categories) {
                var userGoalMap = new HashMap<Long, Map<String, Integer>>();
                for (var u : guild.getMembers()) {
                    var goalScoreMap = new HashMap<String, Integer>();
                    goalScoreMap.put(property("longest"), 0);
                    goalScoreMap.put(property("current"), 0);
                    goalScoreMap.put(property("totalCorrect"), 0);
                    goalScoreMap.put(property("total"), 0);
                    goalScoreMap.put(property("correctEasy"), 0);
                    goalScoreMap.put(property("correctMedium"), 0);
                    goalScoreMap.put(property("correctHard"), 0);
                    goalScoreMap.put(property("totalTrueFalse"), 0);
                    goalScoreMap.put(property("totalNonTrueFalse"), 0);
                    goalScoreMap.put(property("correctTrueFalse"), 0);
                    goalScoreMap.put(property("correctNonTrueFalse"), 0);
                    long userId = u.getIdLong();
                    userGoalMap.put(userId, goalScoreMap);
                }
                streakData.userStreakDataMap.put(c.name, userGoalMap);
            }
        } else {
            streakData = deserializeStreaks(file);
        }
    }

    private Integer getPoints(String catStr) {
        return streakData.userStreakDataMap.get(getCategoryNameById(requestedCategory)).get(currentlyPlayingPlayerId).get(catStr);
    }

    private void requestToken() {
        String json = Utils.getJsonFromAPI("https://opentdb.com/api_token.php?command=request");

        questionToken = getJsonPropertyValue(json, "token");
        LOGGER.info("Token is: " + questionToken);
    }
}
