package bot.actualcommands.textcommands;

import bot.commandmanagement.ICommand;
import bot.utils.Constants;
import bot.service.Reminder;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class RemindMeCommand implements ICommand {

    private final String remindersFile = "data/reminders.txt";
    private final List<Reminder> reminders = new ArrayList<>();

    public RemindMeCommand() {
        var channels = Constants.getDefaultTextChannels();

        try {
            Path file = Paths.get(remindersFile);

            if (Files.exists(file)) {

                List<String> remindersAsStr = Files.readAllLines(file);

                for (var rem : remindersAsStr) {
                    String[] remData = rem.split(";;;");
                    String id = remData[0];
                    String person = remData[1];
                    String message = remData[2];
                    LocalDateTime when = LocalDateTime.parse(remData[3]);

                    for (var elem : channels.entrySet()) {
                        GuildChannel channel = elem.getKey().getGuildChannelById(id);
                        if (channel != null && !when.isBefore(LocalDateTime.now())) {
                            Reminder reminder = new Reminder((MessageChannel) channel, person, message);
                            reminder.setRemindTime(when);
                            reminder.remind();
                            reminders.add(reminder);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String command() {
        return "remindme";
    }

    @Override
    public String help() {
        return "Usage: `!remindme <hh:mm/today/tomorrow/dayaftertomorrow/day of week/(YYYY-)MM-DD hh:mm> <your message>`\n" +
                "Or: `!remindme + <number of minutes> <your message>`\n" +
                "The bot reminds you at the given time. If no time is given, the default is 10:00 AM";
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        if (args.length > 1) {
            try {
                StringBuilder sb = new StringBuilder();
                for (int i = 3; i < args.length; i++)
                    sb.append(args[i]).append(" ");

                String msg = sb.toString();

                if (msg.contains(";;;")) {
                    event.getChannel().sendMessage("no and f u").queue();
                    throw new IllegalArgumentException();
                }

                if (!args[1].equals("+")) {
                    LocalDateTime alertTime = getAlertTime(args);

                    Reminder reminder = new Reminder(event.getChannel(), event.getAuthor().getAsMention(), msg);

                    event.getChannel().sendMessage("Reminder set to: " + alertTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).queue();

                    reminder.setRemindTime(alertTime);
                    reminder.remind();
                    reminders.add(reminder);

                    serializeReminders(remindersFile);
                } else {
                    LocalDateTime alertTime = reminderFunctionShortened(args);
                    Reminder reminder = new Reminder(event.getChannel(), event.getAuthor().getAsMention(), msg);

                    event.getChannel().sendMessage("Reminder set to: " + alertTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).queue();

                    reminder.setRemindTime(alertTime);
                    reminder.remind();
                    reminders.add(reminder);
                }

            } catch (IOException e) {
                event.getChannel().sendMessage("An error occurred (check your date formatting!)" + Constants.PENSIVE).queue();
            }
        }
    }

    private LocalDateTime getAlertTime(String[] args) throws IllegalArgumentException, DateTimeException {
        LocalDateTime currentTime = LocalDateTime.now();
        var _year = currentTime.getYear();
        var _month = currentTime.getMonthValue();
        var _dayOfMonth = currentTime.getDayOfMonth();

        int hour;
        int minute;

        if (isValidHourAndMinute(args[1])) {
            hour = Integer.parseInt(args[1].substring(0, 2));
            minute = Integer.parseInt(args[1].substring(3, 5));
        } else if (args.length > 2 && isValidHourAndMinute(args[2])) {
            hour = Integer.parseInt(args[2].substring(0, 2));
            minute = Integer.parseInt(args[2].substring(3, 5));
        } else {
            hour = 10;
            minute = 0;
        }

        if (hour < 0 || hour >= 24)
            throw new IllegalArgumentException();

        switch (args[1]) {
            case "ma":
            case "today":
                if (hour > currentTime.getHour() || hour == currentTime.getHour() && minute > currentTime.getMinute())
                    return LocalDateTime.of(_year, _month, _dayOfMonth, hour, minute);
                else
                    throw new IllegalArgumentException();
            case "holnap":
            case "tomorrow":
            case "tmrw":
                return LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(hour, minute));

            case "holnapután":
            case "dayaftertomorrow":
                return LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(hour, minute));

            case "hétfő":
            case "monday":
                return LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)), LocalTime.of(hour, minute));

            case "kedd":
            case "tuesday":
                return LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.TUESDAY)), LocalTime.of(hour, minute));

            case "szerda":
            case "wednesday":
                return LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY)), LocalTime.of(hour, minute));

            case "csütörtök":
            case "thursday":
                return LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.THURSDAY)), LocalTime.of(hour, minute));

            case "péntek":
            case "friday":
                return LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY)), LocalTime.of(hour, minute));

            case "szombat":
            case "saturday":
                return LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SATURDAY)), LocalTime.of(hour, minute));

            case "vasárnap":
            case "sunday":
                return LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)), LocalTime.of(hour, minute));

            default:
                if (isValidHourAndMinute(args[1])) {
                    hour = Integer.parseInt(args[1].substring(0, 2));
                    minute = Integer.parseInt(args[1].substring(3, 5));

                    if (hour > currentTime.getHour() || hour == currentTime.getHour() && minute > currentTime.getMinute())
                        return LocalDateTime.of(_year, _month, _dayOfMonth, hour, minute);
                    else
                        return LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(hour, minute));
                }

        }
        //2020-07-15 22:33 yyyy-mm-dd hh:mm
        if (args[1].matches("\\d{4}.*")) {
            if (args[1].matches("\\d{4}([-/]|\\.)\\d{2}([-/]|\\.)\\d{2}.*")) {
                int year = Integer.parseInt(args[1].substring(0, 4));
                int month = Integer.parseInt(args[1].substring(5, 7));
                int day = Integer.parseInt(args[1].substring(8, 10));

                var finalDate = LocalDateTime.of(year, month, day, hour, minute);

                if (finalDate.isBefore(LocalDateTime.now()))
                    throw new IllegalArgumentException();

                return finalDate;
            } else {
                throw new IllegalArgumentException();
            }

        } else { // month at the beginning, let's assume it's current year
            if (args[1].matches("\\d{2}([-/]|\\.)+\\d{2}.*")) {
                int month = Integer.parseInt(args[1].substring(0, 2));
                int day = Integer.parseInt(args[1].substring(3, 5));

                var finalDate = LocalDateTime.of(_year, month, day, hour, minute);

                if (finalDate.isBefore(LocalDateTime.now()))
                    throw new IllegalArgumentException();

                return finalDate;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    private LocalDateTime reminderFunctionShortened(String[] args) {

        if (!isValidMinute(args[2]))
            throw new IllegalArgumentException();

        long sMinute = Long.parseLong(args[2]);

        return LocalDateTime.now().plusMinutes(sMinute);
    }

    private boolean isValidHourAndMinute(String str) {
        return str.matches("([0-2][0-9]:[0-5][0-9])");
    }

    private boolean isValidMinute(String str) {   // REGEX a   shortened commandnak
        return str.matches("(^[1-9][0-9]{0,3})");
    }

    private void serializeReminders(String fileName) throws IOException {
        var remindersAsString = new ArrayList<String>();

        for (var reminder : reminders) {
            if (reminder.getRemindTime().isAfter(LocalDateTime.now())) {
                StringBuilder sb = new StringBuilder();
                sb.append(reminder.getChannelId()).append(";;;").append(reminder.getPersonToRemind()).append(";;;");
                sb.append(reminder.getMessage()).append(";;;").append(reminder.getRemindTime());

                remindersAsString.add(sb.toString());
            }
        }

        Path file = Paths.get(fileName);
        Files.write(file, remindersAsString, StandardCharsets.UTF_8);
    }

}
