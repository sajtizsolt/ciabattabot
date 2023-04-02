package bot.lavaplayer;

import bot.utils.Utils;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TrackScheduler extends AudioEventAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackScheduler.class);

    private final AudioPlayer player;
    private BlockingQueue<AudioTrack> queue;

    private boolean looping = false;

    private MessageReceivedEvent event; //storing this here is cancer to the power of cancer
    private long trackStartMessageId;

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public void setQueue(BlockingQueue<AudioTrack> queue) {
        this.queue = queue;
    }

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track, boolean canInterrupt) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        if (!player.startTrack(track, !canInterrupt)) {
            sendContentAddedFeedback(track, true);
            queue.offer(track);
        }
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        // Player was paused
        player.setPaused(true);
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        // Player was resumed
        player.setPaused(false);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        // A track started playing
        sendContentAddedFeedback(track, false);
        LOGGER.info("A track just started playing");
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        //if (!track.getSourceManager().getSourceName().equals("local"))
        if (!track.getIdentifier().contains("batta_shutdown.mp3"))
            event.getChannel().retrieveMessageById(trackStartMessageId).queue((message -> message.delete().queue()));

        if (looping) {
            queue.add(track.makeClone());
        }
        if (endReason.mayStartNext && queue.size() > 0) {
            // Start next track
//            if(looping && endReason == AudioTrackEndReason.FINISHED){
//                queue.add(track);
//                //TODO
//            }
            nextTrack();
        } else if (endReason == AudioTrackEndReason.STOPPED || endReason == AudioTrackEndReason.CLEANUP) {
            player.destroy();
        }

        if (!Utils.isActualUserLeftInVoiceChannel(Objects.requireNonNull(event.getJDA().getAudioManagers().get(0).getConnectedChannel()).asVoiceChannel()))
            event.getJDA().getAudioManagers().get(0).closeAudioConnection();

        // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
        // endReason == STOPPED: The player was stopped.
        // endReason == REPLACED: Another track started playing while this had not finished
        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
        //                       clone of this back to your queue
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        // An already playing track threw an exception (track end event will still be received separately)
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        // Audio track has been unable to provide us any audio, might want to just start a new track
    }

    public void nextTrack() {
        player.startTrack(queue.poll(), false);
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public void setEvent(MessageReceivedEvent event) {
        this.event = event;
    }

    private void sendContentAddedFeedback(AudioTrack track, boolean queued) {
        EmbedBuilder message = new EmbedBuilder();
        if (!track.getSourceManager().getSourceName().equals("local")) { //lokális fájl esetén ne legyen exception ebből

            if (queued) {
                message.setAuthor("Added to queue");

            } else {
                message.setAuthor("Now playing");
                message.setImage(getYTVideoThumbnailURL(track.getInfo().uri));
            }

            message.setColor(Color.RED);
            message.setTitle(track.getInfo().title, track.getInfo().uri);

            var length = track.getInfo().length;
            long hours = TimeUnit.MILLISECONDS.toHours(length);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(length) % 60;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(length) % 60;
            if (hours == 0)
                message.appendDescription(String.format("%02d : %02d", minutes, seconds));
            else
                message.appendDescription(String.format("%02d : %02d : %02d", hours, minutes, seconds));

            message.setFooter("Added by " + event.getMember().getEffectiveName());

            event.getChannel().sendMessageEmbeds(message.build()).queue((msg) -> {
                if (!queued) trackStartMessageId = msg.getIdLong();
            });

        } else {
            if (!track.getIdentifier().contains("batta_shutdown.mp3")) {

                message.setAuthor("Playing local track");
                message.setColor(Color.GRAY);
                message.setTitle(track.getIdentifier().substring(track.getIdentifier().lastIndexOf("/") + 1));

                event.getChannel().sendMessageEmbeds(message.build()).queue((msg) -> {
                    trackStartMessageId = msg.getIdLong();
                });
            }
        }
    }

    private String getYTVideoThumbnailURL(String url) {
        if (url.contains("youtube")) {
            String id = url.substring(url.lastIndexOf('=') + 1);
            return "https://img.youtube.com/vi/" + id + "/1.jpg";
        }
        return null;
    }
}
