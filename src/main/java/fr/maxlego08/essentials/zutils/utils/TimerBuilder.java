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
            message.append(days).append(days <= 1 ? Message.FORMAT_DAY.getMessageAsString() : Message.FORMAT_DAYS.getMessageAsString()).append(" ");
        }
        if (hours > 0) {
            message.append(hours).append(hours <= 1 ? Message.FORMAT_HOUR.getMessageAsString() : Message.FORMAT_HOURS.getMessageAsString()).append(" ");
        }
        if (minutes > 0) {
            message.append(minutes).append(minutes <= 1 ? Message.FORMAT_MINUTE.getMessageAsString() : Message.FORMAT_MINUTES.getMessageAsString()).append(" ");
        }
        if (totalSeconds < 10) {
            if (totalSeconds == (long) totalSeconds) {
                long roundedSeconds = (long) totalSeconds;
                message.append(roundedSeconds).append(roundedSeconds <= 1 ? Message.FORMAT_SECOND.getMessageAsString() : Message.FORMAT_SECONDS.getMessageAsString());
            } else {
                message.append(String.format("%.1f", totalSeconds)).append(Message.FORMAT_SECONDS.getMessageAsString());
            }
        } else if (seconds > 0 || message.isEmpty()) {
            message.append(seconds).append(seconds <= 1 ? Message.FORMAT_SECOND.getMessageAsString() : Message.FORMAT_SECONDS.getMessageAsString());
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
