package bot.actualcommands.textcommands;

import bot.commandmanagement.ICommand;
import bot.utils.Utils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCommand implements ICommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipCommand.class);


    private String path;
    private final String htmlFileName = "messages.html";
    private final String zippedFolder = "data/zipped/";

    @Override
    public String command() {
        return "zip";
    }

    @Override
    public String help() {
        return "Usage: As an answer to a message: `!zip <number of messages (max 100)>` OR `!zip <ID of last message TO BE included>`\n" +
                "In other words: `]Replied message, given message id]`\n" +
                "Idk what happens if you write a number that's bigger than the number of messages that are after it so don't do that please lmao";
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        if (event.getMessage().getReferencedMessage() == null) {
            event.getChannel().sendMessage("Need a referenced message").queue();
            return;
        }

        path = zippedFolder + RandomStringUtils.randomAlphanumeric(10);
        File dir = new File(path);
        if (!dir.exists())
            if (!dir.mkdirs())
                throw new NotImplementedException();

        long messageCode = Long.parseLong(args[1]);
        if (messageCode > 100 && messageCode < 100000000000000L) {
            event.getChannel().sendMessage("Only 100 messages can be requested by giving a number. If you need more, write a message ID").queue();
        } else {
            List<Message> retrievedMessages = getMessages(event.getMessage().getReferencedMessage(), messageCode);
            convertMessagesToHTML(retrievedMessages);
            LOGGER.info("Zipped messages with name " + path.substring(7));
            // TODO: event.getChannel().sendFile(new File(compressDirectoryToZipFile(path))).queue();
        }
    }

    private String compressDirectoryToZipFile(String sourceDir) {
        String zipsFolderName = zippedFolder + "zips/";
        File zipsFolder = new File(zipsFolderName);
        String outputFileName = zipsFolderName + path.substring(zippedFolder.length()) + ".zip";

        if (!zipsFolder.exists())
            if (!zipsFolder.mkdir())
                throw new NotImplementedException();

        System.out.println("source dir: " + sourceDir);

        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFileName))) {
            for (File file : new File(sourceDir).listFiles()) {

                compressDir(out, file, "");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputFileName;
    }

    private void compressDir(ZipOutputStream out, File fileToZip, String parentDir) throws IOException {
        if (fileToZip.isDirectory()) {
            out.putNextEntry(new ZipEntry(fileToZip.getName() + "/"));
            for (var f : fileToZip.listFiles()) {
                compressDir(out, f, fileToZip.getName());
            }
        } else {
            System.out.println("file: " + fileToZip.getName() + " \\ path : " + fileToZip.getAbsolutePath());
            if (!parentDir.isEmpty()) parentDir += "/";
            ZipEntry entry = new ZipEntry(parentDir + fileToZip.getName());
            out.putNextEntry(entry);

            FileInputStream in = new FileInputStream(fileToZip.getAbsolutePath());
            IOUtils.copy(in, out);
            in.close();
        }
    }

    private List<Message> getMessages(Message referencedMessage, Long messageCode) {
        List<Message> messagesRet;
        List<Message> messagesTemp = new ArrayList<>();
        MessageHistory mh = referencedMessage.getChannel().getHistory();
        if (messageCode <= 100) {
            messagesTemp = new ArrayList<>(mh.getChannel().getHistoryAfter(referencedMessage, Math.toIntExact(messageCode)).complete().getRetrievedHistory());
            Collections.reverse(messagesTemp);
        } else {
            LOGGER.info("Looking for message with id {}", messageCode);
            boolean foundMessage = false;
            var lastMessage = referencedMessage;
            while (!foundMessage) {
                LOGGER.info("Still looking for message with id {}...", messageCode);
                List<Message> retrievedList = new ArrayList<>(mh.getChannel().getHistoryAfter(lastMessage, 100).complete().getRetrievedHistory());
                Collections.reverse(retrievedList);
                for (var msg : retrievedList) {
                    messagesTemp.add(msg);
                    if (msg.getIdLong() == messageCode) {
                        LOGGER.info("Found message!");
                        foundMessage = true;
                        break;
                    }
                }
                lastMessage = retrievedList.get(0);
            }
        }
        messagesRet = messagesTemp;
        return messagesRet;
    }

    private void convertMessagesToHTML(List<Message> messages) {
        StringBuilder HTMLString = new StringBuilder(Utils.getPropertiesFromResourceFile("ZipCommandHTML.properties").getProperty("Start"));
        for (Message message : messages) {
            HTMLString.append(messageToHTML(message));
        }
        HTMLString.append(Utils.getPropertiesFromResourceFile("ZipCommandHTML.properties").getProperty("End"));

        saveHTMLToFile(HTMLString.toString());
    }

    private void saveHTMLToFile(String html) {
        try (FileWriter fw = new FileWriter(path + "/" + htmlFileName)) {
            fw.write(html);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("couldnt write to file :/");
        }
    }

    private String saveImage(Message.Attachment attachment, String nameToSaveAs) {
        File dir = new File(path + "/images");
        if (!dir.exists())
            if (!dir.mkdir())
                throw new NotImplementedException();

        String imgName = nameToSaveAs + "." + attachment.getFileExtension();
        File img = new File(path + "/images/" + imgName);
        try {
            attachment.downloadToFile(img).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return imgName;
    }

    private String messageToHTML(Message message) {
        String ret = "<b>" + message.getAuthor().getAsTag() + "</b> <i>@ " + message.getTimeCreated().format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm:ss")) + "</i>: " + message.getContentRaw();
        var attachments = message.getAttachments();
        if (!attachments.isEmpty()) {
            if (attachments.get(0).isImage()) {
                String imgName = saveImage(attachments.get(0), RandomStringUtils.randomAlphanumeric(10));
                ret += " <button id=\"" + imgName.substring(0, imgName.lastIndexOf(".")) + "\" type=\"button\" onclick=\"showOrHideImage(this.id)\">Show Image</button> <div class=\"collapsible\"> <img src=\"images\\" + imgName + "\"> </div>";
            }
        }
        ret += "<br>";
        return ret;
    }
}
