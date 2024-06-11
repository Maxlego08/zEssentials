package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.DefaultFontInfo;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.messages.MessageType;
import fr.maxlego08.essentials.api.messages.messages.BossBarMessage;
import fr.maxlego08.essentials.api.messages.messages.ClassicMessage;
import fr.maxlego08.essentials.api.messages.messages.TitleMessage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.component.ComponentMessage;
import fr.maxlego08.menu.zcore.utils.nms.NMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MessageUtils extends PlaceholderUtils {

    protected final ComponentMessage componentMessage = ComponentMessageHelper.componentMessage;

    public static String getString(String message, Object[] newArgs) {
        if (newArgs.length % 2 != 0) {
            throw new IllegalArgumentException("Number of invalid arguments. Arguments must be in pairs.");
        }

        for (int i = 0; i < newArgs.length; i += 2) {
            if (newArgs[i] == null || newArgs[i + 1] == null) {
                throw new IllegalArgumentException("Keys and replacement values must not be null.");
            }
            message = message.replace(newArgs[i].toString(), newArgs[i + 1].toString());
        }
        return message;
    }

    protected void message(CommandSender sender, String message) {
        sender.sendMessage(message);
    }

    protected void message(User sender, Message message, Object... args) {
        message(sender.getPlayer(), message, args);
    }

    protected void message(UUID uniqueId, Message message, Object... args) {
        Player player = Bukkit.getPlayer(uniqueId);
        if (player == null) return;
        message(player, message, args);
    }

    protected void broadcast(Permission permission, Message message, Object... args) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.hasPermission(permission.asPermission())) {
                message(player, message, args);
            }
        });
        message(Bukkit.getConsoleSender(), message, args);
    }

    protected void broadcast(Message message, Object... args) {
        Bukkit.getOnlinePlayers().forEach(player -> message(player, message, args));
        message(Bukkit.getConsoleSender(), message, args);
    }

    protected void message(CommandSender sender, Message message, Object... args) {

        if (sender instanceof Player player) {
            message.getMessages().forEach(essentialsMessage -> {

                if (essentialsMessage instanceof ClassicMessage classicMessage) {

                    switch (essentialsMessage.messageType()) {
                        case TCHAT, WITHOUT_PREFIX -> sendTchatMessage(sender, classicMessage, args);
                        case ACTION -> classicMessage.messages().forEach(currentMessage -> {
                            this.componentMessage.sendActionBar(player, getMessage(currentMessage, args));
                        });
                        case CENTER -> classicMessage.messages().forEach(currentMessage -> {
                            this.componentMessage.sendMessage(sender, getCenteredMessage(getMessage(currentMessage, args)));
                        });
                    }

                } else if (essentialsMessage instanceof BossBarMessage bossBarMessage) {

                    EssentialsPlugin plugin = (EssentialsPlugin) Bukkit.getPluginManager().getPlugin("zEssentials");
                    this.componentMessage.sendBossBar(plugin, player, bossBarMessage);
                } else if (essentialsMessage instanceof TitleMessage titleMessage) {

                    this.componentMessage.sendTitle(player, titleMessage);
                }
            });
        } else {
            message.getMessages().forEach(essentialsMessage -> {
                if (essentialsMessage instanceof ClassicMessage classicMessage) {
                    sendTchatMessage(sender, classicMessage, args);
                }
            });
        }
    }

    private void sendTchatMessage(CommandSender sender, ClassicMessage classicMessage, Object... args) {
        boolean isWithoutPrefix = classicMessage.messageType() == MessageType.WITHOUT_PREFIX;
        classicMessage.messages().forEach(message -> {
            this.componentMessage.sendMessage(sender, (isWithoutPrefix ? "" : Message.PREFIX.getMessageAsString()) + getMessage(message, args));
        });
    }

    protected String getMessage(Message message, Object... args) {
        return getMessage(String.join("\n", message.getMessageAsStringList()), args);
    }

    protected String getMessage(String message, Object... args) {

        List<Object> modifiedArgs = new ArrayList<>();
        for (Object arg : args) handleArg(arg, modifiedArgs);
        Object[] newArgs = modifiedArgs.toArray();

        return getString(message, newArgs);
    }

    private void handleArg(Object arg, List<Object> modifiedArgs) {
        if (arg instanceof Player player) {
            addPlayerDetails(modifiedArgs, player.getName(), player.getDisplayName());
        } else if (arg instanceof User user) {
            addPlayerDetails(modifiedArgs, user.getName(), user.getPlayer().getDisplayName());
        } else {
            modifiedArgs.add(arg);
        }
    }

    private void addPlayerDetails(List<Object> modifiedArgs, String name, String displayName) {
        modifiedArgs.add("%player%");
        modifiedArgs.add(name);
        modifiedArgs.add("%displayName%");
        modifiedArgs.add(displayName);
    }

    // ToDo, rework with component
    protected String getCenteredMessage(String message) {
        if (message == null || message.equals("")) return "";

        int CENTER_PX = 154;

        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
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

    protected String color(String message) {
        if (message == null) return null;
        if (NMSUtils.isHexColor()) {
            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                String color = message.substring(matcher.start(), matcher.end());
                message = message.replace(color, String.valueOf(net.md_5.bungee.api.ChatColor.of(color)));
                matcher = pattern.matcher(message);
            }
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }

}
