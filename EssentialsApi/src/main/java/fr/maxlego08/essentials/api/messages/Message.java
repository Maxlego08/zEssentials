package fr.maxlego08.essentials.api.messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Message {

    // Rework message for a better system

    PREFIX("§8(§6zEssentials§8) "),

    TIME_DAY("%02d %day% %02d %hour% %02d %minute% %02d %second%"),
    TIME_HOUR("%02d %hour% %02d minute(s) %02d %second%"),
    TIME_MINUTE("%02d %minute% %02d %second%"),
    TIME_SECOND("%02d %second%"),

    FORMAT_SECOND("second"),
    FORMAT_SECONDS("seconds"),

    FORMAT_MINUTE("minute"),
    FORMAT_MINUTES("minutes"),

    FORMAT_HOUR("hour"),
    FORMAT_HOURS("hours"),

    FORMAT_DAY("d"),
    FORMAT_DAYS("days"),

    COMMAND_SYNTAX_ERROR("§cYou must execute the command like this§7: §a%syntax%"),
    COMMAND_NO_PERMISSION("§cYou do not have permission to run this command."),
    COMMAND_NO_CONSOLE("§cOnly one player can execute this command."),
    COMMAND_NO_ARG("§cImpossible to find the command with its arguments."),
    COMMAND_SYNTAXE_HELP("§f%syntax% §7» §7%description%"),

    COMMAND_RELOAD("§aYou have just reloaded the configuration files."),
    COMMAND_ESSENTIALS("zEssentials, version %version%"),
    COMMAND_GAMEMODE("&fSet game mode&e %gamemode% &ffor &b%player%&f."),
    COMMAND_GAMEMODE_INVALID("&cYou need to specify a valid player."),
    COMMAND_DAY("&fYou have just brought &edaylight&f into the world &a%world%&f."),
    COMMAND_NIGHT("&fYou have just brought &enightfall&f into the world &a%world%&f."),
    COMMAND_SUN("&fYou have just brought the &esun&f into the world &a%world%&f."),
    COMMAND_TOP("§7You've just been teleported to &etop§7."),
    COMMAND_TOP_ERROR("&cUnable to find a position to transport you safely."),
    COMMAND_SPEED_INVALID("&cYou need to specify a valid player."),
    COMMAND_SPEED_FLY("&7You have just set your &nfly&r&7 speed to &f%speed%&7 for &f%player%&7. &8(&f2 &7by default&8)"),
    COMMAND_SPEED_WALK("&7You have just set your &nwalk&r&7 speed to &f%speed%&7 for &f%player%&7. &8(&f2 &7by default&8)"),
    COMMAND_SPEED_ERROR("&cYou must enter a number between &60&c and &610&c. &8(&f2 &7by default&8)"),


    DESCRIPTION_RELOAD("Reload configuration files"),
    DESCRIPTION_GAMEMODE("Change player gamemode"),
    DESCRIPTION_GAMEMODE_CREATIVE("Change player gamemode to creative"),
    DESCRIPTION_GAMEMODE_SURVIVAL("Change player gamemode to survival"),
    DESCRIPTION_GAMEMODE_ADVENTURE("Change player gamemode to adventure"),
    DESCRIPTION_GAMEMODE_SPECTATOR("Change player gamemode to spectator"),
    DESCRIPTION_DAY("Set the day in your world"),
    DESCRIPTION_NIGHT("Set the night in your world"),
    DESCRIPTION_SUN("Set the sun in your world"),
    DESCRIPTION_ENDERCHEST("Open your enderchest"),
    DESCRIPTION_ENDERSEE("Open a player enderchest"),
    DESCRIPTION_TOP("Teleporting to top"),
    DESCRIPTION_SPEED("Change player speed"),

    YOU("you");

    private String message;
    private List<String> messages;
    private MessageType messageType = MessageType.TCHAT;

    Message(String message) {
        this.message = message;
        this.messages = new ArrayList<>();
    }

    Message(String... message) {
        this.message = null;
        this.messages = Arrays.asList(message);
    }

    public String getMessage() {
        return message;
    }

    public List<String> getMessages() {
        return messages;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
