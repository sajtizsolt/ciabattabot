package bot.actualcommands.audiocommands;

import bot.commandmanagement.ICommand;
import bot.utils.Constants;
import bot.lavaplayer.GuildAudioManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class VolumeCommand implements ICommand {

    private final GuildAudioManager guildAudioManager;

    public VolumeCommand(GuildAudioManager guildAudioManager) {
        this.guildAudioManager = guildAudioManager;
    }

    @Override
    public String command() {
        return "volume";
    }

    @Override
    public String help() {
        return "Sets the volume of the currently playing track\nUsage: `!volume <value>`\n" +
                "The default is 100, the limits are 0 and 1000, but you don't really want to set it higher than 100 lmao";
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        if (event.getGuild().getAudioManager().isConnected() && botAndCallerAreInTheSameVoiceChannel(event.getMember())) {
            Guild guild = event.getGuild();
            try {
                if (args.length >= 2) {
                    int volume = Integer.parseInt(args[1]);
                    guildAudioManager.setVolume(guild, volume);
                    event.getChannel().sendMessage("Volume set to " + guildAudioManager.getVolume(guild)).queue();
                } else {
                    event.getChannel().sendMessage("Current volume is " + guildAudioManager.getVolume(guild)).queue();
                }
            } catch (NumberFormatException e) {
                event.getChannel().sendMessage("Can you not be stupid " + Constants.PENSIVE).queue();
            }
        } else {
            event.getChannel().sendMessage("You are not in the same voice channel as the bot " + Constants.PENSIVE).queue();
        }
    }

    //this method should be in like a separate audioutils class or sth, because it's being used in other classes as well and this is ugly as hell
    public static boolean botAndCallerAreInTheSameVoiceChannel(Member member) {
        Guild guild = member.getGuild();
        VoiceChannel voiceChannel = null;
        for (VoiceChannel vc : guild.getVoiceChannels()) {
            if (!vc.getMembers().isEmpty()) {
                if (vc.getMembers().contains(member)) {
                    voiceChannel = vc;
                }
            }
        }

        if (voiceChannel != null) {
            return guild.getAudioManager().getConnectedChannel().getIdLong() == voiceChannel.getIdLong();
        }
        return false;
    }
}
