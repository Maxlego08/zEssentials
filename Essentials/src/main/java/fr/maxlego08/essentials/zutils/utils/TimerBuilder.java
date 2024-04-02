package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.messages.Message;

public class TimerBuilder {

    private static String formatDuration(double totalSeconds) {
        long days = (long) (totalSeconds / 86400L);
        long hours = (long) ((totalSeconds % 86400L) / 3600L);
        long minutes = (long) ((totalSeconds % 3600L) / 60L);
        long seconds = (long) (totalSeconds % 60L);

        StringBuilder message = new StringBuilder();
        if (days > 0) {
            message.append(days).append(" ").append(days <= 1 ? Message.FORMAT_DAY.getMessage() : Message.FORMAT_DAYS.getMessage()).append(" ");
        }
        if (hours > 0) {
            message.append(hours).append(" ").append(hours <= 1 ? Message.FORMAT_HOUR.getMessage() : Message.FORMAT_HOURS.getMessage()).append(" ");
        }
        if (minutes > 0) {
            message.append(minutes).append(" ").append(minutes <= 1 ? Message.FORMAT_MINUTE.getMessage() : Message.FORMAT_MINUTES.getMessage()).append(" ");
        }
        if (totalSeconds < 10) {
            message.append(String.format("%.1f", totalSeconds)).append(" ").append(Message.FORMAT_SECONDS.getMessage());
        } else if (seconds > 0 || message.length() == 0) {
            message.append(seconds).append(" ").append(seconds <= 1 ? Message.FORMAT_SECOND.getMessage() : Message.FORMAT_SECONDS.getMessage());
        }

        return format(message.toString().trim());
    }

    public static String getStringTime(double milliseconds) {
        return formatDuration(milliseconds / 1000.0);
    }

    private static String format(String message) {
        return message.replaceAll(" 0 [a-zA-Z]+(\\s|$)", "");
    }
}
