package fr.maxlego08.essentials.api.messages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.messages.messages.ClassicMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Message {

    // Rework message for a better system

    PREFIX("<primary>zEssentials <secondary>• "),
    CONSOLE("Console"),
    EXPIRED("Expired"),
    PLAYER_NOT_FOUND("&f%player% <error>was not found."),
    MODULE_DISABLE("<error>The &f%name% <error>module is disabled. You cannot use this command."),
    RULES(
            "#ffd353ℹ sᴇʀᴠᴇʀ ʀᴜʟᴇs#3f3f3f:",
            "#3f3f3f• &7Respect all players and staff.",
            "#3f3f3f• &7No grieging or stealing.",
            "#3f3f3f• &7No cheating or hacking.",
            "#3f3f3f• &7Keep chat clean and respectful.",
            "#3f3f3f• &7No spamming or advertising."
    ),

    FORMAT_SECOND("second"),
    FORMAT_SECONDS("seconds"),

    FORMAT_MINUTE("minute"),
    FORMAT_MINUTES("minutes"),

    FORMAT_HOUR("hour"),
    FORMAT_HOURS("hours"),

    FORMAT_DAY("d"),
    FORMAT_DAYS("days"),

    COMMAND_SYNTAX_ERROR("<error>You must execute the command like this&7: <success>%syntax%"),
    COMMAND_NO_PERMISSION("<error>You do not have permission to run this command."),
    COMMAND_NO_CONSOLE("<error>Only one player can execute this command."),
    COMMAND_NO_ARG("<error>Impossible to find the command with its arguments."),
    COMMAND_SYNTAXE_HELP("&f%syntax% &7» &7%description%"),

    COMMAND_RELOAD("<success>You have just reloaded the configuration files."),
    COMMAND_RELOAD_MODULE("<success>You have just reloaded the configuration files of the module &f%module%<success>."),
    COMMAND_RELOAD_ERROR("&f%module% <error>was not found !"),
    COMMAND_ESSENTIALS("zEssentials, version %version%"),
    COMMAND_GAMEMODE("&fSet game mode&e %gamemode% &ffor &b%player%&f."),
    COMMAND_GAMEMODE_INVALID("<error>You need to specify a valid player."),
    COMMAND_DAY("&fYou have just brought &edaylight&f into the world <success>%world%&f."),
    COMMAND_NIGHT("&fYou have just brought &enightfall&f into the world <success>%world%&f."),
    COMMAND_SUN("&fYou have just brought the &esun&f into the world <success>%world%&f."),
    COMMAND_TOP("&7You've just been teleported to &etop&7."),
    COMMAND_BOTTOM("&7You've just been teleported to &ebottom&7."),
    COMMAND_BOTTOM_ERROR("<error>Unable to find a position to transport you safely."),
    COMMAND_TOP_ERROR("<error>Unable to find a position to transport you safely."),
    COMMAND_SPEED_INVALID("<error>You need to specify a valid player."),
    COMMAND_SPEED_FLY("&7You have just set your &nfly&r&7 speed to &f%speed%&7 for &f%player%&7. &8(&f2 &7by default&8)"),
    COMMAND_SPEED_WALK("&7You have just set your &nwalk&r&7 speed to &f%speed%&7 for &f%player%&7. &8(&f2 &7by default&8)"),
    COMMAND_SPEED_ERROR("<error>You must enter a number between &60<error> and &610<error>. &8(&f2 &7by default&8)"),

    COMMAND_FLY_ERROR_WORLD("<error>You can’t fly in this world."),
    COMMAND_FLY_ENABLE("&7Flight mode <success>enable &7for &f%player%<success>."),
    COMMAND_FLY_ENABLE_SECONDS("&7Flight mode <success>enable &7for &f%player%<success>. &8(&7time remaining %time%&8)"),
    COMMAND_FLY_DISABLE("&7Flight mode <error>disable &7for &f%player%<success>."),
    COMMAND_FLY_ERROR("<error>You have no more fly time available."),
    COMMAND_FLY_END("<error>You have used up your entire flight time, you can no longer fly"),
    COMMAND_FLY_ERROR_OTHER("<error>%player% has no more flight time available."),
    COMMAND_FLY_ADD("<success>You just added %time% <success>of fly time to &f%player%<success>."),
    COMMAND_FLY_GET("<success>%player%&8: &f%player%<success>."),
    COMMAND_FLY_INFO_EMPTY("<error>You do not have time to fly available."),
    COMMAND_FLY_INFO("<success>You have &f%time%<success>."),
    COMMAND_FLY_REMOVE("<success>You just removed %time% <success>of fly time to &f%player%<success>."),
    COMMAND_FLY_SET("<success>You just set %time% <success>of fly time to &f%player%<success>."),
    COMMAND_GOD_ENABLE("&7God mode <success>enable &7for &f%player%<success>."),
    COMMAND_GOD_DISABLE("&7God mode <error>disable &7for &f%player%<success>."),
    COMMAND_NIGHT_VISION_ENABLE("&7Night vision <success>enable &7for &f%player%<success>."),
    COMMAND_NIGHT_VISION_DISABLE("&7Night vision <error>disable &7for &f%player%<success>."),
    COMMAND_HEAL_SENDER("&7You just healed the player &f%player%&7."),
    COMMAND_HEAL_RECEIVER("<success>You have been healed."),
    COMMAND_HEAL_ERROR("<error>You cannot heal someone who is dead !"),
    COMMAND_FEED_SENDER("&7You just feed the player &f%player%&7."),
    COMMAND_FEED_RECEIVER("<success>You have been feed."),
    COMMAND_FEED_ERROR("<error>You cannot feed someone who is dead !"),
    COMMAND_CLEARINVENTORY_SUCCESS("<success>You have cleared your inventory."),
    COMMAND_CLEARINVENTORY_SUCCESS_OTHER("<success>You have cleared the inventory of &f%player%<success>."),

    // Teleport Command
    COMMAND_TPA_ERROR("<error>You have already sent a request to #34cfe0%player%<error>."),
    COMMAND_TPA_ERROR_SAME("<error>You cannot teleport to yourself."),
    COMMAND_TPA_ERROR_OFFLINE("<error>Unable to find player, must be offline."),
    COMMAND_TPA_ERROR_TO_LATE_EMPTY("<error>You do not have a teleport request."),
    COMMAND_TPA_ERROR_TO_LATE_EXPIRE("<error>The teleport request has expired."),
    COMMAND_TPA_SENDER("&7You have just sent a teleport request to #34cfe0%player%&7."),
    COMMAND_TPA_RECEIVER(
            "&7You have just received a teleport request from #34cfe0%player%&7.",
            "&7You have <error>60 &6seconds&e to accept the teleport request.",
            "&7To accept the request do #0EEA93/tpaccept&7."
    ),
    COMMAND_TPA_ACCEPT_RECEIVER("<success>You have just accepted the teleport request from #34cfe0%player%<success>."),
    COMMAND_TPA_ACCEPT_SENDER("#34cfe0%player%<success> has just accepted your teleport request."),
    COMMAND_TELEPORT_IGNORE_PLAYER("<error>You cannot send a teleport request to #34cfe0%player%<error> they are ignoring you."),
    COMMAND_TELEPORT_WORLD("<error>You need to be in the same world to teleport."),
    COMMAND_TPA_ERROR_TO_LATE("<error>You do not have a teleport request."),
    COMMAND_TPA_ERROR_TO_LATE_2("<error>The request has expired."),
    COMMAND_TP_DENY_SENDER("Denied %player% teleport request."),
    COMMAND_TP_DENY_RECEIVER("%player% has denied your teleport request"),
    COMMAND_TP_CANCEL_ERROR("<error>You did not send a teleport request at &f%player%<error>."),
    COMMAND_TP_CANCEL_SENDER("<error>Cancelled #99E0FFyour teleport request to %player%."),
    COMMAND_TP_CANCEL_RECEIVER("&f%player% <error>cancelled their teleport request to you."),
    COMMAND_TP("&7You just teleport to the player #34cfe0%player%&f."),
    COMMAND_TP_LOCATION("&7You just teleport to #34cfe0%x%&f, #34cfe0%y%&f, #34cfe0%z%&f."),
    COMMAND_TP_LOCATION_OTHER("&7You just teleport #34cfe0%player% &fto #34cfe0%x%&f, #34cfe0%y%&f, #34cfe0%z%&f."),
    COMMAND_TP_ALL("&7You just teleported all the players onto you."),
    COMMAND_TP_SELF("&7You just teleported #34cfe0%player%&7 to your position."),
    COMMAND_BACK("&7Returning to previous location."),
    COMMAND_BACK_ERROR("<error>You have no last location. Impossible to go back."),

    COMMAND_MORE_ERROR("<error>You cannot make this order in item in hand."),
    COMMAND_MORE_SUCCESS("&7You just put your item to &f64&7."),
    COMMAND_WORLD_TELEPORT_SELF("<success>You have just been teleported into the world &f%world%<success>."),
    COMMAND_WORLD_TELEPORT_OTHER("<success>You just teleported the player &f%player% <success>in the world &f%world%<success>."),

    COMMAND_COMPACT_TYPE("<error>Impossible to compact the material &f%material%<error>."),
    COMMAND_COMPACT_ERROR("<error>You have no &f%item%<error> in your inventory."),
    COMMAND_COMPACT_SUCCESS("&7You have just transformed #0EEA93x%amount% #34cfe0%item% &7into #0EEA93x%toAmount% #34cfe0%toItem%&7."),
    COMMAND_COMPACT_SUCCESS_ALL("&7You just converted all your items."),
    COMMAND_HAT_SUCCESS("&7You just put #0ef0ce%item% &7on your head."),
    COMMAND_HAT_ERROR("<error>You cannot put air on your head."),
    COMMAND_PLAYER_WEATHER_RESET("&7You just changed the weather to that of the server."),
    COMMAND_PLAYER_WEATHER_DOWNFALL("&7You just put the rain on for yourself."),
    COMMAND_PLAYER_TIME_RESET("&7You just changed the time to that of the server."),
    COMMAND_PLAYER_TIME_CHANGE("&7You’re here to change your time."),

    DESCRIPTION_RELOAD("Reload configuration files"),
    DESCRIPTION_DELETE_WORLD("Removes data being linked to a world"),
    DESCRIPTION_GAMEMODE("Change player gamemode"),
    DESCRIPTION_GAMEMODE_CREATIVE("Change player gamemode to creative"),
    DESCRIPTION_GAMEMODE_SURVIVAL("Change player gamemode to survival"),
    DESCRIPTION_GAMEMODE_ADVENTURE("Change player gamemode to adventure"),
    DESCRIPTION_GAMEMODE_SPECTATOR("Change player gamemode to spectator"),
    DESCRIPTION_DAY("Set the day in your world"),
    DESCRIPTION_NIGHT("Set the night in your world"),
    DESCRIPTION_GOD("Toggle god mode"),
    DESCRIPTION_FLY("Toggle flight"),
    DESCRIPTION_FLY_ADD("Added the fly time"),
    DESCRIPTION_FLY_GET("Display player's fly info"),
    DESCRIPTION_FLY_INFO("Display your temp fly info"),
    DESCRIPTION_FLY_REMOVE("Removed the fly time"),
    DESCRIPTION_FLY_SET("Set the fly time"),
    DESCRIPTION_HEAL("Heal a player"),
    DESCRIPTION_SUN("Set the sun in your world"),
    DESCRIPTION_ENDERCHEST("Open your enderchest"),
    DESCRIPTION_ENDERSEE("Open a player enderchest"),
    DESCRIPTION_TOP("Teleporting to top"),
    DESCRIPTION_BOTTOM("Teleporting to top"),
    DESCRIPTION_SPEED("Change player speed"),
    DESCRIPTION_TPA("Teleport to a player"),
    DESCRIPTION_TP("Teleport to a player"),
    DESCRIPTION_TP_ALL("Teleport all player to your position"),
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
    DESCRIPTION_CLEARINVENTORY("Clear player's inventory"),
    DESCRIPTION_COMPACT("Compact the items in your hand"),
    DESCRIPTION_HAT("Create your custom hat !"),
    DESCRIPTION_PLAYER_WEATHER("Change your weather"),
    DESCRIPTION_PLAYER_TIME("Change your time"),
    DESCRIPTION_MONEY("Show your money"),
    DESCRIPTION_ECO("Manages the server economies"),
    DESCRIPTION_ECO_SET("Sets the specified player's balance to the specified amount of money"),
    DESCRIPTION_ECO_TAKE("Takes the specified amount of money from the specified player"),
    DESCRIPTION_ECO_GIVE("Gives the specified player the specified amount of money"),
    DESCRIPTION_ECO_GIVE_RANDOM("Gives the specified player a random amount of money"),
    DESCRIPTION_ECO_GIVE_ALL("Gives for all players the specified amount of money"),
    DESCRIPTION_ECO_RESET("Resets the specified player's balance to the server's starting balance"),
    DESCRIPTION_ECO_SHOW("Show player money"),
    DESCRIPTION_PAY("Pays another player from your balance"),
    DESCRIPTION_PAY_TOGGLE("Activate or not the receipt of money"),
    DESCRIPTION_SET_SPAWN("Set server spawn"),
    DESCRIPTION_SPAWN("Teleport to spawn"),
    DESCRIPTION_WARP_SET("Create a warp"),
    DESCRIPTION_WARP_USE("Teleport to a warp"),
    DESCRIPTION_WARP_DEL("Delete a warp"),
    DESCRIPTION_WARP_LIST("Show warp list"),
    DESCRIPTION_SET_HOME("Create a home"),
    DESCRIPTION_SET_HOME_CONFIRM("Confirm the creation of a home"),
    DESCRIPTION_DEL_HOME("Delete a home"),
    DESCRIPTION_DEL_HOME_CONFIRM("Confirm the deletion of a home"),
    DESCRIPTION_HOME("Teleport to a home"),
    DESCRIPTION_KICK("Kick a player"),
    DESCRIPTION_FREEZE("Freeze a player"),
    DESCRIPTION_KICK_ALL("Kick all players"),
    DESCRIPTION_KITTY_CANNON("Launch kitty, wtf you want to do that ? monster"),
    DESCRIPTION_BAN("Ban a player"),
    DESCRIPTION_MUTE("Mute a player"),
    DESCRIPTION_UN_MUTE("Unmute a player"),
    DESCRIPTION_UN_BAN("Unban a player"),
    DESCRIPTION_SANCTION("Open sanction inventory"),
    DESCRIPTION_CHAT_HISTORY("Show player's chat messages"),
    DESCRIPTION_CHAT_CLEAR("Clear chat"),
    DESCRIPTION_CHAT_DISABLE("Disable chat"),
    DESCRIPTION_CHAT_ENABLE("Enable chat"),
    DESCRIPTION_CHAT_BROADCAST("Broadcast a message"),
    DESCRIPTION_MESSAGE("Send a private message to a player"),
    DESCRIPTION_REPLY("Reply to a private message"),
    DESCRIPTION_MESSAGE_TOGGLE("Toggle private message"),
    DESCRIPTION_POWER_TOOLS_TOGGLE("Toggle power tools"),
    DESCRIPTION_SOCIALSPY("Display private messages of players"),
    DESCRIPTION_COMPACT_ALL("Compact items in your inventories"),
    DESCRIPTION_FURNACE("Smelt all the items in your hand"),
    DESCRIPTION_SKULL("Gets the head of a player"),
    DESCRIPTION_REPAIR("Fix the item in your hand"),
    DESCRIPTION_REPAIR_ALL("Repair all items in your inventory"),
    DESCRIPTION_EXT("Stop burning"),
    DESCRIPTION_NEAR("Show players close to you"),
    DESCRIPTION_PLAY_TIME("Show player's playtime"),
    DESCRIPTION_VERSION("Show plugin version"),
    DESCRIPTION_KILL_ALL("Kill entities"),
    DESCRIPTION_SEEN("Show player informations"),
    DESCRIPTION_SEEN_IP("Show players who have the same IP"),
    DESCRIPTION_KIT("Get a kit"),
    DESCRIPTION_KIT_SHOW("Show a kit"),
    DESCRIPTION_KIT_EDITOR("Edit a kit"),
    DESCRIPTION_KIT_CREATE("Create a new kit"),
    DESCRIPTION_KIT_DELETE("Delete a kit"),
    DESCRIPTION_COOLDOWN("Display a player’s cooldowns"),
    DESCRIPTION_COOLDOWN_DELETE("Delete a cooldown"),
    DESCRIPTION_COOLDOWN_CREATE("Create a cooldown"),
    DESCRIPTION_ITEM_NAME("Change the item name"),
    DESCRIPTION_ITEM_LORE("Show commands for the lore"),
    DESCRIPTION_ITEM_LORE_SET("Define a line of a lore"),
    DESCRIPTION_ITEM_LORE_ADD("Add a line to lore"),
    DESCRIPTION_ITEM_LORE_CLEAR("Clear item lore"),
    DESCRIPTION_GIVE("Give items"),
    DESCRIPTION_GIVE_ALL("Give items to online players"),
    DESCRIPTION_POWER_TOOLS("Add command to your items"),
    DESCRIPTION_MAIL("Open mailbox"),
    DESCRIPTION_MAIL_OPEN("Open player's mailbox"),
    DESCRIPTION_MAIL_GIVE("Give an item to player's mailbox"),
    DESCRIPTION_MAIL_GIVEALL("Give an items to players mailbox"),
    DESCRIPTION_RULES("Read server rules"),
    DESCRIPTION_SUICIDE("Kill yourself"),
    DESCRIPTION_HOLOGRAM("Show hologram commands"),
    DESCRIPTION_HOLOGRAM_CREATE("Create a hologram"),
    DESCRIPTION_HOLOGRAM_DELETE("Delete a hologram"),
    DESCRIPTION_HOLOGRAM_EDIT("Edit a hologram"),
    DESCRIPTION_HOLOGRAM_LIST("Display hologram list"),
    DESCRIPTION_HOLOGRAM_REMOVE_LINE("Remove a line from a hologram"),
    DESCRIPTION_HOLOGRAM_ADD_LINE("Add a line from a hologram"),
    DESCRIPTION_HOLOGRAM_SET_LINE("Define a line from a hologram"),
    DESCRIPTION_HOLOGRAM_INSERT_BEFORE_LINE("Insert a line before another"),
    DESCRIPTION_HOLOGRAM_INSERT_AFTER_LINE("Define a line after another"),
    DESCRIPTION_HOLOGRAM_MOVE_HERE("Move a hologram to your position"),
    DESCRIPTION_HOLOGRAM_MOVE_TO("Move a hologram to a location"),
    DESCRIPTION_HOLOGRAM_SCALE("Change hologram scale"),
    DESCRIPTION_HOLOGRAM_TRANSLATION("Change hologram translation"),
    DESCRIPTION_HOLOGRAM_BILLBOARD("Change hologram billboard"),
    DESCRIPTION_HOLOGRAM_TEXT_ALIGNMENT("Change hologram text alignment"),
    DESCRIPTION_HOLOGRAM_TEXT_SHADOW("Change hologram text shadow"),
    DESCRIPTION_HOLOGRAM_SHADOW_STRENGTH("Change hologram shadow strength"),
    DESCRIPTION_HOLOGRAM_SHADOW_RADIUS("Change hologram shadow radius"),
    DESCRIPTION_HOLOGRAM_TEXT_BACKGROUND("Change hologram text background"),
    DESCRIPTION_HOLOGRAM_SEE_THROUGH("Change hologram see through"),
    DESCRIPTION_HOLOGRAM_YAW("Change hologram yaw"),
    DESCRIPTION_HOLOGRAM_PITCH("Change hologram pitch"),
    DESCRIPTION_HOLOGRAM_TELEPORT("Teleport to a hologram"),
    DESCRIPTION_SCOREBOARD("Enable/Disable scoreboard"),
    DESCRIPTION_BALANCE_TOP("Show baltop"),
    DESCRIPTION_BALANCE_TOP_REFRESH("Refresh baltop"),
    DESCRIPTION_VOTEPARTY_INFORMATION("Show the vote party"),
    DESCRIPTION_VOTEPARTY_SET("Set the number of votes in the vote party"),
    DESCRIPTION_VOTEPARTY_ADD("Add in vote party"),
    DESCRIPTION_VOTEPARTY_REMOVE("Remove in vote party"),
    DESCRIPTION_VOTE_INFORMATION("Show information on how to vote"),
    DESCRIPTION_VOTE_SET("Set the number of votes for a player"),
    DESCRIPTION_VOTE_ADD("Add votes to a player"),
    DESCRIPTION_VOTE_REMOVE("Remove votes from a player"),
    DESCRIPTION_ENCHANT("Add enchant to an item"),
    DESCRIPTION_NIGHT_VISION("Enable or disable night vision"),
    DESCRIPTION_SUDO("Force a player to execute a command"),
    DESCRIPTION_WORLDEDIT_GIVE("Give a worldedit item to a player"),
    DESCRIPTION_WORLDEDIT_SET("Set all blocks of your selection"),
    DESCRIPTION_WORLDEDIT_WALLS("Set all blocks of your selection with a walls"),
    DESCRIPTION_WORLDEDIT_SPHERE("Set all blocks of your selection with a sphere"),
    DESCRIPTION_WORLDEDIT_FILL("Fill all blocks of your selection"),
    DESCRIPTION_WORLDEDIT_CYL("Fill all blocks of your selection with a cylinder"),
    DESCRIPTION_WORLDEDIT_CUT("Cut all blocks of your selection"),
    DESCRIPTION_WORLDEDIT_STOP("Stop the current edition"),
    DESCRIPTION_WORLDEDIT_CONFIRM("Confirm worldedit action"),
    DESCRIPTION_WORLDEDIT_POS1("Set first position of the selection"),
    DESCRIPTION_WORLDEDIT_POS2("Set second position of the selection"),
    DESCRIPTION_WORLDEDIT_OPTION("Configure your use of worldedit"),
    DESCRIPTION_WORLDEDIT_OPTION_INVENTORY("Use your inventory to take or add items"),
    DESCRIPTION_WORLDEDIT_OPTION_BOSSBAR("Enable or disable the worldedit progress bar"),
    DESCRIPTION_VAULT_GIVE("Give items to player's vault"),
    DESCRIPTION_VAULT_ADD_SLOT("Add slot to player's vault"),
    DESCRIPTION_VAULT_SET_SLOT("Set slot to player's vault"),
    DESCRIPTION_SHOW_ITEM("Show player's item"),
    DESCRIPTION_EXPERIENCE("Manage player experience"),

    YOU("you"),
    TRASH("&8Trash"),

    // Teleportation

    TELEPORT_MOVE("<error>You must not move!"),
    TELEPORT_MESSAGE(MessageType.ACTION, "&7Teleporting in #0EEA93%seconds% &7seconds, you must not move."),
    TELEPORT_SUCCESS(MessageType.ACTION, "#99E0FFYou have just teleported successfully!"),
    TELEPORT_MESSAGE_RANDOM(MessageType.ACTION, "&7Teleporting in #0EEA93%seconds% &7seconds, you must not move."),
    TELEPORT_SUCCESS_RANDOM(MessageType.ACTION, "#99E0FFYou have just teleported successfully!"),
    TELEPORT_MESSAGE_SPAWN(
            ClassicMessage.tchat("&7Teleporting in #0EEA93%seconds% &7seconds, you must not move."),
            ClassicMessage.action("&7Teleporting in #0EEA93%seconds% &7seconds, you must not move.")
    ),
    TELEPORT_MESSAGE_SPAWN_CONSOLE("<success>You just teleported the player <white>%player% <success>to spawn."),
    TELEPORT_SUCCESS_SPAWN(
            ClassicMessage.tchat("&7You just teleported to #0EEA93spawn &7!"),
            ClassicMessage.action("&7You just teleported to #0EEA93spawn &7!")
    ),
    TELEPORT_MESSAGE_WARP(
            ClassicMessage.tchat("&7Teleporting in #0EEA93%seconds% &7seconds, you must not move."),
            ClassicMessage.action("&7Teleporting in #0EEA93%seconds% &7seconds, you must not move.")
    ),
    TELEPORT_SUCCESS_WARP(
            ClassicMessage.tchat("&7You just teleported to warp #0EEA93%name% &7!"),
            ClassicMessage.action("&7You just teleported to warp #0EEA93%name% &7!")
    ),
    TELEPORT_MESSAGE_HOME(
            ClassicMessage.tchat("&7Teleporting in #0EEA93%seconds% &7seconds, you must not move."),
            ClassicMessage.action("&7Teleporting in #0EEA93%seconds% &7seconds, you must not move.")
    ),
    TELEPORT_SUCCESS_HOME(
            ClassicMessage.tchat("&7You just teleported to home #0EEA93%name% &7!"),
            ClassicMessage.action("&7You just teleported to home #0EEA93%name% &7!")
    ),

    TELEPORT_DAMAGE("<error>You must not take damage during teleportation."),
    TELEPORT_ERROR_LOCATION("<error>Unable to teleport you safely."),

    COOLDOWN("<error>✘ You must wait for &f%cooldown% <error>before performing this action."),
    COOLDOWN_COMMANDS("<error>✘ You must wait for &f%cooldown% <error>before performing this commands."),

    // Economy

    COMMAND_ECONOMY_NOT_FOUND("<error> Can’t find a economy with the name &f%name%<error>."),
    COMMAND_ECONOMY_GIVE_ALL_SENDER("#99E0FFYou just gave &f%economyFormat% #99E0FFto the online players."),
    COMMAND_ECONOMY_GIVE_SENDER("#99E0FFYou just gave &f%economyFormat% #99E0FFto the player &7%player%#99E0FF."),
    COMMAND_ECONOMY_GIVE_RECEIVER("#99E0FFYou have just received &f%economyFormat%."),
    COMMAND_ECONOMY_SET_SENDER("#99E0FFYou just set &f%economyFormat% #99E0FFto the player &7%player%#99E0FF."),
    COMMAND_ECONOMY_SET_RECEIVER("#99E0FFYou have just been modified &f%economyFormat%."),
    COMMAND_ECONOMY_TAKE_SENDER("#99E0FFYou just take &f%economyFormat% #99E0FFto the player &7%player%#99E0FF."),
    COMMAND_ECONOMY_TAKE_RECEIVER("#99E0FFYou have just lost &f%economyFormat%."),
    COMMAND_ECONOMY_SHOW_EMPTY("&f%player% <error>has no money."),
    COMMAND_ECONOMY_SHOW_INFO("&7- #99E0FF%economy% &f%amount%."),
    COMMAND_MONEY(
            "#99E0FFYou have&8:",
            " &7- #99E0FF%economy-name-money% &f%economy-money%.",
            " &7- #99E0FF%economy-name-coins% &f%economy-coins%."
    ),

    COMMAND_MONEY_OTHER(
            "#99E0FF%player% have&8:",
            " &7- #99E0FF%economy-name-money% &f%economy-money%.",
            " &7- #99E0FF%economy-name-coins% &f%economy-coins%."
    ),

    COMMAND_PAY_NEGATIVE("<error>Amount to pay must be positive."),
    COMMAND_PAY_MIN("<error>The minimum amount you can pay is &f%amount%<error>."),
    COMMAND_PAY_MAX("<error>The maximum amount you can pay is &f%amount%<error>."),
    COMMAND_PAY_SENDER("#99E0FFYou just sent #F8F327%amount%#99E0FF to the player &f%player%#99E0FF."),
    COMMAND_PAY_RECEIVER("&f%player% #99E0FFjust sent you #F8F327%amount%#99E0FF."),
    COMMAND_PAY_DISABLE("<error>You can’t send %name%."),
    COMMAND_PAY_SELF("<error>You cannot send money to yourself."),
    COMMAND_PAY_NOT_ENOUGH("<error>You don't have enough money."),

    JOIN_MESSAGE("#99E0FF%player% &7joined the game"),
    QUIT_MESSAGE("#99E0FF%player% &7left the game"),
    FIRST_JOIN_MESSAGE("&7Welcome #99E0FF%player% &7on <your server name>! &8(&f%totalUserFormat% &7registered players&8)"),
    FIRST_JOIN_MOTD(
            "",
            "<success>✔ #99E0FFWelcome %player%#99E0FF !",
            "",
            "#858ef8Discord&8: #99E0FF<hover:show_text:'&fJoin the server #858ef8Discord &f!'><click:open_url:'https://discord.groupez.dev/'>https://discord.groupez.dev/</click></hover>",
            "&7Website&8: #99E0FF<hover:show_text:'&fOpen the Website &f!'><click:open_url:'https://minecraft-inventory-builder.com/'>https://minecraft-inventory-builder.com/</click></hover>",
            ""
    ),

    COMMAND_SET_SPAWN("&fYou just set the spawn location."),
    COMMAND_SPAWN_NOT_DEFINE("<error>The spawn does not exist. Please contact an administrator."),

    COMMAND_WARP_ALREADY_EXIST("<error>Warp &f%name% <error>already exists. &7Use &n<hover:show_text:'&fUse this command'><click:suggest_command:'/setwarp %name% true'>/setwarp %name% true</click></hover>&r command to modify the warp"),
    COMMAND_WARP_DOESNT_EXIST("<error>Warp &f%name% <error> does not exist."),
    COMMAND_WARP_NO_PERMISSION("<error>You do not have permission to use the warp &f%name%<error>."),
    COMMAND_WARP_CREATE(
            "<success>You just created the warp &f%name% <success>to your position.",
            "&7Warp Permission is: <hover:show_text:'&fCopy command to add permission to a player'><click:SUGGEST_COMMAND:'/lp user <username> permission set essentials.warp.%name%'>&f&nessentials.warp.%name%</click></hover>"
    ),
    COMMAND_WARP_USE(
            "<error>Usage&8: &f/warp <destination>",
            "&7Warps&8:&f%destinations%"
    ),
    COMMAND_WARP_EMPTY("<error>No warp defined, use command &f/setwarp <name>"),
    COMMAND_WARP_DESTINATION(" <hover:show_text:'&fClick to teleport to warp &n%name%'><click:run_command:'/warp %name%'>&f%name%</click></hover>&7"),
    COMMAND_WARP_LIST("&7Warps&8:&f%destinations%"),
    COMMAND_WARP_LIST_INFO(" <hover:show_text:'&fClick to teleport to warp &n%name%'><click:run_command:'/warp %name%'>&f%name%</click></hover>&7"),
    COMMAND_WARP_DELETE("<success>You just removed the warp &f%name%<success>."),

    COMMAND_RANDOM_TP_ERROR("<error>No safe location found after multiple attempts, please try again."),

    COMMAND_SET_HOME_INVALIDE_NAME("&f%name% <error>is not a valid name, please choose another one."),
    COMMAND_SET_HOME_TOO_LONG("&f%name% <error>is too long, please choose another one."),
    COMMAND_SET_HOME_TOO_SHORT("&f%name% <error>is too short name, please choose another one."),
    COMMAND_SET_HOME_MAX("<error>You cannot have more than &f%max%<error> homes."),
    COMMAND_SET_HOME_WORLD("<error>You cannot create a home here"),
    COMMAND_SET_HOME_CREATE(
            "",
            "#99E0FFYou just created the home &f%name%#99E0FF. &8(&7%current%&8/&7%max%&8)",
            "&fUse &n<hover:show_text:'&fClick to teleport to home'><click:suggest_command:'/home %name%'>/home %name%</click></hover>&r command to teleport to it",
            ""
    ),
    COMMAND_SET_HOME_CREATE_CONFIRM(
            "",
            "#99E0FFThe home &f%name%#99E0FF. already exists. Do you want to overwrite it?",
            "<success><hover:show_text:'&fClick to overwrite the home %name%'><click:suggest_command:'/sethomeconfirm %name%'>ᴄᴏɴғɪʀᴍ</click></hover>",
            ""
    ),

    COMMAND_HOME_DOESNT_EXIST("<error>The home &f%name%<error> does not exist."),

    COMMAND_HOME_INFORMATION_MULTI_LINE_HEADER(
            "",
            "#8cc0ccʏᴏᴜʀ ʜᴏᴍᴇs <success>♦ &7(%count%) &8- &7(Max: %max%)"
    ),
    COMMAND_HOME_INFORMATION_MULTI_LINE_CONTENT(MessageType.WITHOUT_PREFIX,
            " #8cc0cc♢ &f%name% &7in %world% (%environment%) <success><hover:show_text:'&7Click to teleport to home &f&n%name%'><click:run_command:'/home %name%'>[CLICK]</click></hover>"),
    COMMAND_HOME_INFORMATION_MULTI_LINE_FOOTER(MessageType.WITHOUT_PREFIX, ""),
    COMMAND_HOME_INFORMATION_IN_LINE(MessageType.WITHOUT_PREFIX, "#8cc0ccʏᴏᴜʀ ʜᴏᴍᴇs <success>♦ &7(%count%/%max%)&8:&f%homes%"),
    COMMAND_HOME_INFORMATION_IN_LINE_INFO(" <hover:show_text:'&7Click to teleport to home &f&n%name%'><click:run_command:'/home %name%'>&f%name%</click></hover>&7"),
    COMMAND_HOME_ICON_ERROR("<error>You must have an item in your hand to change the icon of your home."),
    COMMAND_HOME_ICON_SUCCESS("<success>You just changed the home icon &f%name%<success>."),
    COMMAND_HOME_ICON_RESET("<success>You just reset the home icon &f%name%<success>."),
    COMMAND_HOME_DELETE("#99E0FFYou just deleted the home &f%name%#99E0FF."),
    COMMAND_HOME_DELETE_CONFIRM(
            "",
            "#99E0FFYou really want to delete the home &f%name%#99E0FF.",
            "<success><hover:show_text:'&fClick to overwrite the home %name%'><click:run_command:'/delhomeconfirm %name%'>ᴄᴏɴғɪʀᴍ</click></hover>",
            ""
    ),
    COMMAND_HOME_ADMIN_DELETE("<success>You just deleted home &f%name%<success> of &b%player%<success>."),
    COMMAND_HOME_ADMIN_SET("<success>You just created home &f%name%<success> of &b%player%<success>."),
    COMMAND_HOME_ADMIN_LIST("#8cc0cc%player% ʜᴏᴍᴇs&8:&f%homes%"),
    COMMAND_HOME_ADMIN_LIST_INFO(" <hover:show_text:'&7Click to teleport to home &f&n%name%'><click:run_command:'/home %player%:%name%'>&f%name%</click></hover>&7"),

    COMMAND_KICK_NOTIFY(MessageType.WITHOUT_PREFIX, "<hover:show_text:'&7By: <white>%sender%<newline>&7Duration: <white>%duration%<newline>&7Reason: <white>%reason%<newline>&7Date: <white>%created_at%'><click:run_command:'/sc %target%'>&8(#f59e07Sanction&8) #8aebeb%player% #e33414just kicked the player #e0d12d%target%#e33414.</click></hover>"),
    COMMAND_BAN_NOTIFY(MessageType.WITHOUT_PREFIX, "<hover:show_text:'&7By: <white>%sender%<newline>&7Duration: <white>%duration%<newline>&7Reason: <white>%reason%<newline>&7Date: <white>%created_at%<newline>&7Expires: <white>%expired_at%'><click:run_command:'/sc %target%'>&8(#f59e07Sanction&8) #8aebeb%player% #e33414just banned the player #e0d12d%target%#e33414.</click></hover>"),
    COMMAND_MUTE_NOTIFY(MessageType.WITHOUT_PREFIX, "<hover:show_text:'&7By: <white>%sender%<newline>&7Duration: <white>%duration%<newline>&7Reason: <white>%reason%<newline>&7Date: <white>%created_at%<newline>&7Expires: <white>%expired_at%'><click:run_command:'/sc %target%'>&8(#f59e07Sanction&8) #8aebeb%player% #e33414just muted the player #e0d12d%target%#e33414.</click></hover>"),
    COMMAND_UNMUTE_NOTIFY(MessageType.WITHOUT_PREFIX, "<hover:show_text:'&7By: <white>%sender%<newline>&7Duration: <white>%duration%<newline>&7Reason: <white>%reason%<newline>&7Date: <white>%created_at%'><click:run_command:'/sc %target%'>&8(#f59e07Sanction&8) #8aebeb%player% #e33414just unmuted the player #e0d12d%target%#e33414.</click></hover>"),
    COMMAND_UNBAN_NOTIFY(MessageType.WITHOUT_PREFIX, "<hover:show_text:'&7By: <white>%sender%<newline>&7Duration: <white>%duration%<newline>&7Reason: <white>%reason%<newline>&7Date: <white>%created_at%'><click:run_command:'/sc %target%'>&8(#f59e07Sanction&8) #8aebeb%player% #e33414just unbanned the player #e0d12d%target%#e33414.</click></hover>"),
    COMMAND_BAN_ERROR_DURATION("<error>The duration of a banishment must be at least 1 second."),
    COMMAND_MUTE_ERROR_DURATION("<error>The duration of a mute must be at least 1 second."),
    COMMAND_UN_MUTE_ERROR("&f%player% <error>is not mute."),
    COMMAND_UN_BAN_ERROR("&f%player% <error>is not ban."),
    COMMAND_SANCTION_ERROR("<error>You can’t sanction that player. He’s protected."),

    MESSAGE_KICK(
            "",
            "<error>You have just been kicked from the server for the reason:",
            "%reason%",
            "",
            "&fMinecraft-Inventory-Builder.com",
            ""
    ),

    COMMAND_FREEZE_SUCCESS("&7You just freeze the player &f%player%&7."),
    COMMAND_UN_FREEZE_SUCCESS("&7You just unfreeze the player &f%player%&7."),

    MESSAGE_FREEZE(
            "",
            "&cYou have just been frozen.",
            ""
    ),

    MESSAGE_UN_FREEZE(
            "",
            "&aYou have just been unfrozen.",
            ""
    ),

    MESSAGE_MUTE(
            "",
            "&fYou have just lost your <u>voice</u>.",
            "&fDuration&8: <gradient:#7ae856:#a1d909>%duration%</gradient>",
            "&fReason&8: #82d1ff%reason%",
            ""
    ),

    MESSAGE_UNMUTE(
            "",
            "&fYou have just regained your <u>voice</u>, congratulations !",
            "&fReason&8: #82d1ff%reason%",
            ""
    ),

    MESSAGE_MUTE_TALK(
            "",
            "&fYou do not have the right to speak.",
            "",
            "&fDuration&8: <gradient:#7ae856:#a1d909>%duration%</gradient>",
            "&fReason&8: #82d1ff%reason%",
            ""
    ),

    MESSAGE_BAN(
            "",
            "<error>You have just been ban from the server for the reason:",
            "&f%reason%",
            "&fDuration&8: &7%duration%",
            "",
            "&fMinecraft-Inventory-Builder.com",
            ""
    ),

    MESSAGE_BAN_JOIN(
            "",
            "<error>You are banned on server:",
            "&f%reason%",
            "&fRemaining time&8: &7%remaining%",
            "",
            "&fMinecraft-Inventory-Builder.com",
            ""
    ),

    // Chat
    CHAT_ERROR("<error>Impossible to let you speak, your data is not loaded."),
    CHAT_ALPHANUMERIC_REGEX("<error>You use a forbidden character."),
    CHAT_COOLDOWN("<error>Please wait before sending your next message. &8(&7%cooldown%&8)"),
    CHAT_SAME("<error>You can’t put the same sentence."),
    CHAT_CAPS("<error>You don’t have to scream to talk !"),
    CHAT_FLOOD("<error>You can’t talk like that."),
    CHAT_LINK("<error>You cannot send a link in the chat."),
    CHAT_DISABLE("<error>The chat is currently unavailable."),

    CHAT_DEFAULT_FORMAT("<white>%displayName% <#656665>• &7%message%"),
    CHAT_MESSAGES_EMPTY("&f%player% <error>did not write a message."),
    CHAT_MESSAGES_LINE(MessageType.WITHOUT_PREFIX, "&f%date% &7%message%"),
    CHAT_MESSAGES_FOOTER(MessageType.WITHOUT_PREFIX, "<click:run_command:'/chathistory %player% %previousPage%'><hover:show_text:'Previous Page'><white>⬅</hover></click> &7%page%/%maxPage% <click:run_command:'/chathistory %player% %nextPage%'><hover:show_text:'Next Page'><white>➡</hover></click>"),

    COMMAND_CHAT_CLEAR("<success>The chat just got clear."),
    COMMAND_CHAT_DISABLE_ERROR("<error>Chat is already disable."),
    COMMAND_CHAT_DISABLE_SUCCESS("<success>You just deactivated the chat."),
    COMMAND_CHAT_ENABLE_ERROR("<error>Chat is already enable."),
    COMMAND_CHAT_ENABLE_SUCCESS("<success>You just activated the chat."),
    COMMAND_CHAT_BROADCAST(MessageType.WITHOUT_PREFIX, "<primary>ʙʀᴏᴀᴅᴄᴀsᴛ <secondary>• <white>%message%"),

    COMMAND_MESSAGE_SELF("<error>You can’t talk to yourself."),
    COMMAND_MESSAGE_DISABLE("&f%%player% <error>has disabled private messages."),
    COMMAND_MESSAGE_MUTE("<error>You’re not allowed to speak."),
    COMMAND_MESSAGE_ME("<hover:show_text:'&fClick to reply'><click:suggest_command:'/r '>&8(#f533f5me &7-> #34cfe0%target%&8) #e6e1e6<message></click></hover>"),
    COMMAND_MESSAGE_OTHER("<hover:show_text:'&fClick to reply'><click:suggest_command:'/r '>&8(#34cfe0%target% &7-> #f533f5me&8) #e6e1e6<message></click></hover>"),
    COMMAND_MESSAGE_SOCIAL_SPY(MessageType.WITHOUT_PREFIX, "&8(&7SocialSpy&8) &8(<hover:show_text:'&fOpen sanction for %sender%'><click:run_command:'/sc %sender%'>#34cfe0%sender%</click></hover> &7-> <hover:show_text:'&fOpen sanction for %receiver%'><click:run_command:'/sc %receiver%'>#f533f5%receiver%</click></hover>&8) #e6e1e6%message%"),
    COMMAND_MESSAGE_ERROR("<error>You have no one to answer."),
    COMMAND_MESSAGE_TOGGLE_ENABLE("&7Private message <success>enable &7for &f%player%<success>."),
    COMMAND_MESSAGE_TOGGLE_DISABLE("&7Private message <error>disable &7for &f%player%<error>."),
    COMMAND_SOCIAL_SPY_ENABLE("&7Social spy <success>enable &7for &f%player%<success>."),
    COMMAND_SOCIAL_SPY_DISABLE("&7Social spy <error>disable &7for &f%player%<error>."),
    COMMAND_PAY_TOGGLE_ENABLE("&7Pay <success>enable &7for &f%player%<success>."),
    COMMAND_PAY_TOGGLE_DISABLE("<error>All of your power tools have been disabled &7for &f%player%<error>."),
    COMMAND_POWER_TOOLS_TOGGLE_ENABLE("<success>All of your power tools have been enabled &7for &f%player%<success>."),
    COMMAND_POWER_TOOLS_TOGGLE_DISABLE("&7Pay <error>disable &7for &f%player%<error>."),

    COMMAND_FURNACE_TYPE("<error>Impossible to smelt the material &f%material%<error>."),
    COMMAND_FURNACE_ERROR("<error>You have no &f%item%<error> in your inventory."),
    COMMAND_FURNACE_SUCCESS("&7You have just transformed #0EEA93x%amount% #34cfe0%item% &7into #0EEA93x%toAmount% #34cfe0%toItem%&7."),
    COMMAND_SKULL("<success>You just got the player’s head &f%name%<success>."),
    COMMAND_REPAIR_ERROR("<error>The item in your hand cannot be repaired."),
    COMMAND_REPAIR_SUCCESS("<success>You just fixed the item in your hand."),
    COMMAND_REPAIR_ALL_ERROR("<error>You have no items to repair."),
    COMMAND_REPAIR_ALL_SUCCESS("<success>You have just repaired all the items in your inventory"),
    COMMAND_EXT("&fBy the will of Maxlego08, you no longer burn."),

    COMMAND_NEAR_EMPTY("<error>No player near to you."),
    COMMAND_NEAR_PLAYER("&fPlayers near to you&7:%players%"),
    COMMAND_NEAR_INFO(" &7%player% &8(&e%distance%m&8)"),
    COMMAND_PLAY_TIME(
            "&7You played&8: <primary>%playtime%",
            "&7Current session&8: <primary>%playtime_session%"
    ),

    COMMAND_REMOVE("&7Removed <success>%amount% &7entities."),

    COMMAND_SEEN_OFFLINE(MessageType.WITHOUT_PREFIX, "#99E0FFPlayer &f%player% #99E0FFhas been <error>offline #99E0FFsince &7%date%#99E0FF."),
    COMMAND_SEEN_ONLINE(MessageType.WITHOUT_PREFIX, "#99E0FFPlayer &f%player% #99E0FFhas been <success>online #99E0FFsince %date%#99E0FF."),
    COMMAND_SEEN_PLAYTIME(MessageType.WITHOUT_PREFIX, "#99E0FFPlayTime&8: &f%playtime%"),
    COMMAND_SEEN_UUID(MessageType.WITHOUT_PREFIX, "#99E0FFUUID&8: &f%uuid%"),
    COMMAND_SEEN_IP(MessageType.WITHOUT_PREFIX, "#99E0FFIP Address&8:&f%ips%"),
    COMMAND_SEEN_LAST_LOCATION(MessageType.WITHOUT_PREFIX, "#99E0FFLast Location&8: &f%world%, %x%, %y%, %z%"),
    COMMAND_SEEN_FIRST_JOIN(MessageType.WITHOUT_PREFIX, "#99E0FFFirst Join&8:&f%created_at%"),

    COMMAND_SEEN_ADDRESS(MessageType.WITHOUT_PREFIX, " <click:run_command:'/seenip %ip%'><hover:show_text:'&fShow players who have the same address'>%ip%</hover></click>"),

    COMMAND_SEEN_IP_EMPTY("<error>Unable to find players with ip &f%ip%<error>."),
    COMMAND_SEEN_IP_LINE("#99E0FFPlayers with the ip &f%ip%&8:%players%"),
    COMMAND_SEEN_IP_INFO(" <white><click:run_command:'/seen %name%'><hover:show_text:'&fShow player's information'>%name%</hover></click>&8"),

    COMMAND_KIT_NO_PERMISSION("<error>You do not have permission to use the kit &f%kit%<error>."),
    COMMAND_KIT_NOT_FOUND("<error>Unable to find the kit &f%kit%<error>."),
    COMMAND_KIT_ALREADY_EXISTS("<error>The &f%kit% <error>kit already exists."),
    COMMAND_KIT_INFORMATION_IN_LINE_EMPTY(MessageType.WITHOUT_PREFIX, "#8cc0ccʏᴏᴜʀ ᴋɪᴛs <success>♦ &7(%count%/%max%)&8: <error>ɴᴏ ᴋɪᴛ ᴀᴠᴀɪʟᴀʙʟᴇ"),
    COMMAND_KIT_INFORMATION_IN_LINE(MessageType.WITHOUT_PREFIX, "#8cc0ccʏᴏᴜʀ ᴋɪᴛs&8:&f%kits%"),
    COMMAND_KIT_INFORMATION_IN_LINE_INFO_AVAILABLE(" <hover:show_text:'&7Click to get the kit &f&n%name%'><click:run_command:'/kit %name%'><#1fde65>%name%</click></hover>&7"),
    COMMAND_KIT_INFORMATION_IN_LINE_INFO_UNAVAILABLE(" <hover:show_text:'&7You must wait <u>%time%</u> before getting the kit &f&n%name%'><st><#de1f1f>%name%</st></hover>&7"),

    COMMAND_KIT_INFORMATION_MULTI_LINE_HEADER(
            "",
            "#8cc0ccʏᴏᴜʀ ᴋɪᴛs&8:"
    ),
    COMMAND_KIT_INFORMATION_MULTI_LINE_CONTENT_AVAILABLE(MessageType.WITHOUT_PREFIX, "<#8cc0cc>♢ <hover:show_text:'&7Click to get the kit &f&n%name%'><click:run_command:'/kit %name%'><#1fde65>%name% </click></hover>"),
    COMMAND_KIT_INFORMATION_MULTI_LINE_CONTENT_UNAVAILABLE(MessageType.WITHOUT_PREFIX, "<#8cc0cc>♢ <hover:show_text:'&7You must wait <u>%time%</u> before getting the kit &f&n%name%'><st><#de1f1f>%name%</st></hover> &8(&7%time%&8)"),
    COMMAND_KIT_INFORMATION_MULTI_LINE_FOOTER(MessageType.WITHOUT_PREFIX, ""),
    COMMAND_KIT_EDITOR_SAVE("<success>You have just modified the kit &f%kit%<success>."),
    COMMAND_KIT_CREATE("<success>You just created the kit &f%kit%<success>."),
    COMMAND_KIT_DELETE("<success>You just deleted the kit &f%kit%<success>."),

    COMMAND_COOLDOWN_HEADER("&fPlayer cooldown %player% &8(&7%amount%&8)"),
    COMMAND_COOLDOWN_EMPTY("&f%player% <error>has no cooldown."),
    COMMAND_COOLDOWN_LINE(MessageType.WITHOUT_PREFIX, "&f%key% <secondary>- &7%createdAt% <secondary>- <success>%timeLeft% <secondary>- <hover:show_text:'&fClick to remove the cooldown'><click:run_command:'/cooldown delete %player% %key%'>&8[<error>ᴅᴇʟᴇᴛᴇ&8]</click></hover>"),
    COMMAND_COOLDOWN_NOT_FOUND("&f%cooldown% <error>was not found."),
    COMMAND_COOLDOWN_DELETE("<success>You just removed the cooldown &f%cooldown% <success>from &n%player%&r<success>."),
    COMMAND_COOLDOWN_CREATE("<success>You just created the cooldown &f%key% <success>for &n%player%&r<success>."),

    COMMAND_ITEM_EMPTY("<error>You have no item in your hand."),
    COMMAND_ITEM_CLEAR("<success>You have cleared this item's name"),
    COMMAND_ITEM_SET("<success>You have renamed your held item to &f%name%<success>."),
    COMMAND_ITEM_LORE_ADD("<success>You just added the line &f%text%<success>."),
    COMMAND_ITEM_LORE_SET("<success>You just set line %line% to &f%text%<success>."),
    COMMAND_ITEM_LORE_CLEAR("<success>You just clear the item lore."),
    COMMAND_ITEM_LORE_SET_ERROR("<error>Can’t find the line &f%line%<error>."),
    COMMAND_GIVE_ERROR("<error>Can’t find the item &f%item%<error>."),
    COMMAND_GIVE("<success>You just gave &n&fx%amount% %item%&r <success>to player &f%player%<success>."),
    COMMAND_GIVE_VAULT("<success>You just gave &n&fx%amount% %item%&r <success>to &f%player%<success> vault's."),
    COMMAND_GIVE_ALL("<success>You just gave &n&fx%amount% %item%&r <success>to online player."),

    COMMAND_POWER_TOOL_ERROR_ITEM("<error>You have no item in your hand."),
    COMMAND_POWER_TOOL_ERROR_RESET("<error>This item has no recorded command."),
    COMMAND_POWER_TOOL_INFO("<success>You just put the command &f/%command%<success> on the item &f%item<success>."),
    COMMAND_POWER_TOOL_RESET("<success>You have just deleted the command on the item &f%item<success>."),

    MAILBOX_REMOVE_FULL("<error>You must have space in your inventory to retrieve an item."),
    MAILBOX_REMOVE_EXPIRE("<error>You can no longer retrieve this item, it has expired."),
    MAILBOX_ADD(MessageType.ACTION, "<success>An item has just been added to your mailbox &8(&f/mail&8)"),
    MAILBOX_GIVE_ERROR("<error>Can’t find the item &f%item%<error>."),
    MAILBOX_GIVE("<success>You just gave &n&fx%amount% %item%&r <success>to player &f%player%<success> mailbox."),
    MAILBOX_GIVE_ALL("<success>You just gave &n&fx%amount% %item%&r <success>to online player mailbox."),


    HOLOGRAM_CREATE_ERROR("<error>Hologram &f%name% <error>already exists."),
    HOLOGRAM_CREATE(MessageType.WITHOUT_PREFIX,
            "<success>You just created the hologram &f%name%<success>.",
            "&7Use &n<click:suggest_command:'/hologram edit %name%'>/hologram %command% %name%</click>&r&7 command to edit the hologram"
    ),
    HOLOGRAM_DELETE("<success>Hologram &f%name% <success>has been deleted."),
    HOLOGRAM_DOESNT_EXIST("<error>Hologram &f%name% <error>doesn't exists."),
    HOLOGRAM_IS_NOT_A_TEXT("<error>Hologram &f%name% <error>is not a text hologram."),
    HOLOGRAM_IS_NOT_A_BLOCK("<error>Hologram &f%name% <error>is not a block hologram."),
    HOLOGRAM_IS_NOT_A_ITEM("<error>Hologram &f%name% <error>is not a item hologram."),
    HOLOGRAM_ADD_LINE("<success>You just added a line to the hologram &f%name%<success>."),
    HOLOGRAM_SET_LINE("<success>You just set the line &f%line%<success> to the hologram &f%name%<success>."),
    HOLOGRAM_INSERT_BEFORE_LINE("<success>You just set a new line before the line &f%line%<success> to the hologram &f%name%<success>."),
    HOLOGRAM_INSERT_AFTER_LINE("<success>You just set a new line after the line &f%line%<success> to the hologram &f%name%<success>."),
    HOLOGRAM_LINE_DOESNT_EXIST("<error>The line &f%line% <error>does not exist for the hologram &f%name%<error>."),
    HOLOGRAM_REMOVE_LINE("<success>You just remove the line &f%line%<success> to the hologram &f%name%<success>."),
    HOLOGRAM_SCALE("<success>You just changed the scale of the hologram &f%name% <success>to &7%x%&8, &7%y%&8, &7%z%"),
    HOLOGRAM_TRANSLATION("<success>You just changed the translation of the hologram &f%name% <success>to &7%x%&8, &7%y%&8, &7%z%"),
    HOLOGRAM_MOVE_HERE("<success>You just teleported the hologram &f%name%<success> to your position."),
    HOLOGRAM_MOVE_TO("<success>You just teleported the hologram &f%name% <success>to &7%x%&8, &7%y%&8, &7%z%"),
    HOLOGRAM_BILLBOARD("<success>You just change the billboard of the hologram &f%name%<success> to &f%billboard%<success>."),
    HOLOGRAM_TEXT_ALIGNMENT("<success>You just change the text alignment of the hologram &f%name%<success> to &f%textAlignment%<success>."),
    HOLOGRAM_YAW("<success>You just change the yaw of the hologram &f%name%<success> to &f%yaw%<success>."),
    HOLOGRAM_PITCH("<success>You just change the pitch of the hologram &f%name%<success> to &f%pitch%<success>."),
    HOLOGRAM_TEXT_BACKGROUND("<success>You just change the background color of the hologram &f%name%<success> to &f%color%<success>."),
    HOLOGRAM_TEXT_BACKGROUND_ERROR("<error>Could not parse background color."),
    HOLOGRAM_TEXT_SHADOW("<success>You set the text shadow to &f%textshadow% <success>for the hologram &f%name%<success>."),
    HOLOGRAM_SEE_THROUGH("<success>You set the see through to &f%seethrough% <success>for the hologram &f%name%<success>."),
    HOLOGRAM_SHADOW_STRENGTH("<success>You set the shadow strength to &f%shadow% <success>for the hologram &f%name%<success>."),
    HOLOGRAM_SHADOW_RADIUS("<success>You set the shadow radius to &f%shadow% <success>for the hologram &f%name%<success>."),
    HOLOGRAM_VIEW_DISTANCE("<success>You set the view distance to &f%distance% <success>for the hologram &f%name%<success>."),
    HOLOGRAM_LIST_EMPTY("<error>There is no hologram, to create one please use the command &f/holo create <name>"),
    HOLOGRAM_LIST(MessageType.WITHOUT_PREFIX, "&f%name% &8- &7%world%: %x%, %y%, %z% &8- <hover:show_text:'&7Click to be teleported to this hologram'><click:run_command:'/holo tp %name%'><green>ᴛᴇʟᴇᴘᴏʀᴛ</click> </hover><hover:show_text:'<red>Click to delete this hologram'><click:suggest_command:'/holo delete %name%'><red>ᴅᴇʟᴇᴛᴇ</click></hover>"),
    HOLOGRAM_ITEM_ERROR("<error>You can't use air for this hologram."),
    HOLOGRAM_ITEM("<success>You just modified the item of the hologram &f%name%<success>."),
    HOLOGRAM_BLOCK("<success>You just modified the block of the hologram &f%name%<success> to &f%material%<success>."),

    SCOREBOARD_DISABLE("<error>You just disabled the scoreboard."),
    SCOREBOARD_ENABLE("<success>You just enabled the scoreboard."),

    COMMAND_BALTOP_ERROR("<error>Can’t find the economy &f%name%<error>."),
    COMMAND_BALTOP_HEADER(MessageType.WITHOUT_PREFIX, "#00f986ᴍᴏsᴛ ᴍᴏɴᴇʏ &8(&f%page%&8/&7%maxPage%&8) <click:run_command:'/baltop %previousPage%'>&f◀</click> &8- <click:run_command:'/baltop %nextPage%'>&f▶</click>"),
    COMMAND_BALTOP(MessageType.WITHOUT_PREFIX, "#00f986#%position% &f%name%&8: %amount%"),

    CODE_NOT_FOUND("<red>Cannot find the code."),

    COMMAND_VOTEPARTY_SET("<success>You just set the voteparty to &f%amount% <success>votes."),
    COMMAND_VOTEPARTY_ADD("<success>You just added &f%amount% <success>vote in the voteparty."),
    COMMAND_VOTEPARTY_REMOVE("<success>You have just removed &f%amount% <success>vote in the voteparty."),
    COMMAND_VOTEPARTY_INFORMATIONS("<green>VoteParty: <white>%amount%<dark_gray>/<yellow>%objective%"),

    COMMAND_VOTE_ADD("<success>You just added a vote to &f%player%<success>."),
    COMMAND_VOTE_ADD_ERROR("<error>The site &f%site% <error>doesn't exist !"),
    COMMAND_VOTE(MessageType.ACTION, "<success>You just voted and received a vote key."),

    VOTE_OFFLINE(
            "",
            "<success>You have voted <gold>%amount% times <success>while offline, use the command <yellow><click:run_command:'/mailbox'>/mailbox</click> <success>to get your items !",
            ""
    ),


    VAULT_LORE(
            "",
            "&fɪɴғᴏʀᴍᴀᴛɪᴏɴs",
            " #f8b175• &7Quantity&8: &e%quantity%",
            "",
            "&f<b>➥</b> #fc99f8Left Click &7for take 1",
            "&f<b>➥</b> #fc99f8Right Click &7for take 64",
            "&f<b>➥</b> #fc99f8Sneak + Right Click &7for take all"
    ),
    COMMAND_VAULT_NOT_FOUND("<error>Cannot find the vault &f%vaultId%<error>."),
    COMMAND_VAULT_NO_PERMISSION("<error>You do not have permission to view se vault."),
    COMMAND_VAULT_SET_SLOT("<success>You just defined the slot of &f%player% <success>to &7%slots%<success>."),
    COMMAND_VAULT_ADD_SLOT("<success>you've just added &7%amount% <success>slot to &f%player%<success>."),
    COMMAND_VAULT_CHANGE_ICON_ERROR("<error>You have an item in your hand."),
    COMMAND_VAULT_CHANGE_ICON_SUCCESS("<success>You just changed the icon of your vault."),
    COMMAND_VAULT_RESET_ICON("<success>You just reset the icon of your vault."),
    COMMAND_VAULT_RENAME_START("<success>Please write in the chat the new name of the vault."),
    COMMAND_VAULT_RENAME_SUCCESS("<success>You just set the vault name to &f%name%<success>."),
    COMMAND_VAULT_RENAME_ERROR("<error>You cannot use this name for your vault."),
    COMMAND_VAULT_RENAME_RESET("<success>You just reset the name of your vault."),

    COMMAND_ENCHANT_INVALID("<error>You need to specify a valid player."),
    COMMAND_ENCHANT_ERROR_ENCHANT("<error>Impossible to find the enchantment &f%name%<error>."),
    COMMAND_ENCHANT_ERROR_ITEM_SELF("<error>You must have an item in your hand."),
    COMMAND_ENCHANT_ERROR_ITEM_PLAYER("<error>%player% has no item in his hand."),
    COMMAND_ENCHANT_REMOVE_SELF("<success>The enchantment &f%enchant% <success>has been removed from your item in hand."),
    COMMAND_ENCHANT_REMOVE_PLAYER("<success>The enchantment &f%enchant% <success>has been removed from the item from &f%player%’s<succes> hand."),
    COMMAND_ENCHANT_SUCCESS_SELF("<success>The enchantment &f%enchant% <success>has been applied to your item in hand."),
    COMMAND_ENCHANT_SUCCESS_PLAYER("<success>The enchantment &f%enchant% <success>has been applied to the item from &f%player%’s<succes> hand."),

    COMMAND_SUDO_ERROR("<error>You cannot force the player to execute commands."),
    COMMAND_SUDO_COMMAND("<success>You just forced the player <white>%player% <success>to execute the command <white>%command%<success>."),
    COMMAND_SUDO_MESSAGE("<success>You just forced the player <white>%player% <success>to send the message <white>%message%<success>."),

    COMMAND_WORLDEDIT_GIVE_ERROR("<error>Unable to find item &f%name%<error>"),
    COMMAND_WORLDEDIT_GIVE_SENDER("<success>You just gave &f%item% <success>to the player &f%player%<success>."),
    COMMAND_WORLDEDIT_GIVE_RECEIVER("<success>you just received &f%item%<success>."),
    COMMAND_WORLDEDIT_MATERIAL_NOT_FOUND("<error>Unable to find the material &f%material%<error>."),
    COMMAND_WORLDEDIT_CONFIRM_PRICE(
            "",
            "<success>Price<dark_gray>: <white>%price%",
            "<success>Blocks<dark_gray>: <white>%materials%",
            "<success>Duration<dark_gray>: <white>%duration% <dark_gray>(<gray>%blocks% at %speed% block%s%/s)",
            "",
            "<#45ff55><hover:show_text:'&fClick to confirm action'><click:run_command:'/pw confirm'>ᴄʟɪᴄᴋ ʜᴇʀᴇ ᴛᴏ ᴄᴏɴғɪʀᴍ</click></hover><gray>"
    ),
    COMMAND_WORLDEDIT_CONFIRM_PRICE_CUT(
            "",
            "<success>Price<dark_gray>: <white>%price%",
            "<success>Duration<dark_gray>: <white>%duration% <dark_gray>(<gray>%blocks% at %speed% block%s%/s)",
            "",
            "<#45ff55><hover:show_text:'&fClick to confirm action'><click:run_command:'/pw confirm'>ᴄʟɪᴄᴋ ʜᴇʀᴇ ᴛᴏ ᴄᴏɴғɪʀᴍ</click></hover><gray>"
    ),
    COMMAND_WORLDEDIT_CONFIRM_MATERIAL("<hover:show_text:'<white>Amount<dark_gray>: <aqua>%amount%<newline><white>Total Price<dark_gray>: <aqua>%price%<newline><white>Price per block<dark_gray>: <aqua>%price-per-block%'><lang:%translation-key%></hover>"),
    COMMAND_WORLDEDIT_CONFIRM_ERROR("<error>You can’t do that now."),

    COMMAND_WORLDEDIT_STOP_EMPTY("<error>You have no current edition."),
    COMMAND_WORLDEDIT_STOP_ERROR("<error>You cannot stop editing currently."),
    COMMAND_WORLDEDIT_ERROR_ITEM("<error>You must have the worldedit tool in your hand."),
    COMMAND_WORLDEDIT_ERROR_MAX("<error>You can only use this tool, it has been used to the maximum."),
    COMMAND_WORLDEDIT_OPTION_INVENTORY_ENABLE("<success>You have just activated worldedit to use your inventory."),
    COMMAND_WORLDEDIT_OPTION_INVENTORY_DISABLE("<error>You just disabled worldedit to use your inventory."),
    COMMAND_WORLDEDIT_OPTION_BOSSBAR_ENABLE("<success>You have just activated worldedit progress bar."),
    COMMAND_WORLDEDIT_OPTION_BOSSBAR_DISABLE("<error>You just disabled worldedit worldedit progress bar."),

    WORLDEDIT_SELECTION_ERROR("<error>You must select two positions before you make this command."),
    WORLDEDIT_SELECTION_VOLUME("<error>You cannot change more than &f%blocks% <error>at the same time."),
    WORLDEDIT_SELECTION_DISTANCE("<error>The distance between your two points in your selection is too large. <dark_gray>(<gray>Maximum <white>%distance% <gray>blocks<dark_gray>)"),
    WORLDEDIT_ALREADY_RUNNING("<error>You already have an edit in progress, please wait until it is finished."),
    WORLDEDIT_NOT_ENOUGH_MONEY("<error>You don’t have enough money to make this edition."),
    WORLDEDIT_NOT_ENOUGH_ITEMS("<error>You don’t have the items in your inventory, you can’t do that."),
    WORLDEDIT_SELECTION_POS1("<success>You have just defined the first position."),
    WORLDEDIT_SELECTION_POS2("<success>You have just defined the second position."),
    WORLDEDIT_SPEED_ERROR("<error>You have no permission to set speed per second."),

    WORLDEDIT_START_CALCULATE_PRICE("&7Price calculation in progress, please wait..."),
    WORLDEDIT_START_CHECK_INVENTORY("&7Checking that you have enough items in your inventory, please wait..."),
    WORLDEDIT_START_RUNNING("&7Launch of current edition, please wait..."),
    WORLDEDIT_FINISH("<success>Edition completed !"),
    WORLDEDIT_REFUND(
            "<success>ʏᴏᴜ'ᴠᴇ ᴊᴜsᴛ ʙᴇᴇɴ ʀᴇғᴜɴᴅ",
            "",
            "<success>Price<dark_gray>: <white>%price%",
            "<success>Blocks<dark_gray>:%materials%"
    ),
    WORLDEDIT_REFUND_MATERIAL(" <white><hover:show_text:'<white>Amount<dark_gray>: <aqua>%amount%<newline><white>Total Price<dark_gray>: <aqua>%price%<newline><white>Price per block<dark_gray>: <aqua>%price-per-block%'><lang:%translation-key%></hover><gray>"),
    WORLDEDIT_REFUND_EMPTY("<error>No block to refund"),
    WORLDEDIT_BOSSBAR("#45ff45Time remaining<dark_gray>: <white>%time%"),

    EXPERIENCE_GRANTED("<success>You have just given <white>%amount% %type% <success>to <white>%player%<success>."),
    EXPERIENCE_SETTED("<success>You have just set <white>%amount% %type% <success>to <white>%player%<success>."),
    EXPERIENCE_QUERIED("<success><white>%player% <success>have <white>%amount% <success>%type%."),
    DESCRIPTION_EXPERIENCE_QUERY("Query player experience"),
    DESCRIPTION_EXPERIENCE_SET("Set player experience"),
    DESCRIPTION_EXPERIENCE_GRANT("Grant experience to player"),
    ;

    private EssentialsPlugin plugin;
    private List<EssentialsMessage> messages = new ArrayList<>();

    Message(String message) {
        this(MessageType.TCHAT, message);
    }

    Message(MessageType messageType, String message) {
        this.messages.add(new ClassicMessage(messageType, Collections.singletonList(message)));
    }

    Message(String... message) {
        this(MessageType.TCHAT, message);
    }

    Message(MessageType messageType, String... messages) {
        this.messages.add(new ClassicMessage(messageType, Arrays.asList(messages)));
    }

    Message(EssentialsMessage... essentialsMessages) {
        this.messages = Arrays.asList(essentialsMessages);
    }

    public static Message fromString(String string) {
        try {
            return valueOf(string);
        } catch (Exception ignored) {
            return null;
        }
    }

    public List<EssentialsMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<EssentialsMessage> messages) {
        this.messages = messages;
    }

    public String toConfigurationName() {
        return name().replace("_", "-").toLowerCase();
    }

    public String getMessageAsString() {
        String configurationName = this.toConfigurationName();
        if (this.messages.isEmpty()) {
            this.plugin.getLogger().severe(configurationName + " is empty ! Check your configuration");
            return "Error with " + configurationName + ", check your console";
        }
        EssentialsMessage essentialsMessage = this.messages.get(0);
        if (essentialsMessage instanceof ClassicMessage classicMessage) {

            if (classicMessage.messages().isEmpty()) {
                this.plugin.getLogger().severe(configurationName + " message is empty ! Check your configuration");
                return "Error with " + configurationName + ", check your console";
            }

            return classicMessage.messages().get(0);
        }

        this.plugin.getLogger().severe(configurationName + " is not a tchat message ! Check your configuration");
        return "Error with " + configurationName + ", check your console";
    }

    public void setPlugin(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    public List<String> getMessageAsStringList() {
        return this.messages.stream().filter(essentialsMessage -> essentialsMessage instanceof ClassicMessage).map(essentialsMessage -> (ClassicMessage) essentialsMessage).map(ClassicMessage::messages).flatMap(List::stream).toList();
    }
}