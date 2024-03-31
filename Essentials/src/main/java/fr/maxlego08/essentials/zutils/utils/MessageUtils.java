package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.messages.DefaultFontInfo;
import fr.maxlego08.essentials.api.messages.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class MessageUtils extends PlaceholderUtils {

    protected final ComponentMessage componentMessage = new ComponentMessage();

    protected void message(CommandSender sender, Message message, Object... args) {

        if (sender instanceof Player player) {

            switch (message.getMessageType()) {

                case ACTION -> {
                    this.componentMessage.sendActionBar(sender, Message.PREFIX.getMessage() + getMessage(message, args));
                }
                case TCHAT -> {
                    sendTchatMessage(sender, message, args);
                }
                case TITLE -> {
                    // ToDo
                }
                case CENTER -> {
                    if (message.getMessages().size() > 0) {
                        message.getMessages().forEach(msg -> this.componentMessage.sendMessage(sender, getCenteredMessage(getMessage(msg, args))));
                    } else {
                        this.componentMessage.sendMessage(sender, getCenteredMessage(getMessage(message, args)));
                    }
                }
            }

        } else {
            sendTchatMessage(sender, message, args);
        }
    }

    private void sendTchatMessage(CommandSender sender, Message message, Object... args) {
        if (message.getMessages().size() > 0) {
            message.getMessages().forEach(msg -> this.componentMessage.sendMessage(sender, Message.PREFIX.getMessage() + getMessage(msg, args)));
        } else {
            this.componentMessage.sendMessage(sender, Message.PREFIX.getMessage() + getMessage(message, args));
        }
    }

    protected String getMessage(Message message, Object... args) {
        return getMessage(message.getMessage(), args);
    }

    protected String getMessage(String message, Object... args) {

        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("Number of invalid arguments. Arguments must be in pairs.");
        }

        for (int i = 0; i < args.length; i += 2) {
            if (args[i] == null || args[i + 1] == null) {
                throw new IllegalArgumentException("Keys and replacement values must not be null.");
            }
            message = message.replace(args[i].toString(), args[i + 1].toString());
        }
        return message;
    }

    // ToDo, rework with componrent
    protected String getCenteredMessage(String message) {
        if (message == null || message.equals(""))
            return "";

        int CENTER_PX = 154;

        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == "§".charAt(0)) {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb + message;
    }

}
