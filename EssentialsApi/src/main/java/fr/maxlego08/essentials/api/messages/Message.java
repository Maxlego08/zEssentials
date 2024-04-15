package fr.maxlego08.essentials.api.messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Message {

    // Rework message for a better system

    PREFIX("&8(&6zEssentials&8) "),
    PLAYER_NOT_FOUND("&f%player% #FF0000was not found."),
    MODULE_DISABLE("#FF0000The &f%name% #FF0000module is disabled. You cannot use this command."),

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

    COMMAND_SYNTAX_ERROR("&cYou must execute the command like this&7: &a%syntax%"),
    COMMAND_NO_PERMISSION("&cYou do not have permission to run this command."),
    COMMAND_NO_CONSOLE("&cOnly one player can execute this command."),
    COMMAND_NO_ARG("&cImpossible to find the command with its arguments."),
    COMMAND_SYNTAXE_HELP("&f%syntax% &7» &7%description%"),

    COMMAND_RELOAD("&aYou have just reloaded the configuration files."),
    COMMAND_ESSENTIALS("zEssentials, version %version%"),
    COMMAND_GAMEMODE("&fSet game mode&e %gamemode% &ffor &b%player%&f."),
    COMMAND_GAMEMODE_INVALID("&cYou need to specify a valid player."),
    COMMAND_DAY("&fYou have just brought &edaylight&f into the world &a%world%&f."),
    COMMAND_NIGHT("&fYou have just brought &enightfall&f into the world &a%world%&f."),
    COMMAND_SUN("&fYou have just brought the &esun&f into the world &a%world%&f."),
    COMMAND_TOP("&7You've just been teleported to &etop&7."),
    COMMAND_TOP_ERROR("&cUnable to find a position to transport you safely."),
    COMMAND_SPEED_INVALID("&cYou need to specify a valid player."),
    COMMAND_SPEED_FLY("&7You have just set your &nfly&r&7 speed to &f%speed%&7 for &f%player%&7. &8(&f2 &7by default&8)"),
    COMMAND_SPEED_WALK("&7You have just set your &nwalk&r&7 speed to &f%speed%&7 for &f%player%&7. &8(&f2 &7by default&8)"),
    COMMAND_SPEED_ERROR("&cYou must enter a number between &60&c and &610&c. &8(&f2 &7by default&8)"),

    COMMAND_FLY_ENABLE("&7Flight mode &aenable &7for &f%player%&a."),
    COMMAND_FLY_DISABLE("&7Flight mode &cdisable &7for &f%player%&a."),
    COMMAND_GOD_ENABLE("&7God mode &aenable &7for &f%player%&a."),
    COMMAND_GOD_DISABLE("&7God mode &cdisable &7for &f%player%&a."),
    COMMAND_HEAL_SENDER("&7You just healed the player &f%player%&7."),
    COMMAND_HEAL_RECEIVER("&aYou have been healed."),
    COMMAND_HEAL_ERROR("&cYou cannot heal someone who is dead !"),
    COMMAND_FEED_SENDER("&7You just feed the player &f%player%&7."),
    COMMAND_FEED_RECEIVER("&aYou have been feed."),
    COMMAND_FEED_ERROR("&cYou cannot feed someone who is dead !"),

    // Teleport Command
    COMMAND_TPA_ERROR("&cYou have already sent a request to #34cfe0%player%&c."),
    COMMAND_TPA_ERROR_SAME("&cYou cannot teleport to yourself."),
    COMMAND_TPA_ERROR_OFFLINE("&cUnable to find player, must be offline."),
    COMMAND_TPA_ERROR_TO_LATE_EMPTY("&cYou do not have a teleport request."),
    COMMAND_TPA_ERROR_TO_LATE_EXPIRE("&cThe teleport request has expired."),
    COMMAND_TPA_SENDER("&7You have just sent a teleport request to #34cfe0%player%&7."),
    COMMAND_TPA_RECEIVER(
            "&7You have just received a teleport request from #34cfe0%player%&7.",
            "&7You have &c60 &6seconds&e to accept the teleport request.",
            "&7To accept the request do #0EEA93/tpaccept&7."
    ),
    COMMAND_TPA_ACCEPT_RECEIVER("&aYou have just accepted the teleport request from #34cfe0%player%&a."),
    COMMAND_TPA_ACCEPT_SENDER("#34cfe0%player%&a has just accepted your teleport request."),
    COMMAND_TELEPORT_IGNORE_PLAYER("&cYou cannot send a teleport request to #34cfe0%player%&c they are ignoring you."),
    COMMAND_TELEPORT_WORLD("&cYou need to be in the same world to teleport."),
    COMMAND_TPA_ERROR_TO_LATE("&cYou do not have a teleport request."),
    COMMAND_TPA_ERROR_TO_LATE_2("&cThe request has expired."),
    COMMAND_TP_DENY_SENDER("Denied %player% teleport request."),
    COMMAND_TP_DENY_RECEIVER("%player% has denied your teleport request"),
    COMMAND_TP_CANCEL_ERROR("#ff0000You did not send a teleport request at &f%player%#ff0000."),
    COMMAND_TP_CANCEL_SENDER("#ff0000Cancelled #99E0FFyour teleport request to %player%."),
    COMMAND_TP_CANCEL_RECEIVER("&f%player% #ff0000cancelled their teleport request to you."),
    COMMAND_TP("&7You just teleport to the player #34cfe0%player%&f."),
    COMMAND_TP_SELF("&7You just teleported #34cfe0%player%&7 to your position."),
    COMMAND_BACK("&7Returning to previous location."),
    COMMAND_BACK_ERROR("#ff0000You have no last location. Impossible to go back."),

    COMMAND_MORE_ERROR("&cYou cannot make this order in item in hand."),
    COMMAND_MORE_SUCCESS("&7You just put your item to &f64&7."),
    COMMAND_WORLD_TELEPORT_SELF("&aYou have just been teleported into the world &f%world%&a."),
    COMMAND_WORLD_TELEPORT_OTHER("&aYou just teleported the player &f%player% &ain the world &f%world%&a."),

    COMMAND_COMPACT_TYPE("&cImpossible to compact the material &f%material%&c."),
    COMMAND_COMPACT_ERROR("&cYou have no &f%item%&c in your inventory."),
    COMMAND_COMPACT_SUCCESS("&7You have just transformed #0EEA93x%amount% #34cfe0%item% &7en #0EEA93x%toAmount% #34cfe0%toItem%&7."),
    COMMAND_HAT_SUCCESS("&7You just put #0ef0ce%item% &7on your head."),
    COMMAND_HAT_ERROR("&cYou cannot put air on your head."),
    COMMAND_PLAYER_WEATHER_RESET("&7You just changed the weather to that of the server."),
    COMMAND_PLAYER_WEATHER_DOWNFALL("&7You just put the rain on for yourself."),
    COMMAND_PLAYER_TIME_RESET("&7You just changed the time to that of the server."),
    COMMAND_PLAYER_TIME_CHANGE("&7You’re here to change your time."),

    DESCRIPTION_RELOAD("Reload configuration files"),
    DESCRIPTION_GAMEMODE("Change player gamemode"),
    DESCRIPTION_GAMEMODE_CREATIVE("Change player gamemode to creative"),
    DESCRIPTION_GAMEMODE_SURVIVAL("Change player gamemode to survival"),
    DESCRIPTION_GAMEMODE_ADVENTURE("Change player gamemode to adventure"),
    DESCRIPTION_GAMEMODE_SPECTATOR("Change player gamemode to spectator"),
    DESCRIPTION_DAY("Set the day in your world"),
    DESCRIPTION_NIGHT("Set the night in your world"),
    DESCRIPTION_GOD("Toggle god mode"),
    DESCRIPTION_FLY("Toggle flight"),
    DESCRIPTION_HEAL("Heal a player"),
    DESCRIPTION_SUN("Set the sun in your world"),
    DESCRIPTION_ENDERCHEST("Open your enderchest"),
    DESCRIPTION_ENDERSEE("Open a player enderchest"),
    DESCRIPTION_TOP("Teleporting to top"),
    DESCRIPTION_SPEED("Change player speed"),
    DESCRIPTION_TPA("Teleport to a player"),
    DESCRIPTION_TP("Teleport to a player"),
    DESCRIPTION_TP_RANDOM("Random Teleport in the world"),
    DESCRIPTION_BACK("Teleport to your previous location"),
    DESCRIPTION_TP_SELF("Teleport a player to your location"),
    DESCRIPTION_TPA_ACCEPT("Accept a teleportation request"),
    DESCRIPTION_TPA_DENY("Denied a teleportation request"),
    DESCRIPTION_TPA_CANCEL("Cancel a teleportation request"),
    DESCRIPTION_MORE("Get more items"),
    DESCRIPTION_TP_WORLD("Teleport to another world"),
    DESCRIPTION_TRASH("Open a trash can"),
    DESCRIPTION_FEED("Feed a player"),
    DESCRIPTION_CRAFT("Open workbrench"),
    DESCRIPTION_ENCHANTING("Open enchantment table"),
    DESCRIPTION_ANVIL("Open an anvil"),
    DESCRIPTION_CARTOGRAPHYTABLE("Open a cartography table"),
    DESCRIPTION_GRINDSTONE("Open a grind stone"),
    DESCRIPTION_LOOM("Open a loom"),
    DESCRIPTION_STONECUTTER("Open a stone cutter"),
    DESCRIPTION_SMITHINGTABLE("Open a smithing table"),
    DESCRIPTION_INVSEE("Open player's inventory"),
    DESCRIPTION_COMPACT("Compact material"),
    DESCRIPTION_HAT("Create your custom hat !"),
    DESCRIPTION_PLAYER_WEATHER("Change your weather"),
    DESCRIPTION_PLAYER_TIME("Change your time"),
    DESCRIPTION_MONEY("Show your money"),
    DESCRIPTION_ECO("Manages the server economies"),
    DESCRIPTION_ECO_SET("Sets the specified player's balance to the specified amount of money"),
    DESCRIPTION_ECO_TAKE("Takes the specified amount of money from the specified player"),
    DESCRIPTION_ECO_GIVE("Gives the specified player the specified amount of money"),
    DESCRIPTION_ECO_GIVE_ALL("Gives for all players the specified amount of money"),
    DESCRIPTION_ECO_RESET("Resets the specified player's balance to the server's starting balance"),
    DESCRIPTION_ECO_SHOW("Show player money"),
    DESCRIPTION_PAY("Pays another player from your balance"),
    DESCRIPTION_SET_SPAWN("Set server spawn"),
    DESCRIPTION_SPAWN("Teleport to spawn"),
    DESCRIPTION_WARP_SET("Create a warp"),
    DESCRIPTION_WARP_USE("Teleport to a warp"),
    DESCRIPTION_WARP_DEL("Delete a warp"),
    DESCRIPTION_WARP_LIST("Show warp list"),
    DESCRIPTION_SET_HOME("Create a home"),
    DESCRIPTION_DEL_HOME("Delete a home"),
    DESCRIPTION_HOME("Teleport to a home"),
    DESCRIPTION_KICK("Kick a player"),
    DESCRIPTION_KICK_ALL("Kick all players"),

    YOU("you"),
    TRASH("&8Trash"),

    // Teleportation

    TELEPORT_MOVE("&cYou must not move!"),
    TELEPORT_MESSAGE(MessageType.ACTION, "&7Teleporting in #0EEA93%seconds% &7seconds, you must not move."),
    TELEPORT_SUCCESS(MessageType.ACTION, "#99E0FFYou have just teleported successfully!"),
    TELEPORT_MESSAGE_RANDOM(MessageType.ACTION, "&7Teleporting in #0EEA93%seconds% &7seconds, you must not move."),
    TELEPORT_SUCCESS_RANDOM(MessageType.ACTION, "#99E0FFYou have just teleported successfully!"),
    TELEPORT_MESSAGE_SPAWN(MessageType.TCHAT_AND_ACTION, "&7Teleporting in #0EEA93%seconds% &7seconds, you must not move."),
    TELEPORT_SUCCESS_SPAWN(MessageType.TCHAT_AND_ACTION, "&7You just teleported to #0EEA93spawn &7!"),
    TELEPORT_MESSAGE_WARP(MessageType.TCHAT_AND_ACTION, "&7Teleporting in #0EEA93%seconds% &7seconds, you must not move."),
    TELEPORT_SUCCESS_WARP(MessageType.TCHAT_AND_ACTION, "&7You just teleported to warp #0EEA93%name% &7!"),
    TELEPORT_MESSAGE_HOME(MessageType.TCHAT_AND_ACTION, "&7Teleporting in #0EEA93%seconds% &7seconds, you must not move."),
    TELEPORT_SUCCESS_HOME(MessageType.TCHAT_AND_ACTION, "&7You just teleported to home #0EEA93%name% &7!"),
    TELEPORT_DAMAGE("&cYou must not take damage during teleportation."),
    TELEPORT_ERROR_LOCATION("&cUnable to teleport you safely."),

    COOLDOWN("#ff0000✘ You must wait for &f%cooldown% #ff0000before performing this action."),

    // Economy

    COMMAND_ECONOMY_NOT_FOUND("#ff0000 Can’t find a economy with the name &f%name%#ff0000."),
    COMMAND_ECONOMY_GIVE_ALL_SENDER("#99E0FFYou just gave &f%economyFormat% #99E0FFto the online players."),
    COMMAND_ECONOMY_GIVE_SENDER("#99E0FFYou just gave &f%economyFormat% #99E0FFto the player &7%player%#99E0FF."),
    COMMAND_ECONOMY_GIVE_RECEIVER("#99E0FFYou have just received &f%economyFormat%."),
    COMMAND_ECONOMY_SET_SENDER("#99E0FFYou just set &f%economyFormat% #99E0FFto the player &7%player%#99E0FF."),
    COMMAND_ECONOMY_SET_RECEIVER("#99E0FFYou have just been modified &f%economyFormat%."),
    COMMAND_ECONOMY_TAKE_SENDER("#99E0FFYou just take &f%economyFormat% #99E0FFto the player &7%player%#99E0FF."),
    COMMAND_ECONOMY_TAKE_RECEIVER("#99E0FFYou have just lost &f%economyFormat%."),
    COMMAND_ECONOMY_SHOW_EMPTY("&f%player% #FF0000has no money."),
    COMMAND_ECONOMY_SHOW_INFO("&7- #99E0FF%economy% &f%amount%."),
    COMMAND_MONEY(
            "#99E0FFYou have&8:" ,
            " &7- #99E0FF%economy-name-money% &f%economy-money%.",
            " &7- #99E0FF%economy-name-coins% &f%economy-coins%."
    ),

    COMMAND_PAY_NEGATIVE("#ff0000Amount to pay must be positive."),
    COMMAND_PAY_MIN("#ff0000The minimum amount you can pay is &f%amount%#ff0000."),
    COMMAND_PAY_MAX("#ff0000The maximum amount you can pay is &f%amount%#ff0000."),
    COMMAND_PAY_SENDER("#99E0FFYou just sent #F8F327%amount%#99E0FF to the player &f%player%#99E0FF."),
    COMMAND_PAY_RECEIVER("&f%player% #99E0FFjust sent you #F8F327%amount%#99E0FF."),
    COMMAND_PAY_DISABLE("#ff0000You can’t send %name%."),
    COMMAND_PAY_SELF("#ff0000You cannot send money to yourself."),
    COMMAND_PAY_NOT_ENOUGH("#ff0000You don't have enough money."),

    JOIN_MESSAGE("#99E0FF%player% &7joined the game"),
    QUIT_MESSAGE("#99E0FF%player% &7left the game"),
    FIRST_JOIN_MESSAGE("&7Welcome #99E0FF%player% &7on <your server name>! &8(&f%totalUserFormat% &7registered players&8)"),
    FIRST_JOIN_MOTD(
            "",
            "#00FF00✔ #99E0FFWelcome %player%#99E0FF !",
            "",
            "#858ef8Discord&8: #99E0FF<hover:show_text:'&fJoin the server #858ef8Discord &f!'><click:open_url:'https://discord.groupez.dev/'>https://discord.groupez.dev/</click></hover>",
            "&7Website&8: #99E0FF<hover:show_text:'&fOpen the Website &f!'><click:open_url:'https://minecraft-inventory-builder.com/'>https://minecraft-inventory-builder.com/</click></hover>",
            ""
    ),

    COMMAND_SET_SPAWN("&fYou just set the spawn location."),
    COMMAND_SPAWN_NOT_DEFINE("&cThe spawn does not exist. Please contact an administrator."),

    COMMAND_WARP_ALREADY_EXIST("#ff0000Warp &f%name% #ff0000already exists. &7Use &n<hover:show_text:'&fUse this command'><click:suggest_command:'/setwarp %name% true'>/setwarp %name% true</click></hover>&r command to modify the warp"),
    COMMAND_WARP_DOESNT_EXIST("#ff0000Warp &f%name% #ff0000 does not exist."),
    COMMAND_WARP_NO_PERMISSION("#ff0000You do not have permission to use the warp &f%name%#ff0000."),
    COMMAND_WARP_CREATE(
            "#00ff00You just created the warp &f%name% #00ff00to your position.",
            "&7Warp Permission is: <hover:show_text:'&fCopy command to add permission to a player'><click:SUGGEST_COMMAND:'/lp user <username> permission set nessentials.warp.%name%'>&f&nessentials.warp.%name%</click></hover>"
            ),
    COMMAND_WARP_USE(
            "&cUsage&8: &f/warp <destination>",
            "&7Warps&8:&f%destinations%"
    ),
    COMMAND_WARP_DESTINATION(" <hover:show_text:'&fClick to teleport to warp &n%name%'><click:run_command:'/warp %name%'>&f%name%</click></hover>&7"),
    COMMAND_WARP_LIST("&7Warps&8:&f%destinations%"),
    COMMAND_WARP_LIST_INFO(" <hover:show_text:'&fClick to teleport to warp &n%name%'><click:run_command:'/warp %name%'>&f%name%</click></hover>&7"),
    COMMAND_WARP_DELETE("#00ff00You just removed the warp &f%name%#00ff00."),

    COMMAND_RANDOM_TP_ERROR("#ff0000No safe location found after multiple attempts, please try again."),

    COMMAND_SET_HOME_INVALIDE_NAME("&f%name% #ff0000is not a valid name, please choose another one."),
    COMMAND_SET_HOME_TOO_LONG("&f%name% #ff0000is too long, please choose another one."),
    COMMAND_SET_HOME_TOO_SHORT("&f%name% #ff0000is too short name, please choose another one."),
    COMMAND_SET_HOME_MAX("#ff0000You cannot have more than &f%max%#ff0000 homes."),
    COMMAND_SET_HOME_CREATE(
            "",
            "#99E0FFYou just created the home &f%name%#99E0FF. &8(&7%current%&8/&7%max%&8)",
            "&fUse &n<hover:show_text:'&fClick to teleport to home'><click:suggest_command:'/home %name%'>/home %name%</click></hover>&r command to teleport to it",
            ""
    ),

    COMMAND_HOME_DOESNT_EXIST("#ff0000The home &f%name%#ff0000 does not exist."),

    COMMAND_HOME_INFORMATION_MULTI_LINE_HEADER(
            "",
            "#8cc0ccʏᴏᴜʀ ʜᴏᴍᴇs &a♦ &7(%count%) &8- &7(Max: %max%)"
    ),
    COMMAND_HOME_INFORMATION_MULTI_LINE_CONTENT(MessageType.WITHOUT_PREFIX,
            " #8cc0cc♢ &f%name% &7in %world% (%environment%) &a<hover:show_text:'&7Click to teleport to home &f&n%name%'><click:run_command:'/home %name%'>[CLICK]</click></hover>"),
    COMMAND_HOME_INFORMATION_MULTI_LINE_FOOTER(MessageType.WITHOUT_PREFIX, ""),
    COMMAND_HOME_INFORMATION_IN_LINE(MessageType.WITHOUT_PREFIX, "#8cc0ccʏᴏᴜʀ ʜᴏᴍᴇs &a♦ &7(%count%/%max%)&8:&f%homes%"),
    COMMAND_HOME_INFORMATION_IN_LINE_INFO(" <hover:show_text:'&7Click to teleport to home &f&n%name%'><click:run_command:'/home %name%'>&f%name%</click></hover>&7"),
    COMMAND_HOME_ICON_ERROR("#ff0000You must have an item in your hand to change the icon of your home."),
    COMMAND_HOME_ICON_SUCCESS("#00ff00You just changed the home icon &f%name%#00ff00."),
    COMMAND_HOME_ICON_RESET("#00ff00You just reset the home icon &f%name%#00ff00."),
    COMMAND_HOME_DELETE("#99E0FFYou just deleted the home &f%name%#99E0FF."),
    COMMAND_HOME_ADMIN_DELETE("#00ff00You just deleted home &f%name%#00ff00 of &b%player%#00ff00."),
    COMMAND_HOME_ADMIN_SET("#00ff00You just created home &f%name%#00ff00 of &b%player%#00ff00."),
    COMMAND_HOME_ADMIN_LIST("#8cc0cc%player% ʜᴏᴍᴇs&8:&f%homes%"),
    COMMAND_HOME_ADMIN_LIST_INFO(" <hover:show_text:'&7Click to teleport to home &f&n%name%'><click:run_command:'/home %player%:%name%'>&f%name%</click></hover>&7"),

    COMMAND_KICK_NOTIFY("&f%player% &cjust kicked out the player &7&n%target%&r &cfor the reason &e%reason%"),
    COMMAND_KICK_RECEIVER(
            "",
            "&cYou have just been kicked from the server for the reason:",
            "%reason%",
            "",
            "&fMinecraft-Inventory-Builder.com",
            ""
    )

    ;

    private String message;
    private List<String> messages;
    private MessageType messageType = MessageType.TCHAT;

    Message(String message) {
        this.message = message;
        this.messages = new ArrayList<>();
    }

    Message(MessageType messageType, String message) {
        this.message = message;
        this.messages = new ArrayList<>();
        this.messageType = messageType;
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
