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
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        this.componentMessage.sendMessage(sender, message);
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

        if (sender == null) return;

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
                    this.componentMessage.sendBossBar(plugin, player, bossBarMessage, handleArgs(args));
                } else if (essentialsMessage instanceof TitleMessage titleMessage) {

                    this.componentMessage.sendTitle(player, titleMessage, handleArgs(args));
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
        boolean isWithoutPrefix = classicMessage.messageType() == MessageType.WITHOUT_PREFIX || classicMessage.messages().size() > 1;
        classicMessage.messages().forEach(message -> this.componentMessage.sendMessage(sender, (isWithoutPrefix ? "" : Message.PREFIX.getMessageAsString()) + getMessage(message, args)));
    }

    protected String getMessage(Message message, Object... args) {
        return getMessage(String.join("\n", message.getMessageAsStringList()), args);
    }

    protected String getMessage(String message, Object... args) {
        return getString(message, handleArgs(args));
    }

    /**
     * Normalises the given arguments so that {@link Player}/{@link User} objects are expanded into their
     * %player%/%displayName% placeholder pairs. This is the same normalisation applied to classic chat
     * messages and must be applied to every message type (title, boss bar, ...) so internal placeholders
     * such as %name% or %seconds% are resolved consistently.
     *
     * @param args the raw arguments
     * @return the normalised, placeholder-paired arguments
     */
    protected Object[] handleArgs(Object... args) {
        List<Object> modifiedArgs = new ArrayList<>();
        for (Object arg : args) handleArg(arg, modifiedArgs);
        return modifiedArgs.toArray();
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

        String plainMessage = PlainTextComponentSerializer.plainText().serialize(MiniMessage.miniMessage().deserialize(message));

        int messagePxSize = 0;
        for (char character : plainMessage.toCharArray()) {
            DefaultFontInfo fontInfo = DefaultFontInfo.getDefaultFontInfo(character);
            messagePxSize += fontInfo.getLength() + 1;
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
