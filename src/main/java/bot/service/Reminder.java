package bot.service;


import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Reminder {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Map<ScheduledFuture<?>, Boolean> tasksWithPrescheduledJobs = new HashMap<>();
    private MessageChannel channel;
    private String personToRemind;
    private String message;

    private LocalDateTime remindTime;

    public Reminder() {
    }

    public Reminder(MessageChannel channel, String personToRemind, String message) {
        this.channel = channel;
        this.personToRemind = personToRemind;
        this.message = message;
    }

    public void remind() {
        Runnable sender = new Runnable() {
            public void run() {
                channel.sendMessage(personToRemind + " " + message).queue();
            }
        };
        long delay = ChronoUnit.SECONDS.between(LocalDateTime.now(), remindTime);
        scheduler.schedule(sender, delay, TimeUnit.SECONDS);
    }

    public void remind(Runnable sender, boolean deletable) {
        long delay = ChronoUnit.SECONDS.between(LocalDateTime.now(), remindTime);
        tasksWithPrescheduledJobs.put(scheduler.schedule(sender, delay, TimeUnit.SECONDS), deletable);
    }

    public Map<ScheduledFuture<?>, Boolean> getTasksWithPrescheduledJobs() {
        return tasksWithPrescheduledJobs;
    }

    public void setRemindTime(LocalDateTime remindTime) {
        this.remindTime = remindTime;
    }

    public long getChannelId() {
        return channel.getIdLong();
    }

    public String getPersonToRemind() {
        return personToRemind;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getRemindTime() {
        return remindTime;
    }

    public void setChannel(MessageChannel channel) {
        this.channel = channel;
    }

    public void setPersonToRemind(String personToRemind) {
        this.personToRemind = personToRemind;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }
}
