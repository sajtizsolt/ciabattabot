package bot.commandmanagement.imagecommands;

import bot.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageManager.class);

    private final String imagesFolder = "data/images/";
    private final String commandsFile = "data/pictureCommands.txt";

    public static final String addCommand = "addpic";
    public static final String listCommand = "piccommands";
    private final Map<String, String> images = new HashMap<>(); //filename - format

    public ImageManager() {
        try {
            initCommands();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkImage(String name, MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        Message message = event.getMessage();
        if (!name.startsWith(Constants.PREFIX)) {
            channel.sendMessage("The image command has to start with a " + Constants.PREFIX + " " + Constants.PENSIVE_CHAIN).queue();

        } else if (message.getAttachments().isEmpty()) {
            channel.sendMessage("I need an image too " + Constants.PENSIVE_CHAIN).queue();

        } else if (!message.getAttachments().get(0).isImage()) {
            channel.sendMessage("This isn't an image " + Constants.PENSIVE).queue();

        } else if (name.contains("\n")) {
            channel.sendMessage("You can't have a newline character in the name " + Constants.PENSIVE).queue();
        } else if (images.containsKey(name.substring(1))) {
            channel.sendMessage("This image command already exists " + Constants.PENSIVE).queue();
        } else {
            addImage(message, name);
        }
    }

    private void addImage(Message message, String name) {
        MessageChannel channel = message.getChannel();

        Message.Attachment attachment = message.getAttachments().get(0);
        String fileName = attachment.getFileName();
        String format = fileName.substring(fileName.lastIndexOf('.'));

        File dir = new File(imagesFolder);

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                LOGGER.error("Couldn't create directory");
                channel.sendMessage("Internal error, check logs for information").queue();
            }
        }

        String newName = name.substring(1);
        String newFileName = newName + format;
        String finalString = imagesFolder + newFileName;

        File file = new File(finalString);

        attachment.downloadToFile(file);

        channel.sendMessage("The image has been uploaded!").queue();

        images.put(newName, format);

        saveCommandsToFile();
    }

    void saveCommandsToFile() {
        try {
            FileWriter fw = new FileWriter(commandsFile);

            for (var str : images.entrySet()) {
                fw.write(str.getKey() + str.getValue() + System.lineSeparator());
            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Couldn't write to file");
        }
    }

    public void checkAndSend(String command, MessageChannel channel) {

        if (images.containsKey(command)) {

            String imageInFolder = imagesFolder + command + images.get(command);

            try {
                File f = new File(imageInFolder);
                // TODO: channel.sendFile(f).queue();
            } catch (Exception e) {
                e.printStackTrace();
                channel.sendMessage("Internal error, check logs for information").queue();
            }
        }
    }

    private void initCommands() throws IOException {
        Path path = Paths.get(commandsFile);
        List<String> imagesList;
        if (Files.exists(path)) {
            imagesList = Files.readAllLines(path);

            for (String image : imagesList) {
                images.put(image.substring(0, image.lastIndexOf('.')), image.substring(image.lastIndexOf('.')));
            }
        }
    }

    public void help(MessageChannel channel) {

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Image commands:");
        eb.setColor(Color.CYAN);

        List<String> sortedCommands = new ArrayList<>(images.keySet());
        Collections.sort(sortedCommands);
        sortedCommands.add(0, "addpic\n");

        int size = sortedCommands.size();
        StringBuilder leftList = new StringBuilder();
        StringBuilder rightList = new StringBuilder();

        for (int i = 0; i < sortedCommands.size(); i++) {
            if (i < size / 2)
                leftList.append("- ").append(sortedCommands.get(i)).append("\n");
            else
                rightList.append("- ").append(sortedCommands.get(i)).append("\n");
        }

        eb.addField("", leftList.toString(), true);
        eb.addField("", rightList.toString(), true);

        channel.sendMessageEmbeds(eb.build()).queue();
    }
}
