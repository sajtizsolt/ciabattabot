package bot.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Map;

public final class Constants {
    public static final String PREFIX = "!";

    public static final String SMILING_FACE_WITH_3_HEARTS = ":smiling_face_with_3_hearts:";
    public static final String SLIGHT_SMILE = ":slight_smile:";
    public static final String PENSIVE = ":pensive:";
    public static final String PENSIVE_CHAIN = ":pensive: :pensive: :pensive: :thumbsup:";
    public static final String THINKING = ":thinking:";
    public static final String TRIUMPH = ":triumph:";
    public static final String FACE_WITH_RAISED_EYEBROWS = ":face_with_raised_eyebrow:";

    private static Map<Guild, TextChannel> defaultTextChannels = null;

    public static void setDefaultTextChannels(Map<Guild, TextChannel> map) {
        if (defaultTextChannels == null)
            defaultTextChannels = map;
    }

    public static Map<Guild, TextChannel> getDefaultTextChannels() {
        return defaultTextChannels;
    }

}
