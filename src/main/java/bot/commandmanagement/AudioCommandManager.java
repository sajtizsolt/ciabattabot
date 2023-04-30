package bot.commandmanagement;

import bot.actualcommands.audiocommands.LocalFileCommand;
import bot.actualcommands.audiocommands.LoopCommand;
import bot.actualcommands.audiocommands.PauseCommand;
import bot.actualcommands.audiocommands.PlayCommand;
import bot.actualcommands.audiocommands.QueueCommand;
import bot.actualcommands.audiocommands.ResumeCommand;
import bot.actualcommands.audiocommands.SkipCommand;
import bot.actualcommands.audiocommands.StopCommand;
import bot.actualcommands.audiocommands.VolumeCommand;
import bot.lavaplayer.GuildAudioManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AudioCommandManager extends CommandManager {

    private final LocalFileCommand localFileCommand;

    public AudioCommandManager(GuildAudioManager guildAudioManager) {
        localFileCommand = new LocalFileCommand(guildAudioManager);

        add(new PlayCommand(guildAudioManager));
        add(new StopCommand(guildAudioManager));
        add(new SkipCommand(guildAudioManager));
        add(new VolumeCommand(guildAudioManager));
        add(new PauseCommand(guildAudioManager));
        add(new ResumeCommand(guildAudioManager));
        add(new QueueCommand(guildAudioManager));
        add(new LoopCommand(guildAudioManager));
        //add(new RemoveCommand(guildAudioManager));
    }

    @Override
    public void handleCommand(MessageReceivedEvent event) {
        String messageContent = event.getMessage().getContentRaw();
        String[] contentArr = messageContent.split(" ");

        contentArr[0] = contentArr[0].substring(1);
        String command = contentArr[0];

        if (command.equals("audiocommands")) {
            listCommands(event.getChannel(), "Audio commands");
        } else {

            if (commands.containsKey(command))
                commands.get(command).execute(contentArr, event);
            else
                localFileCommand.execute(contentArr, event);
        }
    }

    public boolean hasCommand(String command) {
        return commands.containsKey(command) || localFileCommand.hasCommand(command) || command.equals("audiocommands");
    }
}
