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
    PLAYER_NOT_FOUND("<c:#FFFFFF>%player% <error>was not found."),
    MODULE_DISABLE("<error>The <c:#FFFFFF>%name% <error>module is disabled. You cannot use this command."),
    RULES(
            "<c:#FFD353>ℹ sᴇʀᴠᴇʀ ʀᴜʟᴇs<c:#3F3F3F>:",
            "<c:#3F3F3F>• <c:#AAAAAA>Respect all players and staff.",
            "<c:#3F3F3F>• <c:#AAAAAA>No grieging or stealing.",
            "<c:#3F3F3F>• <c:#AAAAAA>No cheating or hacking.",
            "<c:#3F3F3F>• <c:#AAAAAA>Keep chat clean and respectful.",
            "<c:#3F3F3F>• <c:#AAAAAA>No spamming or advertising."
    ),

    FORMAT_SECOND(" second"),
    FORMAT_SECONDS(" seconds"),

    FORMAT_MINUTE(" minute"),
    FORMAT_MINUTES(" minutes"),

    FORMAT_HOUR(" hour"),
    FORMAT_HOURS(" hours"),

    FORMAT_DAY(" d"),
    FORMAT_DAYS(" days"),

    COMMAND_SYNTAX_ERROR("<error>You must execute the command like this<c:#AAAAAA>: <success>%syntax%"),
    COMMAND_NO_PERMISSION("<error>You do not have permission to run this command."),
    COMMAND_NO_CONSOLE("<error>Only one player can execute this command."),
    COMMAND_NO_ARG("<error>Impossible to find the command with its arguments."),
    COMMAND_RESTRICTED("<error>You cannot use this command here."),
    COMMAND_SYNTAXE_HELP("<c:#FFFFFF>%syntax% <c:#AAAAAA>» <c:#AAAAAA>%description%"),

    COMMAND_RELOAD("<success>You have just reloaded the configuration files."),
    COMMAND_RELOAD_MODULE("<success>You have just reloaded the configuration files of the module <c:#FFFFFF>%module%<success>."),
    COMMAND_RELOAD_ERROR("<c:#FFFFFF>%module% <error>was not found !"),
    COMMAND_ESSENTIALS("zEssentials, version %version%"),
    COMMAND_GAMEMODE("<c:#FFFFFF>Set game mode<c:#FFFF55> %gamemode% <c:#FFFFFF>for <c:#55FFFF>%player%<c:#FFFFFF>."),
    COMMAND_GAMEMODE_INVALID("<error>You need to specify a valid player."),
    COMMAND_TIME_ERROR("<error>You need to specify a world."),
    COMMAND_TIME_ALREADY_CHANGING("<error>Time is already changing in <c:#FFFFFF>%world%<error>."),
    COMMAND_DAY("<c:#FFFFFF>You have just brought <c:#FFFF55>daylight<c:#FFFFFF> into the world <success>%world%<c:#FFFFFF>."),
    COMMAND_NIGHT("<c:#FFFFFF>You have just brought <c:#FFFF55>nightfall<c:#FFFFFF> into the world <success>%world%<c:#FFFFFF>."),
    COMMAND_SUN("<c:#FFFFFF>You have just brought the <c:#FFFF55>sun<c:#FFFFFF> into the world <success>%world%<c:#FFFFFF>."),
    COMMAND_ENDERSEE_ERROR("<error>Unable to load the ender chest of <c:#FFFFFF>%player%<error>."),
    COMMAND_TOP("<c:#AAAAAA>You've just been teleported to <c:#FFFF55>top<c:#AAAAAA>."),
    COMMAND_BOTTOM("<c:#AAAAAA>You've just been teleported to <c:#FFFF55>bottom<c:#AAAAAA>."),
    COMMAND_BOTTOM_ERROR("<error>Unable to find a position to transport you safely."),
    COMMAND_TOP_ERROR("<error>Unable to find a position to transport you safely."),
    COMMAND_SPEED_INVALID("<error>You need to specify a valid player."),
    COMMAND_SPEED_FLY("<c:#AAAAAA>You have just set your <underlined>fly<reset><c:#AAAAAA> speed to <c:#FFFFFF>%speed%<c:#AAAAAA> for <c:#FFFFFF>%player%<c:#AAAAAA>. <c:#555555>(<c:#FFFFFF>2 <c:#AAAAAA>by default<c:#555555>)"),
    COMMAND_SPEED_WALK("<c:#AAAAAA>You have just set your <underlined>walk<reset><c:#AAAAAA> speed to <c:#FFFFFF>%speed%<c:#AAAAAA> for <c:#FFFFFF>%player%<c:#AAAAAA>. <c:#555555>(<c:#FFFFFF>2 <c:#AAAAAA>by default<c:#555555>)"),
    COMMAND_SPEED_ERROR("<error>You must enter a number between <c:#FFAA00>0<error> and <c:#FFAA00>10<error>. <c:#555555>(<c:#FFFFFF>2 <c:#AAAAAA>by default<c:#555555>)"),

    COMMAND_FLY_ERROR_WORLD("<error>You can’t fly in this world."),
    COMMAND_FLY_ENABLE("<c:#AAAAAA>Flight mode <success>enable <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_FLY_ENABLE_SECONDS("<c:#AAAAAA>Flight mode <success>enable <c:#AAAAAA>for <c:#FFFFFF>%player%<success>. <c:#555555>(<c:#AAAAAA>time remaining %time%<c:#555555>)"),
    COMMAND_FLY_DISABLE("<c:#AAAAAA>Flight mode <error>disable <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_FLY_ERROR("<error>You have no more fly time available."),
    COMMAND_FLY_END("<error>You have used up your entire flight time, you can no longer fly"),
    COMMAND_FLY_ERROR_OTHER("<error>%player% has no more flight time available."),
    COMMAND_FLY_ADD("<success>You just added %time% <success>of fly time to <c:#FFFFFF>%player%<success>."),
    COMMAND_FLY_GET("<success>%player%<c:#555555>: <c:#FFFFFF>%player%<success>."),
    COMMAND_FLY_INFO_EMPTY("<error>You do not have time to fly available."),
    COMMAND_FLY_INFO("<success>You have <c:#FFFFFF>%time%<success>."),
    COMMAND_FLY_REMOVE("<success>You just removed %time% <success>of fly time to <c:#FFFFFF>%player%<success>."),
    COMMAND_FLY_SET("<success>You just set %time% <success>of fly time to <c:#FFFFFF>%player%<success>."),
    COMMAND_FLY_REMAINING("<success>You have <c:#FFFFFF>%time% <success> remaining flight time."),
    COMMAND_GOD_ENABLE("<c:#AAAAAA>God mode <success>enable <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_GOD_DISABLE("<c:#AAAAAA>God mode <error>disable <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_VANISH_ENABLE("<c:#AAAAAA>Vanish <success>enable <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_VANISH_DISABLE("<c:#AAAAAA>Vanish <error>disable <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_NIGHT_VISION_ENABLE("<c:#AAAAAA>Night vision <success>enable <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_NIGHT_VISION_DISABLE("<c:#AAAAAA>Night vision <error>disable <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_PHANTOMS_ENABLE("<c:#AAAAAA>Phantom spawning <success>enable <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_PHANTOMS_DISABLE("<c:#AAAAAA>Phantom spawning <error>disable <c:#AAAAAA>for <c:#FFFFFF>%player%<error>."),
    COMMAND_EXT_SENDER("<c:#AAAAAA>You just extinguished the player <c:#FFFFFF>%player%<c:#AAAAAA>."),
    COMMAND_EXT_RECEIVER("<success>You are no longer burning."),
    COMMAND_EXT_ERROR("<error>You cannot extinguish someone who is dead!"),
    COMMAND_HEAL_ALL("<c:#AAAAAA>You just healed all players on the server."),
    COMMAND_HEAL_SENDER("<c:#AAAAAA>You just healed the player <c:#FFFFFF>%player%<c:#AAAAAA>."),
    COMMAND_HEAL_RECEIVER("<success>You have been healed."),
    COMMAND_HEAL_ERROR("<error>You cannot heal someone who is dead !"),
    COMMAND_LIGHTING_ALL("<c:#AAAAAA>You just struck all players with lightning."),
    COMMAND_LIGHTING("<c:#AAAAAA>You just summoned lightning at the targeted location."),
    COMMAND_LIGHTING_SENDER("<c:#AAAAAA>You just summoned lightning at the location of <c:#FFFFFF>%player%<c:#AAAAAA>."),
    COMMAND_LIGHTING_RECEIVER("<c:#FF5555>You have been struck by lightning."),
    COMMAND_LIGHTING_ERROR("<error>You cannot strike a dead player with lightning!"),
    COMMAND_FEED_ALL("<c:#AAAAAA>You just feed all players on the server."),
    COMMAND_FEED_SENDER("<c:#AAAAAA>You just feed the player <c:#FFFFFF>%player%<c:#AAAAAA>."),
    COMMAND_FEED_RECEIVER("<success>You have been feed."),
    COMMAND_FEED_ERROR("<error>You cannot feed someone who is dead !"),
    COMMAND_AFK_ENABLE("<c:#AAAAAA>You are now marked as <error>AFK<c:#AAAAAA>."),
    COMMAND_AFK_DISABLE("<success>You are no longer marked as AFK."),
    COMMAND_MESSAGE_TARGET_AFK("<c:#FFFFFF>%player% <c:#AAAAAA>is currently <error>AFK<c:#AAAAAA>."),
    COMMAND_CLEARINVENTORY_SUCCESS("<success>You have cleared your inventory."),
    COMMAND_CLEARINVENTORY_SUCCESS_OTHER("<success>You have cleared the inventory of <c:#FFFFFF>%player%<success>."),

    // Teleport Command
    COMMAND_TPA_ERROR("<error>You have already sent a request to <c:#34CFE0>%player%<error>."),
    COMMAND_TPA_ERROR_SAME("<error>You cannot teleport to yourself."),
    COMMAND_TPA_ERROR_OFFLINE("<error>Unable to find player, must be offline."),
    COMMAND_TPA_ERROR_TO_LATE_EMPTY("<error>You do not have a teleport request."),
    COMMAND_TPA_ERROR_TO_LATE_EXPIRE("<error>The teleport request has expired."),
    COMMAND_TPA_SENDER("<c:#AAAAAA>You have just sent a teleport request to <c:#34CFE0>%player%<c:#AAAAAA>."),
    COMMAND_TPA_RECEIVER(
            "<c:#AAAAAA>You have just received a teleport request from <c:#34CFE0>%player%<c:#AAAAAA>.",
            "<c:#AAAAAA>You have <error>60 <c:#FFAA00>seconds<c:#FFFF55> to accept the teleport request.",
            "<c:#AAAAAA>To accept the request do <c:#0EEA93>/tpaccept<c:#AAAAAA>."
    ),
    COMMAND_TPA_ACCEPT_RECEIVER("<success>You have just accepted the teleport request from <c:#34CFE0>%player%<success>."),
    COMMAND_TPA_ACCEPT_SENDER("<c:#34CFE0>%player%<success> has just accepted your teleport request."),

    COMMAND_TPA_HERE_ERROR("<error>You have already sent a request to <c:#34CFE0>%player%<error>."),
    COMMAND_TPA_HERE_ERROR_SAME("<error>You cannot teleport to yourself."),
    COMMAND_TPA_HERE_SENDER("<c:#AAAAAA>You have just sent a teleport request to <c:#34CFE0>%player%<c:#AAAAAA>."),
    COMMAND_TPA_HERE_RECEIVER(
            "<c:#34CFE0>%player% <c:#AAAAAA>request to teleport you to him.",
            "<c:#AAAAAA>You have <error>60 <c:#FFAA00>seconds<c:#FFFF55> to accept the teleport request.",
            "<c:#AAAAAA>To accept the request do <c:#0EEA93>/tpaccept<c:#AAAAAA>."
    ),
    COMMAND_TPA_HERE_ACCEPT_RECEIVER("<success>You have just accepted the teleport request from <c:#34CFE0>%player%<success>."),
    COMMAND_TPA_HERE_ACCEPT_SENDER("<c:#34CFE0>%player%<success> has just accepted your teleport request."),

    COMMAND_TELEPORT_IGNORE_PLAYER("<error>You cannot send a teleport request to <c:#34CFE0>%player%<error> they are ignoring you."),
    COMMAND_TELEPORT_WORLD("<error>You need to be in the same world to teleport."),
    COMMAND_TPA_ERROR_TO_LATE("<error>You do not have a teleport request."),
    COMMAND_TPA_ERROR_TO_LATE_2("<error>The request has expired."),
    COMMAND_TPA_ERROR_NO_REQUEST_FROM_PLAYER("<error>You do not have a teleport request from <c:#FFFFFF>%player%<error>."),
    COMMAND_TPA_ACCEPT_ALL_SUCCESS("<success>You have accepted <c:#FFFFFF>%count% <success>teleport request(s)."),
    COMMAND_TPA_DENY_ALL_SUCCESS("<success>You have denied <c:#FFFFFF>%count% <success>teleport request(s)."),
    COMMAND_TP_DENY_SENDER("Denied %player% teleport request."),
    COMMAND_TP_DENY_RECEIVER("%player% has denied your teleport request"),
    COMMAND_TP_CANCEL_ERROR("<error>You did not send a teleport request at <c:#FFFFFF>%player%<error>."),
    COMMAND_TP_CANCEL_SENDER("<error>Cancelled <c:#99E0FF>your teleport request to %player%."),
    COMMAND_TP_CANCEL_RECEIVER("<c:#FFFFFF>%player% <error>cancelled their teleport request to you."),
    COMMAND_TP("<c:#AAAAAA>You just teleport to the player <c:#34CFE0>%player%<c:#FFFFFF>."),
    COMMAND_TP_LOCATION("<c:#AAAAAA>You just teleport to <c:#34CFE0>%x%<c:#FFFFFF>, <c:#34CFE0>%y%<c:#FFFFFF>, <c:#34CFE0>%z%<c:#FFFFFF>."),
    COMMAND_TP_LOCATION_ERROR("<error>The coordinates are outside of the world."),
    COMMAND_TP_LOCATION_OTHER("<c:#AAAAAA>You just teleport <c:#34CFE0>%player% <c:#FFFFFF>to <c:#34CFE0>%x%<c:#FFFFFF>, <c:#34CFE0>%y%<c:#FFFFFF>, <c:#34CFE0>%z%<c:#FFFFFF>."),
    COMMAND_TP_ALL("<c:#AAAAAA>You just teleported all the players onto you."),
    COMMAND_TP_SELF("<c:#AAAAAA>You just teleported <c:#34CFE0>%player%<c:#AAAAAA> to your position."),
    COMMAND_TP_OTHER("<c:#AAAAAA>You just teleported <c:#34CFE0>%player% <c:#AAAAAA>to <c:#34CFE0>%target%<c:#AAAAAA>."),
    COMMAND_BACK("<c:#AAAAAA>Returning to previous location."),
    COMMAND_BACK_ERROR("<error>You have no last location. Impossible to go back."),

    COMMAND_MORE_ERROR("<error>You cannot make this order in item in hand."),
    COMMAND_MORE_SUCCESS("<c:#AAAAAA>You just put your item to <c:#FFFFFF>64<c:#AAAAAA>."),
    COMMAND_WORLD_TELEPORT_SELF("<success>You have just been teleported into the world <c:#FFFFFF>%world%<success>."),
    COMMAND_WORLD_TELEPORT_OTHER("<success>You just teleported the player <c:#FFFFFF>%player% <success>in the world <c:#FFFFFF>%world%<success>."),
    COMMAND_RANDOM_TP_OTHER("<success>You just randomly teleported the player <c:#FFFFFF>%player% <success>in the world <c:#FFFFFF>%world%<success>."),

    COMMAND_COMPACT_TYPE("<error>Impossible to compact the material <c:#FFFFFF>%material%<error>."),
    COMMAND_COMPACT_ERROR("<error>You have no <c:#FFFFFF>%item%<error> in your inventory."),
    COMMAND_COMPACT_SUCCESS("<c:#AAAAAA>You have just transformed <c:#0EEA93>x%amount% <c:#34CFE0>%item% <c:#AAAAAA>into <c:#0EEA93>x%toAmount% <c:#34CFE0>%toItem%<c:#AAAAAA>."),
    COMMAND_COMPACT_SUCCESS_ALL("<c:#AAAAAA>You just converted all your items."),
    COMMAND_HAT_SUCCESS("<c:#AAAAAA>You just put <c:#0EF0CE>%item% <c:#AAAAAA>on your head."),
    COMMAND_HAT_ERROR("<error>You cannot put air on your head."),
    COMMAND_PLAYER_WEATHER_RESET("<c:#AAAAAA>You just changed the weather to that of the server."),
    COMMAND_PLAYER_WEATHER_DOWNFALL("<c:#AAAAAA>You just put the rain on for yourself."),
    COMMAND_PLAYER_TIME_RESET("<c:#AAAAAA>You just changed the time to that of the server."),
    COMMAND_PLAYER_TIME_CHANGE("<c:#AAAAAA>You’re here to change your time."),

    COMMAND_LAG_WORLD_LINE("<c:#55FF55>%world%<c:#AAAAAA>: <c:#55FF55>%peaceful% peaceful<c:#AAAAAA>, <c:#FF5555>%monsters% monsters<c:#AAAAAA>, <c:#FFFF55>%items% items"),
    COMMAND_LAG_WORLD_HEADER("<c:#55FF55>Entities for world <c:#00AA00>%world%<c:#55FF55>:"),
    COMMAND_LAG_WORLD_ENTRY("<c:#AAAAAA>- <c:#55FF55>%type%<c:#AAAAAA>: <c:#FFFFFF>%amount%"),
    COMMAND_LAG_CLEAR_SUCCESS("<c:#AAAAAA>Removed <c:#55FF55>%amount% <c:#AAAAAA>entities in <c:#55FF55>%world%<c:#AAAAAA>."),
    COMMAND_LAG_CLEAR_COUNTDOWN("<c:#FF5555>Entity removal in <c:#FFFF55>%time% <c:#FF5555>seconds!"),

    DESCRIPTION_CLEAR_RANDOM_WORD("Clear random words"),
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
    DESCRIPTION_VANISH("Toggle vanish mode"),
    DESCRIPTION_FLY("Toggle flight"),
    DESCRIPTION_FLY_ADD("Added the fly time"),
    DESCRIPTION_FLY_GET("Display player's fly info"),
    DESCRIPTION_FLY_INFO("Display your temp fly info"),
    DESCRIPTION_FLY_REMOVE("Removed the fly time"),
    DESCRIPTION_FLY_SET("Set the fly time"),
    DESCRIPTION_HEAL("Heal a player"),
    DESCRIPTION_LIGHTING("Strike a player with lightning"),
    DESCRIPTION_AFK("Toggle your AFK status"),
    DESCRIPTION_SUN("Set the sun in your world"),
    DESCRIPTION_ENDERCHEST("Open your enderchest"),
    DESCRIPTION_ENDERSEE("Open a player enderchest"),
    DESCRIPTION_TOP("Teleporting to top"),
    DESCRIPTION_BOTTOM("Teleporting to top"),
    DESCRIPTION_SPEED("Change player speed"),
    DESCRIPTION_WALK_SPEED("Change your walk speed"),
    DESCRIPTION_FLY_SPEED("Change your fly speed"),
    DESCRIPTION_TPA("Teleport to a player"),
    DESCRIPTION_TPA_HERE("Teleport a player to your location"),
    DESCRIPTION_TP("Teleport to a player"),
    DESCRIPTION_TP_ALL("Teleport all player to your position"),
    DESCRIPTION_TP_RANDOM("Random Teleport in the world"),
    DESCRIPTION_TP_RANDOM_OTHER("Force a random teleport for a player"),
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
    DESCRIPTION_COMPACT("Compact a material into blocks, aliases: /blocks, /condense"),
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
    DESCRIPTION_ECO_RESET_ALL("Resets every player's balance for the specified economy"),
    DESCRIPTION_ECO_SHOW("Show player money"),
    DESCRIPTION_PAY("Pays another player from your balance"),
    DESCRIPTION_PAY_TOGGLE("Activate or not the receipt of money"),
    DESCRIPTION_PAY_NOTIFICATION_TOGGLE("Toggle receiving pay notifications"),
    DESCRIPTION_SET_SPAWN("Set server spawn"),
    DESCRIPTION_SET_FIRST_SPAWN("Set server first spawn"),
    DESCRIPTION_SPAWN("Teleport to spawn"),
    DESCRIPTION_SPAWN_FIRST("Teleport to first spawn"),
    DESCRIPTION_WARP_SET("Create a warp"),
    DESCRIPTION_WARP_USE("Teleport to a warp"),
    DESCRIPTION_WARP_DEL("Delete a warp"),
    DESCRIPTION_WARP_LIST("Show warp list"),
    DESCRIPTION_SET_HOME("Create a home"),
    DESCRIPTION_SET_HOME_CONFIRM("Confirm the creation of a home"),
    DESCRIPTION_DEL_HOME("Delete a home"),
    DESCRIPTION_DEL_HOME_CONFIRM("Confirm the deletion of a home"),
    DESCRIPTION_HOME("Teleport to a home"),
    DESCRIPTION_HOME_LIST("Show player's homes"),
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
    DESCRIPTION_TP_TOGGLE("Toggle receiving teleport requests"),
    DESCRIPTION_TPA_HERE_TOGGLE("Toggle receiving tpahere requests"),
    DESCRIPTION_IGNORE("Ignore a player, blocking their private messages and teleport requests"),
    DESCRIPTION_UNIGNORE("Stop ignoring a player"),
    DESCRIPTION_IGNORE_LIST("List the players you are ignoring"),
    DESCRIPTION_PING_SOUND("Toggle the chat ping sound"),
    DESCRIPTION_DEL_HOME_OTHER("Delete a home of another player"),
    DESCRIPTION_HOME_PUBLIC("Make one of your homes public"),
    DESCRIPTION_PUBLIC_HOMES("List the public homes"),
    DESCRIPTION_HOME_SHARE("Share a home with a player"),
    DESCRIPTION_HOME_UNSHARE("Stop sharing a home with a player"),
    DESCRIPTION_HOME_SHARES("List the players a home is shared with"),
    DESCRIPTION_HOME_CATEGORY("Set the category of a home"),
    DESCRIPTION_HOME_FAVORITE("Toggle a home as favorite"),
    DESCRIPTION_HOME_IMPORT("Import homes from another plugin"),
    DESCRIPTION_POWER_TOOLS_TOGGLE("Toggle power tools"),
    DESCRIPTION_SOCIALSPY("Display private messages of players"),
    DESCRIPTION_COMPACT_ALL("Compact every item in your inventory, aliases: /blocksall, /condenseall"),
    DESCRIPTION_FURNACE("Smelt all the items in your hand"),
    DESCRIPTION_SKULL("Gets the head of a player"),
    DESCRIPTION_REPAIR("Fix the item in your hand"),
    DESCRIPTION_REPAIR_ALL("Repair all items in your inventory"),
    DESCRIPTION_EXT("Stop burning"),
    DESCRIPTION_NEAR("Show players close to you"),
    DESCRIPTION_PLAY_TIME("Show player's playtime"),
    DESCRIPTION_VERSION("Show plugin version"),
    DESCRIPTION_KILL_ALL("Kill entities"),
    DESCRIPTION_LAG("Display entity counts per world"),
    DESCRIPTION_LAG_WORLD("Display detailed entity counts for a world"),
    DESCRIPTION_LAG_CLEAR("Clear entities for a world or globally"),
    DESCRIPTION_LAG_CLEAR_COUNTDOWN("Clear entities after a delay"),
    DESCRIPTION_SEEN("Show player informations"),
    DESCRIPTION_SEEN_IP("Show players who have the same IP"),
    DESCRIPTION_KIT("Get a kit"),
    DESCRIPTION_KIT_SHOW("Show a kit"),
    DESCRIPTION_KIT_EDITOR("Edit a kit"),
    DESCRIPTION_KIT_CREATE("Create a new kit"),
    DESCRIPTION_KIT_GIVE("Give a kit to a player"),
    DESCRIPTION_KIT_DELETE("Delete a kit"),
    DESCRIPTION_COOLDOWN("Show cooldown commands"),
    DESCRIPTION_COOLDOWN_SHOW("Display a player’s cooldowns"),
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
    DESCRIPTION_MAIL_GIVE_HAND("Send the item in your hand to a player's mailbox"),
    DESCRIPTION_MAIL_CLEAR("Removes items from the player’s mailbox"),
    DESCRIPTION_MAIL_GIVEALL("Give an items to players mailbox"),
    DESCRIPTION_MAIL_GIVEALL_HAND("Send the item in your hand to players mailboxes"),
    DESCRIPTION_MAIL_SEND("Send a mail to a player, online or not"),
    DESCRIPTION_MAIL_READ("Read the mails you received"),
    DESCRIPTION_MAIL_SEND_ALL("Send a mail to every online player"),
    DESCRIPTION_MAIL_CLEAR_MESSAGES("Delete the mails of a player"),
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
    DESCRIPTION_PHANTOMS("Enable or disable phantom spawning"),
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
    DESCRIPTION_WORLDEDIT_CANCEL("Cancel current selection"),
    DESCRIPTION_WORLDEDIT_POS1("Set first position of the selection"),
    DESCRIPTION_WORLDEDIT_POS2("Set second position of the selection"),
    DESCRIPTION_WORLDEDIT_OPTION("Configure your use of worldedit"),
    DESCRIPTION_WORLDEDIT_OPTION_INVENTORY("Use your inventory to take or add items"),
    DESCRIPTION_WORLDEDIT_OPTION_BOSSBAR("Enable or disable the worldedit progress bar"),
    DESCRIPTION_VAULT_GIVE("Give items to player's vault"),
    DESCRIPTION_VAULT_ADD_SLOT("Add slot to player's vault"),
    DESCRIPTION_VAULT_SET_SLOT("Set slot to player's vault"),
    DESCRIPTION_VAULT_INFO("Get information about player's vault"),
    DESCRIPTION_VAULT_SHOW("Show a player's vault"),
    DESCRIPTION_VAULT_GET_SLOT("Get the content of a vault slot"),
    DESCRIPTION_VAULT_DELETE_SLOT("Force delete a vault slot"),
    DESCRIPTION_SHOW_ITEM("Show player's item"),
    DESCRIPTION_EXPERIENCE("Manage player experience"),
    DESCRIPTION_DISCORD_LINK("Link your discord account"),
    DESCRIPTION_DISCORD_UNLINK("Unlink your discord account"),
    DESCRIPTION_PUB("Send a message highlighted in the chat"),
    DESCRIPTION_STEP("Show step commands"),
    DESCRIPTION_STEP_START("Start a step"),
    DESCRIPTION_STEP_FINISH("Finish a step"),

    YOU("you"),
    TRASH("<c:#555555>Trash"),

    // Teleportation

    TELEPORT_MOVE("<error>You must not move!"),
    TELEPORT_MESSAGE(MessageType.ACTION, "<c:#AAAAAA>Teleporting in <c:#0EEA93>%seconds% <c:#AAAAAA>seconds, you must not move."),
    TELEPORT_SUCCESS(MessageType.ACTION, "<c:#99E0FF>You have just teleported successfully!"),
    TELEPORT_RANDOM_START("<c:#AAAAAA>Searching for a destination... you must not move."),
    TELEPORT_MESSAGE_RANDOM(MessageType.ACTION, "<c:#AAAAAA>Teleporting in <c:#0EEA93>%seconds% <c:#AAAAAA>seconds, you must not move."),
    TELEPORT_SUCCESS_RANDOM(MessageType.ACTION, "<c:#99E0FF>You have just teleported successfully!"),
    TELEPORT_MESSAGE_SPAWN(
            ClassicMessage.tchat("<c:#AAAAAA>Teleporting in <c:#0EEA93>%seconds% <c:#AAAAAA>seconds, you must not move."),
            ClassicMessage.action("<c:#AAAAAA>Teleporting in <c:#0EEA93>%seconds% <c:#AAAAAA>seconds, you must not move.")
    ),
    TELEPORT_MESSAGE_SPAWN_CONSOLE("<success>You just teleported the player <c:#FFFFFF>%player% <success>to spawn."),
    TELEPORT_SUCCESS_SPAWN(
            ClassicMessage.tchat("<c:#AAAAAA>You just teleported to <c:#0EEA93>spawn <c:#AAAAAA>!"),
            ClassicMessage.action("<c:#AAAAAA>You just teleported to <c:#0EEA93>spawn <c:#AAAAAA>!")
    ),
    TELEPORT_MESSAGE_WARP(
            ClassicMessage.tchat("<c:#AAAAAA>Teleporting in <c:#0EEA93>%seconds% <c:#AAAAAA>seconds, you must not move."),
            ClassicMessage.action("<c:#AAAAAA>Teleporting in <c:#0EEA93>%seconds% <c:#AAAAAA>seconds, you must not move.")
    ),
    TELEPORT_SUCCESS_WARP(
            ClassicMessage.tchat("<c:#AAAAAA>You just teleported to warp <c:#0EEA93>%name% <c:#AAAAAA>!"),
            ClassicMessage.action("<c:#AAAAAA>You just teleported to warp <c:#0EEA93>%name% <c:#AAAAAA>!")
    ),
    TELEPORT_MESSAGE_HOME(
            ClassicMessage.tchat("<c:#AAAAAA>Teleporting in <c:#0EEA93>%seconds% <c:#AAAAAA>seconds, you must not move."),
            ClassicMessage.action("<c:#AAAAAA>Teleporting in <c:#0EEA93>%seconds% <c:#AAAAAA>seconds, you must not move.")
    ),
    TELEPORT_SUCCESS_HOME(
            ClassicMessage.tchat("<c:#AAAAAA>You just teleported to home <c:#0EEA93>%name% <c:#AAAAAA>!"),
            ClassicMessage.action("<c:#AAAAAA>You just teleported to home <c:#0EEA93>%name% <c:#AAAAAA>!")
    ),

    TELEPORT_DAMAGE("<error>You must not take damage during teleportation."),
    TELEPORT_ERROR_LOCATION("<error>Unable to teleport you safely."),
    TELEPORT_CROSS_SERVER_NOT_SUPPORTED("<error>Cross-server teleportation is not supported on this server."),
    TELEPORT_CROSS_SERVER_CONNECTING("<success>Connecting to server <c:#FFFFFF>%server%<success>..."),
    TELEPORT_CROSS_SERVER_PLAYER_NOT_FOUND("<error>Player <c:#FFFFFF>%player% <error>was not found on any server."),
    
    // RTP Queue System Messages
    TELEPORT_ALREADY_IN_QUEUE("<error>You are already in the teleportation queue!"),
    TELEPORT_ADDED_TO_QUEUE("<success>You have been added to the teleportation queue. Position: <c:#0EEA93>%position%"),
    
    COOLDOWN("<error>✘ You must wait for <c:#FFFFFF>%cooldown% <error>before performing this action."),
    COOLDOWN_COMMANDS("<error>✘ You must wait for <c:#FFFFFF>%cooldown% <error>before performing this commands."),

    // Economy

    COMMAND_ECONOMY_NOT_FOUND("<error> Can’t find a economy with the name <c:#FFFFFF>%name%<error>."),
    COMMAND_ECONOMY_RESET_ALL_CONFIRM("<error>You are about to reset every balance for <c:#FFFFFF>%economy%<error>. Run the command again within <c:#FFFFFF>%seconds% seconds<error> to confirm."),
    COMMAND_ECONOMY_RESET_ALL_SUCCESS("<c:#99E0FF>You reset all balances for <c:#FFFFFF>%economy%<c:#99E0FF>."),
    COMMAND_ECONOMY_GIVE_ALL_SENDER("<c:#99E0FF>You just gave <c:#FFFFFF>%economyFormat% <c:#99E0FF>to the online players."),
    COMMAND_ECONOMY_GIVE_SENDER("<c:#99E0FF>You just gave <c:#FFFFFF>%economyFormat% <c:#99E0FF>to the player <c:#AAAAAA>%player%<c:#99E0FF>."),
    COMMAND_ECONOMY_GIVE_RECEIVER("<c:#99E0FF>You have just received <c:#FFFFFF>%economyFormat%."),
    COMMAND_ECONOMY_SET_SENDER("<c:#99E0FF>You just set <c:#FFFFFF>%economyFormat% <c:#99E0FF>to the player <c:#AAAAAA>%player%<c:#99E0FF>."),
    COMMAND_ECONOMY_SET_RECEIVER("<c:#99E0FF>You have just been modified <c:#FFFFFF>%economyFormat%."),
    COMMAND_ECONOMY_TAKE_SENDER("<c:#99E0FF>You just take <c:#FFFFFF>%economyFormat% <c:#99E0FF>to the player <c:#AAAAAA>%player%<c:#99E0FF>."),
    COMMAND_ECONOMY_TAKE_RECEIVER("<c:#99E0FF>You have just lost <c:#FFFFFF>%economyFormat%."),
    COMMAND_ECONOMY_SHOW_EMPTY("<c:#FFFFFF>%player% <error>has no money."),
    COMMAND_ECONOMY_SHOW_INFO("<c:#AAAAAA>- <c:#99E0FF>%economy% <c:#FFFFFF>%amount%."),
    COMMAND_MONEY(
            "<c:#99E0FF>You have<c:#555555>:",
            " <c:#AAAAAA>- <c:#99E0FF>%economy-name-money% <c:#FFFFFF>%economy-money%.",
            " <c:#AAAAAA>- <c:#99E0FF>%economy-name-coins% <c:#FFFFFF>%economy-coins%."
    ),

    COMMAND_MONEY_OTHER(
            "<c:#99E0FF>%player% have<c:#555555>:",
            " <c:#AAAAAA>- <c:#99E0FF>%economy-name-money% <c:#FFFFFF>%economy-money%.",
            " <c:#AAAAAA>- <c:#99E0FF>%economy-name-coins% <c:#FFFFFF>%economy-coins%."
    ),

    COMMAND_PAY_NEGATIVE("<error>Amount to pay must be positive."),
    COMMAND_PAY_MIN("<error>The minimum amount you can pay is <c:#FFFFFF>%amount%<error>."),
    COMMAND_PAY_MAX("<error>The maximum amount you can pay is <c:#FFFFFF>%amount%<error>."),
    COMMAND_PAY_SENDER("<c:#99E0FF>You just sent <c:#F8F327>%amount%<c:#99E0FF> to the player <c:#FFFFFF>%player%<c:#99E0FF>."),
    COMMAND_PAY_RECEIVER("<c:#FFFFFF>%player% <c:#99E0FF>just sent you <c:#F8F327>%amount%<c:#99E0FF>."),
    COMMAND_PAY_DISABLE("<error>You can’t send %name%."),
    COMMAND_PAY_SELF("<error>You cannot send money to yourself."),
    COMMAND_PAY_NOT_ENOUGH("<error>You don't have enough money."),
    COMMAND_PAY_DISABLED("<error>You cannot send money to this player."),

    JOIN_MESSAGE("<c:#99E0FF>%player% <c:#AAAAAA>joined the game"),
    QUIT_MESSAGE("<c:#99E0FF>%player% <c:#AAAAAA>left the game"),
    FIRST_JOIN_MESSAGE("<c:#AAAAAA>Welcome <c:#99E0FF>%player% <c:#AAAAAA>on <your server name>! <c:#555555>(<c:#FFFFFF>%totalUserFormat% <c:#AAAAAA>registered players<c:#555555>)"),
    FIRST_JOIN_MOTD(
            "",
            "<success>✔ <c:#99E0FF>Welcome %player%<c:#99E0FF> !",
            "",
            "#858ef8Discord<c:#555555>: <c:#99E0FF><hover:show_text:'<c:#FFFFFF>Join the server #858ef8Discord <c:#FFFFFF>!'><click:open_url:'https://discord.groupez.dev/'>https://discord.groupez.dev/</click></hover>",
            "<c:#AAAAAA>Website<c:#555555>: <c:#99E0FF><hover:show_text:'<c:#FFFFFF>Open the Website <c:#FFFFFF>!'><click:open_url:'https://minecraft-inventory-builder.com/'>https://minecraft-inventory-builder.com/</click></hover>",
            ""
    ),
    JOIN_MOTD(
            "",
            "<c:#99E0FF>Welcome back %player%<c:#99E0FF> !",
            "",
            "#858ef8Discord<c:#555555>: <c:#99E0FF><hover:show_text:'<c:#FFFFFF>Join the server #858ef8Discord <c:#FFFFFF>!'><click:open_url:'https://discord.groupez.dev/'>https://discord.groupez.dev/</click></hover>",
            "<c:#AAAAAA>Website<c:#555555>: <c:#99E0FF><hover:show_text:'<c:#FFFFFF>Open the Website <c:#FFFFFF>!'><click:open_url:'https://minecraft-inventory-builder.com/'>https://minecraft-inventory-builder.com/</click></hover>",
            ""
    ),

    COMMAND_SET_SPAWN("<c:#FFFFFF>You just set the spawn location."),
    COMMAND_SPAWN_NOT_DEFINE("<error>The spawn does not exist. Please contact an administrator."),
    COMMAND_SPAWN_FIRST_NOT_DEFINE("<error>The first spawn does not exist. Please contact an administrator."),
    COMMAND_SPAWN_LOCATION_INVALID("<error>The spawn is invalid, please contact an administrator"),

    COMMAND_WARP_ALREADY_EXIST("<error>Warp <c:#FFFFFF>%name% <error>already exists. <c:#AAAAAA>Use <underlined><hover:show_text:'<c:#FFFFFF>Use this command'><click:suggest_command:'/setwarp %name% true'>/setwarp %name% true</click></hover><reset> command to modify the warp"),
    COMMAND_WARP_DOESNT_EXIST("<error>Warp <c:#FFFFFF>%name% <error> does not exist."),
    COMMAND_WARP_NO_PERMISSION("<error>You do not have permission to use the warp <c:#FFFFFF>%name%<error>."),
    COMMAND_WARP_CREATE(
            "<success>You just created the warp <c:#FFFFFF>%name% <success>to your position.",
            "<c:#AAAAAA>Warp Permission is: <hover:show_text:'<c:#FFFFFF>Copy command to add permission to a player'><click:SUGGEST_COMMAND:'/lp user <username> permission set essentials.warp.%name%'><c:#FFFFFF><underlined>essentials.warp.%name%</click></hover>"
    ),
    COMMAND_WARP_USE(
            "<error>Usage<c:#555555>: <c:#FFFFFF>/warp <destination>",
            "<c:#AAAAAA>Warps<c:#555555>:<c:#FFFFFF>%destinations%"
    ),
    COMMAND_WARP_EMPTY("<error>No warp defined, use command <c:#FFFFFF>/setwarp <name>"),
    COMMAND_WARP_DESTINATION(" <hover:show_text:'<c:#FFFFFF>Click to teleport to warp <underlined>%name%'><click:run_command:'/warp %name%'><c:#FFFFFF>%name%</click></hover><c:#AAAAAA>"),
    COMMAND_WARP_LIST("<c:#AAAAAA>Warps<c:#555555>:<c:#FFFFFF>%destinations%"),
    COMMAND_WARP_LIST_INFO(" <hover:show_text:'<c:#FFFFFF>Click to teleport to warp <underlined>%name%'><click:run_command:'/warp %name%'><c:#FFFFFF>%name%</click></hover><c:#AAAAAA>"),
    COMMAND_WARP_DELETE("<success>You just removed the warp <c:#FFFFFF>%name%<success>."),
    COMMAND_WARP_INVALID("<error>Warp <c:#FFFFFF>%name% <error>has an invalide location."),

    COMMAND_RANDOM_TP_ERROR("<error>No safe location found after multiple attempts, please try again."),
    COMMAND_RANDOM_TP_CONFIGURATION_NOT_FOUND("<error>Impossible to find the random tp configuration for the world <c:#FFFFFF>%world%<error>."),

    COMMAND_SET_HOME_INVALIDE_NAME("<c:#FFFFFF>%name% <error>is not a valid name, please choose another one."),
    COMMAND_SET_HOME_TOO_LONG("<c:#FFFFFF>%name% <error>is too long, please choose another one."),
    COMMAND_SET_HOME_TOO_SHORT("<c:#FFFFFF>%name% <error>is too short name, please choose another one."),
    COMMAND_SET_HOME_MAX("<error>You cannot have more than <c:#FFFFFF>%max%<error> homes."),
    COMMAND_SET_HOME_WORLD("<error>You cannot create a home here"),
    COMMAND_SET_HOME_CREATE(
            "",
            "<c:#99E0FF>You just created the home <c:#FFFFFF>%name%<c:#99E0FF>. <c:#555555>(<c:#AAAAAA>%current%<c:#555555>/<c:#AAAAAA>%max%<c:#555555>)",
            "<c:#FFFFFF>Use <underlined><hover:show_text:'<c:#FFFFFF>Click to teleport to home'><click:suggest_command:'/home %name%'>/home %name%</click></hover><reset> command to teleport to it",
            ""
    ),
    COMMAND_SET_HOME_CREATE_CONFIRM(
            "",
            "<c:#99E0FF>The home <c:#FFFFFF>%name%<c:#99E0FF>. already exists. Do you want to overwrite it?",
            "<success><hover:show_text:'<c:#FFFFFF>Click to overwrite the home %name%'><click:suggest_command:'/sethomeconfirm %name%'>ᴄᴏɴғɪʀᴍ</click></hover>",
            ""
    ),

    COMMAND_HOME_DOESNT_EXIST("<error>The home <c:#FFFFFF>%name%<error> does not exist."),

    COMMAND_HOME_INFORMATION_MULTI_LINE_HEADER(
            "",
            "<c:#8CC0CC>ʏᴏᴜʀ ʜᴏᴍᴇs <success>♦ <c:#AAAAAA>(%count%) <c:#555555>- <c:#AAAAAA>(Max: %max%)"
    ),
    COMMAND_HOME_INFORMATION_MULTI_LINE_CONTENT(MessageType.WITHOUT_PREFIX,
            " <c:#8CC0CC>♢ <c:#FFFFFF>%name% <c:#AAAAAA>in %world% (%environment%) <success><hover:show_text:'<c:#AAAAAA>Click to teleport to home <c:#FFFFFF><underlined>%name%'><click:run_command:'/home %name%'>[CLICK]</click></hover>"),
    COMMAND_HOME_INFORMATION_MULTI_LINE_FOOTER(MessageType.WITHOUT_PREFIX, ""),
    COMMAND_HOME_INFORMATION_IN_LINE(MessageType.WITHOUT_PREFIX, "<c:#8CC0CC>ʏᴏᴜʀ ʜᴏᴍᴇs <success>♦ <c:#AAAAAA>(%count%/%max%)<c:#555555>:<c:#FFFFFF>%homes%"),
    COMMAND_HOME_INFORMATION_IN_LINE_INFO(" <hover:show_text:'<c:#AAAAAA>Click to teleport to home <c:#FFFFFF><underlined>%name%'><click:run_command:'/home %name%'><c:#FFFFFF>%name%</click></hover><c:#AAAAAA>"),
    COMMAND_HOME_ICON_ERROR("<error>You must have an item in your hand to change the icon of your home."),
    COMMAND_HOME_ICON_SUCCESS("<success>You just changed the home icon <c:#FFFFFF>%name%<success>."),
    COMMAND_HOME_ICON_RESET("<success>You just reset the home icon <c:#FFFFFF>%name%<success>."),
    COMMAND_HOME_DELETE("<c:#99E0FF>You just deleted the home <c:#FFFFFF>%name%<c:#99E0FF>."),
    COMMAND_HOME_DELETE_CONFIRM(
            "",
            "<c:#99E0FF>You really want to delete the home <c:#FFFFFF>%name%<c:#99E0FF>.",
            "<success><hover:show_text:'<c:#FFFFFF>Click to overwrite the home %name%'><click:run_command:'/delhomeconfirm %name%'>ᴄᴏɴғɪʀᴍ</click></hover>",
            ""
    ),
    COMMAND_HOME_ADMIN_DELETE("<success>You just deleted home <c:#FFFFFF>%name%<success> of <c:#55FFFF>%player%<success>."),
    COMMAND_HOME_ADMIN_SET("<success>You just created home <c:#FFFFFF>%name%<success> of <c:#55FFFF>%player%<success>."),
    COMMAND_HOME_FEATURE_DISABLED("<error>This home feature is disabled on this server."),
    COMMAND_HOME_PUBLIC_ENABLE("<success>Your home <c:#FFFFFF>%name%<success> is now public."),
    COMMAND_HOME_PUBLIC_DISABLE("<success>Your home <c:#FFFFFF>%name%<success> is no longer public."),
    COMMAND_HOME_PUBLIC_LIMIT("<error>You have reached the maximum number of public homes (<c:#FFFFFF>%max%<error>)."),
    COMMAND_HOME_NOT_ACCESSIBLE("<error>The home <c:#FFFFFF>%name%<error> of <c:#FFFFFF>%player%<error> is not accessible to you."),
    COMMAND_PUBLIC_HOMES_HEADER("<success>Public homes (<c:#FFFFFF>%count%<success>):"),
    COMMAND_PUBLIC_HOMES_LINE("<c:#AAAAAA>- <c:#FFFFFF>%player%<c:#AAAAAA>:<c:#FFFFFF>%name% <click:run_command:'/home %player%:%name%'><hover:show_text:'<c:#AAAAAA>Teleport to this home'><c:#34CFE0>[Teleport]</click></hover>"),
    COMMAND_PUBLIC_HOMES_EMPTY("<error>There is no public home."),
    COMMAND_HOME_SHARE_ADD("<success>You shared your home <c:#FFFFFF>%name%<success> with <c:#FFFFFF>%player%<success>."),
    COMMAND_HOME_SHARE_REMOVE("<success>You stopped sharing your home <c:#FFFFFF>%name%<success> with <c:#FFFFFF>%player%<success>."),
    COMMAND_HOME_SHARE_ALREADY("<error>Your home <c:#FFFFFF>%name%<error> is already shared with <c:#FFFFFF>%player%<error>."),
    COMMAND_HOME_SHARE_NOT("<error>Your home <c:#FFFFFF>%name%<error> is not shared with <c:#FFFFFF>%player%<error>."),
    COMMAND_HOME_SHARE_SELF("<error>You cannot share a home with yourself."),
    COMMAND_HOME_SHARE_LIMIT("<error>You have reached the maximum number of shares for this home (<c:#FFFFFF>%max%<error>)."),
    COMMAND_HOME_SHARE_LIST_HEADER("<success>Home <c:#FFFFFF>%name%<success> is shared with <c:#FFFFFF>%count% <success>player(s):"),
    COMMAND_HOME_SHARE_LIST_LINE("<c:#AAAAAA>- <c:#FFFFFF>%player%"),
    COMMAND_HOME_SHARE_LIST_EMPTY("<error>Home <c:#FFFFFF>%name%<error> is not shared with anyone."),
    COMMAND_HOME_CATEGORY_SET("<success>The category of your home <c:#FFFFFF>%name%<success> is now <c:#FFFFFF>%category%<success>."),
    COMMAND_HOME_CATEGORY_RESET("<success>The category of your home <c:#FFFFFF>%name%<success> has been removed."),
    COMMAND_HOME_CATEGORY_INVALID("<error>Invalid category name."),
    COMMAND_HOME_FAVORITE_ADD("<success>Your home <c:#FFFFFF>%name%<success> is now a favorite."),
    COMMAND_HOME_FAVORITE_REMOVE("<success>Your home <c:#FFFFFF>%name%<success> is no longer a favorite."),
    COMMAND_HOME_PREVIEW("<c:#AAAAAA>Home <c:#FFFFFF>%name% <c:#555555>- <c:#AAAAAA>%world% <c:#555555>(<c:#AAAAAA>%x%<c:#555555>, <c:#AAAAAA>%y%<c:#555555>, <c:#AAAAAA>%z%<c:#555555>) <click:run_command:'/home %name% confirm'><c:#34CFE0>[Teleport]</click>"),
    COMMAND_HOME_IMPORT_START("<success>Importing homes from <c:#FFFFFF>%source%<success>..."),
    COMMAND_HOME_IMPORT_DONE("<success>Import finished: <c:#FFFFFF>%imported% <success>imported, <c:#FFFFFF>%skipped% <success>skipped, <c:#FFFFFF>%errors% <success>errors."),
    COMMAND_HOME_IMPORT_UNKNOWN("<error>Unknown import source <c:#FFFFFF>%source%<error>. Available: essentialsx."),
    COMMAND_HOME_IMPORT_ERROR("<error>An error occurred during the import, check the console."),
    COMMAND_HOME_ADMIN_LIST("<c:#8CC0CC>%player% ʜᴏᴍᴇs<c:#555555>:<c:#FFFFFF>%homes%"),
    COMMAND_HOME_ADMIN_LIST_INFO(" <hover:show_text:'<c:#AAAAAA>Click to teleport to home <c:#FFFFFF><underlined>%name%'><click:run_command:'/home %player%:%name%'><c:#FFFFFF>%name%</click></hover><c:#AAAAAA>"),
    COMMAND_HOME_ADMIN_LIST_EMPTY("The player has no home"),
    COMMAND_HOME_ERROR_TELEPORT("<error>Unable to find the position for teleportation, please delete your home."),

    COMMAND_KICK_NOTIFY(MessageType.WITHOUT_PREFIX, "<hover:show_text:'<c:#AAAAAA>By: <c:#FFFFFF>%sender%<newline><c:#AAAAAA>Duration: <c:#FFFFFF>%duration%<newline><c:#AAAAAA>Reason: <c:#FFFFFF>%reason%<newline><c:#AAAAAA>Date: <c:#FFFFFF>%created_at%'><click:run_command:'/sc %target%'><c:#555555>(<c:#F59E07>Sanction<c:#555555>) <c:#8AEBEB>%player% <c:#E33414>just kicked the player <c:#E0D12D>%target%<c:#E33414>.</click></hover>"),
    COMMAND_BAN_NOTIFY(MessageType.WITHOUT_PREFIX, "<hover:show_text:'<c:#AAAAAA>By: <c:#FFFFFF>%sender%<newline><c:#AAAAAA>Duration: <c:#FFFFFF>%duration%<newline><c:#AAAAAA>Reason: <c:#FFFFFF>%reason%<newline><c:#AAAAAA>Date: <c:#FFFFFF>%created_at%<newline><c:#AAAAAA>Expires: <c:#FFFFFF>%expired_at%'><click:run_command:'/sc %target%'><c:#555555>(<c:#F59E07>Sanction<c:#555555>) <c:#8AEBEB>%player% <c:#E33414>just banned the player <c:#E0D12D>%target%<c:#E33414>.</click></hover>"),
    COMMAND_MUTE_NOTIFY(MessageType.WITHOUT_PREFIX, "<hover:show_text:'<c:#AAAAAA>By: <c:#FFFFFF>%sender%<newline><c:#AAAAAA>Duration: <c:#FFFFFF>%duration%<newline><c:#AAAAAA>Reason: <c:#FFFFFF>%reason%<newline><c:#AAAAAA>Date: <c:#FFFFFF>%created_at%<newline><c:#AAAAAA>Expires: <c:#FFFFFF>%expired_at%'><click:run_command:'/sc %target%'><c:#555555>(<c:#F59E07>Sanction<c:#555555>) <c:#8AEBEB>%player% <c:#E33414>just muted the player <c:#E0D12D>%target%<c:#E33414>.</click></hover>"),
    COMMAND_UNMUTE_NOTIFY(MessageType.WITHOUT_PREFIX, "<hover:show_text:'<c:#AAAAAA>By: <c:#FFFFFF>%sender%<newline><c:#AAAAAA>Duration: <c:#FFFFFF>%duration%<newline><c:#AAAAAA>Reason: <c:#FFFFFF>%reason%<newline><c:#AAAAAA>Date: <c:#FFFFFF>%created_at%'><click:run_command:'/sc %target%'><c:#555555>(<c:#F59E07>Sanction<c:#555555>) <c:#8AEBEB>%player% <c:#E33414>just unmuted the player <c:#E0D12D>%target%<c:#E33414>.</click></hover>"),
    COMMAND_UNBAN_NOTIFY(MessageType.WITHOUT_PREFIX, "<hover:show_text:'<c:#AAAAAA>By: <c:#FFFFFF>%sender%<newline><c:#AAAAAA>Duration: <c:#FFFFFF>%duration%<newline><c:#AAAAAA>Reason: <c:#FFFFFF>%reason%<newline><c:#AAAAAA>Date: <c:#FFFFFF>%created_at%'><click:run_command:'/sc %target%'><c:#555555>(<c:#F59E07>Sanction<c:#555555>) <c:#8AEBEB>%player% <c:#E33414>just unbanned the player <c:#E0D12D>%target%<c:#E33414>.</click></hover>"),
    COMMAND_BAN_ERROR_DURATION("<error>The duration of a banishment must be at least 1 second."),
    COMMAND_MUTE_ERROR_DURATION("<error>The duration of a mute must be at least 1 second."),
    COMMAND_UN_MUTE_ERROR("<c:#FFFFFF>%player% <error>is not mute."),
    COMMAND_UN_BAN_ERROR("<c:#FFFFFF>%player% <error>is not ban."),
    COMMAND_SANCTION_ERROR("<error>You can’t sanction that player. He’s protected."),

    MESSAGE_KICK(
            "",
            "<error>You have just been kicked from the server for the reason:",
            "%reason%",
            "",
            "<c:#FFFFFF>Minecraft-Inventory-Builder.com",
            ""
    ),

    COMMAND_FREEZE_SUCCESS("<c:#AAAAAA>You just freeze the player <c:#FFFFFF>%player%<c:#AAAAAA>."),
    COMMAND_UN_FREEZE_SUCCESS("<c:#AAAAAA>You just unfreeze the player <c:#FFFFFF>%player%<c:#AAAAAA>."),

    MESSAGE_FREEZE(
            "",
            "<c:#FF5555>You have just been frozen.",
            ""
    ),

    MESSAGE_UN_FREEZE(
            "",
            "<c:#55FF55>You have just been unfrozen.",
            ""
    ),

    MESSAGE_MUTE(
            "",
            "<c:#FFFFFF>You have just lost your <u>voice</u>.",
            "<c:#FFFFFF>Duration<c:#555555>: <gradient:#7ae856:#a1d909>%duration%</gradient>",
            "<c:#FFFFFF>Reason<c:#555555>: <c:#82D1FF>%reason%",
            ""
    ),

    MESSAGE_UNMUTE(
            "",
            "<c:#FFFFFF>You have just regained your <u>voice</u>, congratulations !",
            "<c:#FFFFFF>Reason<c:#555555>: <c:#82D1FF>%reason%",
            ""
    ),

    MESSAGE_MUTE_TALK(
            "",
            "<c:#FFFFFF>You do not have the right to speak.",
            "",
            "<c:#FFFFFF>Duration<c:#555555>: <gradient:#7ae856:#a1d909>%duration%</gradient>",
            "<c:#FFFFFF>Reason<c:#555555>: <c:#82D1FF>%reason%",
            ""
    ),

    MESSAGE_BAN(
            "",
            "<error>You have just been ban from the server for the reason:",
            "<c:#FFFFFF>%reason%",
            "<c:#FFFFFF>Duration<c:#555555>: <c:#AAAAAA>%duration%",
            "",
            "<c:#FFFFFF>Minecraft-Inventory-Builder.com",
            ""
    ),

    MESSAGE_BAN_JOIN(
            "",
            "<error>You are banned on server:",
            "<c:#FFFFFF>%reason%",
            "<c:#FFFFFF>Remaining time<c:#555555>: <c:#AAAAAA>%remaining%",
            "",
            "<c:#FFFFFF>Minecraft-Inventory-Builder.com",
            ""
    ),

    // Chat
    CHAT_ERROR("<error>Impossible to let you speak, your data is not loaded."),
    CHAT_ALPHANUMERIC_REGEX("<error>You use a forbidden character."),
    CHAT_COOLDOWN("<error>Please wait before sending your next message. <c:#555555>(<c:#AAAAAA>%cooldown%<c:#555555>)"),
    CHAT_SAME("<error>You can’t put the same sentence."),
    CHAT_CAPS("<error>You don’t have to scream to talk !"),
    CHAT_FLOOD("<error>You can’t talk like that."),
    CHAT_LINK("<error>You cannot send a link in the chat."),
    CHAT_ITEM_FORBIDDEN_CHARACTERS("<error>You cannot display this item because its name contains forbidden characters."),
    CHAT_DISABLE("<error>The chat is currently unavailable."),

    CHAT_DEFAULT_FORMAT("<c:#FFFFFF>%displayName% <c:#656665>• <c:#AAAAAA>%message%"),
    CHAT_MESSAGES_EMPTY("<c:#FFFFFF>%player% <error>did not write a message."),
    CHAT_MESSAGES_LINE(MessageType.WITHOUT_PREFIX, "<c:#FFFFFF>%date% <c:#AAAAAA>%message%"),
    CHAT_MESSAGES_FOOTER(MessageType.WITHOUT_PREFIX, "<click:run_command:'/chathistory %player% %previousPage%'><hover:show_text:'Previous Page'><c:#FFFFFF>⬅</hover></click> <c:#AAAAAA>%page%/%maxPage% <click:run_command:'/chathistory %player% %nextPage%'><hover:show_text:'Next Page'><c:#FFFFFF>➡</hover></click>"),

    COMMAND_CHAT_CLEAR("<success>The chat just got clear."),
    COMMAND_CHAT_DISABLE_ERROR("<error>Chat is already disable."),
    COMMAND_CHAT_DISABLE_SUCCESS("<success>You just deactivated the chat."),
    COMMAND_CHAT_ENABLE_ERROR("<error>Chat is already enable."),
    COMMAND_CHAT_ENABLE_SUCCESS("<success>You just activated the chat."),
    COMMAND_CHAT_BROADCAST(MessageType.WITHOUT_PREFIX, "<primary>ʙʀᴏᴀᴅᴄᴀsᴛ <secondary>• <c:#FFFFFF>%message%"),

    COMMAND_MESSAGE_SELF("<error>You can’t talk to yourself."),
    COMMAND_MESSAGE_DISABLE("<c:#FFFFFF>%%player% <error>has disabled private messages."),
    COMMAND_MESSAGE_MUTE("<error>You’re not allowed to speak."),
    COMMAND_MESSAGE_ME("<hover:show_text:'<c:#FFFFFF>Click to reply'><click:suggest_command:'/r '><c:#555555>(<c:#F533F5>me <c:#AAAAAA>-> <c:#34CFE0>%target%<c:#555555>) <c:#E6E1E6><message></click></hover>"),
    COMMAND_MESSAGE_OTHER("<hover:show_text:'<c:#FFFFFF>Click to reply'><click:suggest_command:'/r '><c:#555555>(<c:#34CFE0>%target% <c:#AAAAAA>-> <c:#F533F5>me<c:#555555>) <c:#E6E1E6><message></click></hover>"),
    COMMAND_MESSAGE_SOCIAL_SPY(MessageType.WITHOUT_PREFIX, "<c:#555555>(<c:#AAAAAA>SocialSpy<c:#555555>) <c:#555555>(<hover:show_text:'<c:#FFFFFF>Open sanction for %sender%'><click:run_command:'/sc %sender%'><c:#34CFE0>%sender%</click></hover> <c:#AAAAAA>-> <hover:show_text:'<c:#FFFFFF>Open sanction for %receiver%'><click:run_command:'/sc %receiver%'><c:#F533F5>%receiver%</click></hover><c:#555555>) <c:#E6E1E6>%message%"),
    COMMAND_MESSAGE_ERROR("<error>You have no one to answer."),
    COMMAND_MESSAGE_TOGGLE_ENABLE("<c:#AAAAAA>Private message <success>enable <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_MESSAGE_TOGGLE_DISABLE("<c:#AAAAAA>Private message <error>disable <c:#AAAAAA>for <c:#FFFFFF>%player%<error>."),
    COMMAND_SOCIAL_SPY_ENABLE("<c:#AAAAAA>Social spy <success>enable <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_SOCIAL_SPY_DISABLE("<c:#AAAAAA>Social spy <error>disable <c:#AAAAAA>for <c:#FFFFFF>%player%<error>."),
    COMMAND_PAY_TOGGLE_ENABLE("<c:#AAAAAA>Pay <success>enable <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_PAY_TOGGLE_DISABLE("<error>All of your power tools have been disabled <c:#AAAAAA>for <c:#FFFFFF>%player%<error>."),
    COMMAND_PAY_NOTIFICATION_TOGGLE_ENABLE("<c:#AAAAAA>Pay notifications <success>enabled <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_PAY_NOTIFICATION_TOGGLE_DISABLE("<c:#AAAAAA>Pay notifications <error>disabled <c:#AAAAAA>for <c:#FFFFFF>%player%<error>."),
    COMMAND_POWER_TOOLS_TOGGLE_ENABLE("<success>All of your power tools have been enabled <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_POWER_TOOLS_TOGGLE_DISABLE("<c:#AAAAAA>Pay <error>disable <c:#AAAAAA>for <c:#FFFFFF>%player%<error>."),
    COMMAND_TELEPORT_REQUEST_DISABLED("<error>The player <c:#FFFFFF>%player%<error> has disabled teleport requests."),
    COMMAND_TP_TOGGLE_ENABLE("<c:#AAAAAA>Teleport requests <success>enabled <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_TP_TOGGLE_DISABLE("<c:#AAAAAA>Teleport requests <error>disabled <c:#AAAAAA>for <c:#FFFFFF>%player%<error>."),
    COMMAND_PING_SOUND_ENABLE("<c:#AAAAAA>Chat ping sound <success>enabled <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_PING_SOUND_DISABLE("<c:#AAAAAA>Chat ping sound <error>disabled <c:#AAAAAA>for <c:#FFFFFF>%player%<error>."),
    COMMAND_IGNORE_ADD("<success>You are now ignoring <c:#FFFFFF>%player%<success>. You will no longer receive their private messages or teleport requests."),
    COMMAND_IGNORE_REMOVE("<success>You are no longer ignoring <c:#FFFFFF>%player%<success>."),
    COMMAND_IGNORE_ALREADY("<error>You are already ignoring <c:#FFFFFF>%player%<error>."),
    COMMAND_IGNORE_NOT("<error>You are not ignoring <c:#FFFFFF>%player%<error>."),
    COMMAND_IGNORE_SELF("<error>You cannot ignore yourself."),
    COMMAND_IGNORE_LIST_EMPTY("<error>You are not ignoring anyone."),
    COMMAND_IGNORE_LIST_HEADER("<success>You are ignoring <c:#FFFFFF>%count% <success>player(s):"),
    COMMAND_IGNORE_LIST_LINE("<c:#AAAAAA>- <c:#FFFFFF>%player%"),
    COMMAND_MESSAGE_IGNORE_PLAYER("<error>You cannot send a message to <c:#34CFE0>%player%<error> they are ignoring you."),
    COMMAND_TELEPORT_HERE_REQUEST_DISABLED("<error>The player <c:#FFFFFF>%player%<error> has disabled tpahere requests."),
    COMMAND_TPA_HERE_TOGGLE_ENABLE("<c:#AAAAAA>Tpahere requests <success>enabled <c:#AAAAAA>for <c:#FFFFFF>%player%<success>."),
    COMMAND_TPA_HERE_TOGGLE_DISABLE("<c:#AAAAAA>Tpahere requests <error>disabled <c:#AAAAAA>for <c:#FFFFFF>%player%<error>."),

    COMMAND_FURNACE_TYPE("<error>Impossible to smelt the material <c:#FFFFFF>%material%<error>."),
    COMMAND_FURNACE_ERROR("<error>You have no <c:#FFFFFF>%item%<error> in your inventory."),
    COMMAND_FURNACE_SUCCESS("<c:#AAAAAA>You have just transformed <c:#0EEA93>x%amount% <c:#34CFE0>%item% <c:#AAAAAA>into <c:#0EEA93>x%toAmount% <c:#34CFE0>%toItem%<c:#AAAAAA>."),
    COMMAND_SKULL("<success>You just got the player’s head <c:#FFFFFF>%name%<success>."),
    COMMAND_REPAIR_ERROR("<error>The item in your hand cannot be repaired."),
    COMMAND_REPAIR_SUCCESS("<success>You just fixed the item in your hand."),
    COMMAND_REPAIR_ALL_ERROR("<error>You have no items to repair."),
    COMMAND_REPAIR_ALL_SUCCESS("<success>You have just repaired all the items in your inventory"),

    COMMAND_NEAR_EMPTY("<error>No player near to you."),
    COMMAND_NEAR_PLAYER("<c:#FFFFFF>Players near to you<c:#AAAAAA>:%players%"),
    COMMAND_NEAR_INFO("<newline><c:#FFAA00>%direction% <c:#AAAAAA>%player% <c:#555555>(<c:#FFFF55>%distance%m<c:#555555>)"),
    COMMAND_PLAY_TIME(
            "<c:#AAAAAA>You played<c:#555555>: <primary>%playtime%",
            "<c:#AAAAAA>Current session<c:#555555>: <primary>%playtime_session%"
    ),

    COMMAND_REMOVE("<c:#AAAAAA>Removed <success>%amount% <c:#AAAAAA>entities."),

    COMMAND_SEEN_OFFLINE(MessageType.WITHOUT_PREFIX, "<c:#99E0FF>Player <c:#FFFFFF>%player% <c:#99E0FF>has been <error>offline <c:#99E0FF>since <c:#AAAAAA>%date%<c:#99E0FF>."),
    COMMAND_SEEN_ONLINE(MessageType.WITHOUT_PREFIX, "<c:#99E0FF>Player <c:#FFFFFF>%player% <c:#99E0FF>has been <success>online <c:#99E0FF>since %date%<c:#99E0FF>."),
    COMMAND_SEEN_PLAYTIME(MessageType.WITHOUT_PREFIX, "<c:#99E0FF>PlayTime<c:#555555>: <c:#FFFFFF>%playtime%"),
    COMMAND_SEEN_UUID(MessageType.WITHOUT_PREFIX, "<c:#99E0FF>UUID<c:#555555>: <c:#FFFFFF>%uuid%"),
    COMMAND_SEEN_IP(MessageType.WITHOUT_PREFIX, "<c:#99E0FF>IP Address<c:#555555>:<c:#FFFFFF>%ips%"),
    COMMAND_SEEN_LAST_LOCATION(MessageType.WITHOUT_PREFIX, "<c:#99E0FF>Last Location<c:#555555>: <c:#FFFFFF>%world%, %x%, %y%, %z%"),
    COMMAND_SEEN_FIRST_JOIN(MessageType.WITHOUT_PREFIX, "#99E0FFFirst Join<c:#555555>:<c:#FFFFFF>%created_at%"),

    COMMAND_SEEN_ADDRESS(MessageType.WITHOUT_PREFIX, " <click:run_command:'/seenip %ip%'><hover:show_text:'<c:#FFFFFF>Show players who have the same address'>%ip%</hover></click>"),

    COMMAND_SEEN_IP_EMPTY("<error>Unable to find players with ip <c:#FFFFFF>%ip%<error>."),
    COMMAND_SEEN_IP_LINE("<c:#99E0FF>Players with the ip <c:#FFFFFF>%ip%<c:#555555>:%players%"),
    COMMAND_SEEN_IP_INFO(" <c:#FFFFFF><click:run_command:'/seen %name%'><hover:show_text:'<c:#FFFFFF>Show player's information'>%name%</hover></click><c:#555555>"),

    COMMAND_KIT_NO_PERMISSION("<error>You do not have permission to use the kit <c:#FFFFFF>%kit%<error>."),
    COMMAND_KIT_NOT_FOUND("<error>Unable to find the kit <c:#FFFFFF>%kit%<error>."),
    COMMAND_KIT_ALREADY_EXISTS("<error>The <c:#FFFFFF>%kit% <error>kit already exists."),
    COMMAND_KIT_INFORMATION_IN_LINE_EMPTY(MessageType.WITHOUT_PREFIX, "<c:#8CC0CC>ʏᴏᴜʀ ᴋɪᴛs <success>♦ <c:#AAAAAA>(%count%/%max%)<c:#555555>: <error>ɴᴏ ᴋɪᴛ ᴀᴠᴀɪʟᴀʙʟᴇ"),
    COMMAND_KIT_INFORMATION_IN_LINE(MessageType.WITHOUT_PREFIX, "<c:#8CC0CC>ʏᴏᴜʀ ᴋɪᴛs<c:#555555>:<c:#FFFFFF>%kits%"),
    COMMAND_KIT_INFORMATION_IN_LINE_INFO_AVAILABLE(" <hover:show_text:'<c:#AAAAAA>Click to get the kit <c:#FFFFFF><underlined>%name%'><click:run_command:'/kit %name%'><c:#1FDE65>%name%</click></hover><c:#AAAAAA>"),
    COMMAND_KIT_INFORMATION_IN_LINE_INFO_UNAVAILABLE(" <hover:show_text:'<c:#AAAAAA>You must wait <u>%time%</u> before getting the kit <c:#FFFFFF><underlined>%name%'><st><c:#DE1F1F>%name%</st></hover><c:#AAAAAA>"),

    COMMAND_KIT_INFORMATION_MULTI_LINE_HEADER(
            "",
            "<c:#8CC0CC>ʏᴏᴜʀ ᴋɪᴛs<c:#555555>:"
    ),
    COMMAND_KIT_INFORMATION_MULTI_LINE_CONTENT_AVAILABLE(MessageType.WITHOUT_PREFIX, "<c:#8CC0CC>♢ <hover:show_text:'<c:#AAAAAA>Click to get the kit <c:#FFFFFF><underlined>%name%'><click:run_command:'/kit %name%'><c:#1FDE65>%name% </click></hover>"),
    COMMAND_KIT_INFORMATION_MULTI_LINE_CONTENT_UNAVAILABLE(MessageType.WITHOUT_PREFIX, "<c:#8CC0CC>♢ <hover:show_text:'<c:#AAAAAA>You must wait <u>%time%</u> before getting the kit <c:#FFFFFF><underlined>%name%'><st><c:#DE1F1F>%name%</st></hover> <c:#555555>(<c:#AAAAAA>%time%<c:#555555>)"),
    COMMAND_KIT_INFORMATION_MULTI_LINE_FOOTER(MessageType.WITHOUT_PREFIX, ""),
    COMMAND_KIT_EDITOR_SAVE("<success>You have just modified the kit <c:#FFFFFF>%kit%<success>."),
    COMMAND_KIT_CREATE("<success>You just created the kit <c:#FFFFFF>%kit%<success>."),
    COMMAND_KIT_DELETE("<success>You just deleted the kit <c:#FFFFFF>%kit%<success>."),
    COMMAND_KIT_DELETE_ERROR("<error>Impossible to delete the kit <c:#FFFFFF>%kit%<error>."),
    COMMAND_KIT_GIVE("<success>You just gave the kit <c:#FFFFFF>%kit%<success> to <u>%player%<u><success>."),

    COMMAND_COOLDOWN_HEADER("<c:#FFFFFF>Player cooldown %player% <c:#555555>(<c:#AAAAAA>%amount%<c:#555555>)"),
    COMMAND_COOLDOWN_EMPTY("<c:#FFFFFF>%player% <error>has no cooldown."),
    COMMAND_COOLDOWN_LINE(MessageType.WITHOUT_PREFIX, "<c:#FFFFFF>%key% <secondary>- <c:#AAAAAA>%createdAt% <secondary>- <success>%timeLeft% <secondary>- <hover:show_text:'<c:#FFFFFF>Click to remove the cooldown'><click:run_command:'/cooldown delete %player% %key%'><c:#555555>[<error>ᴅᴇʟᴇᴛᴇ<c:#555555>]</click></hover>"),
    COMMAND_COOLDOWN_NOT_FOUND("<c:#FFFFFF>%cooldown% <error>was not found."),
    COMMAND_COOLDOWN_DELETE("<success>You just removed the cooldown <c:#FFFFFF>%cooldown% <success>from <underlined>%player%<reset><success>."),
    COMMAND_COOLDOWN_CREATE("<success>You just created the cooldown <c:#FFFFFF>%key% <success>for <underlined>%player%<reset><success>."),

    COMMAND_ITEM_EMPTY("<error>You have no item in your hand."),
    COMMAND_ITEM_CLEAR("<success>You have cleared this item's name"),
    COMMAND_ITEM_SET("<success>You have renamed your held item to <c:#FFFFFF>%name%<success>."),
    COMMAND_ITEM_LORE_ADD("<success>You just added the line <c:#FFFFFF>%text%<success>."),
    COMMAND_ITEM_LORE_SET("<success>You just set line %line% to <c:#FFFFFF>%text%<success>."),
    COMMAND_ITEM_LORE_CLEAR("<success>You just clear the item lore."),
    COMMAND_ITEM_LORE_SET_ERROR("<error>Can’t find the line <c:#FFFFFF>%line%<error>."),
    COMMAND_GIVE_ERROR("<error>Can’t find the item <c:#FFFFFF>%item%<error>."),
    COMMAND_GIVE("<success>You just gave <underlined><c:#FFFFFF>x%amount% %item%<reset> <success>to player <c:#FFFFFF>%player%<success>."),
    COMMAND_GIVE_VAULT("<success>You just gave <underlined><c:#FFFFFF>x%amount% %item%<reset> <success>to <c:#FFFFFF>%player%<success> vault's."),
    COMMAND_GIVE_ALL("<success>You just gave <underlined><c:#FFFFFF>x%amount% %item%<reset> <success>to online player."),

    COMMAND_POWER_TOOL_ERROR_ITEM("<error>You have no item in your hand."),
    COMMAND_POWER_TOOL_ERROR_RESET("<error>This item has no recorded command."),
    COMMAND_POWER_TOOL_INFO("<success>You just put the command <c:#FFFFFF>/%command%<success> on the item <c:#FFFFFF>%item%<success>."),
    COMMAND_POWER_TOOL_RESET("<success>You have just deleted the command on the item <c:#FFFFFF>%item%<success>."),

    MAILBOX_REMOVE_FULL("<error>You must have space in your inventory to retrieve an item."),
    MAILBOX_REMOVE_EXPIRE("<error>You can no longer retrieve this item, it has expired."),
    MAILBOX_ADD(MessageType.ACTION, "<success>An item has just been added to your mailbox <c:#555555>(<c:#FFFFFF>/mail<c:#555555>)"),
    MAILBOX_GIVE_ERROR("<error>Can’t find the item <c:#FFFFFF>%item%<error>."),
    MAILBOX_GIVE("<success>You just gave <underlined><c:#FFFFFF>x%amount% %item%<reset> <success>to player <c:#FFFFFF>%player%<success> mailbox."),
    MAILBOX_GIVE_ALL("<success>You just gave <underlined><c:#FFFFFF>x%amount% %item%<reset> <success>to online player mailbox."),
    MAILBOX_GIVE_HAND("<success>You just sent the item in your hand to <c:#FFFFFF>%player%<success>'s mailbox (<c:#FFFFFF>x%amount% %item%<success>)."),
    MAILBOX_GIVE_ALL_HAND("<success>You just sent the item in your hand (<c:#FFFFFF>x%amount% %item%<success>) to every online player's mailbox."),
    MAILBOX_CLEAR("<success>You have just cleared the mailbox of <c:#FFFFFF>%player%<success>."),

    MAILBOX_MESSAGE_SEND("<success>Your mail was sent to <c:#FFFFFF>%player%<success>."),
    MAILBOX_MESSAGE_RECEIVE("<primary><c:#FFFFFF>%player% <primary>sent you a mail <c:#555555>(<c:#FFFFFF>/mail read<c:#555555>)"),
    MAILBOX_MESSAGE_SEND_ALL("<success>Your mail was sent to <c:#FFFFFF>%amount% <success>player(s)."),
    MAILBOX_MESSAGE_NOTIFY("<primary>You have <c:#FFFFFF>%amount% <primary>unread mail(s), use <click:run_command:'/mail read'><hover:show_text:'<c:#FFFFFF>Click to read your mails'><c:#FFFFFF>/mail read</hover></click><primary>."),
    MAILBOX_MESSAGE_EMPTY("<error>You don't have any mail."),
    MAILBOX_MESSAGE_HEADER(MessageType.WITHOUT_PREFIX, "", "<primary>Your mails <secondary>(<c:#FFFFFF>%amount%<secondary>)", ""),
    MAILBOX_MESSAGE_LINE(MessageType.WITHOUT_PREFIX, "<secondary><c:#555555>[<c:#FFFFFF>%date%<c:#555555>] <primary>%player%<secondary>: <c:#FFFFFF>%message%"),
    MAILBOX_MESSAGE_FOOTER(MessageType.WITHOUT_PREFIX, "", "<secondary>Use <click:run_command:'/mail clearmessages'><hover:show_text:'<c:#FFFFFF>Click to delete your mails'><c:#FFFFFF>/mail clearmessages</hover></click><secondary> to delete them.", ""),
    MAILBOX_MESSAGE_CLEAR("<success>You have just deleted the mails of <c:#FFFFFF>%player%<success>."),
    MAILBOX_MESSAGE_SELF("<error>You cannot send a mail to yourself."),
    MAILBOX_MESSAGE_MUTE("<error>You cannot send a mail while you are muted."),
    MAILBOX_MESSAGE_IGNORE("<error><c:#FFFFFF>%player% <error>does not want to receive your mails."),
    MAILBOX_MESSAGE_FULL("<error>The mailbox of <c:#FFFFFF>%player% <error>is full."),
    MAILBOX_MESSAGE_TOO_LONG("<error>Your mail cannot be longer than <c:#FFFFFF>%max% <error>characters."),


    HOLOGRAM_CREATE_ERROR("<error>Hologram <c:#FFFFFF>%name% <error>already exists."),
    HOLOGRAM_CREATE(MessageType.WITHOUT_PREFIX,
            "<success>You just created the hologram <c:#FFFFFF>%name%<success>.",
            "<c:#AAAAAA>Use <underlined><click:suggest_command:'/hologram edit %name%'>/hologram %command% %name%</click><reset><c:#AAAAAA> command to edit the hologram"
    ),
    HOLOGRAM_DELETE("<success>Hologram <c:#FFFFFF>%name% <success>has been deleted."),
    HOLOGRAM_DOESNT_EXIST("<error>Hologram <c:#FFFFFF>%name% <error>doesn't exists."),
    HOLOGRAM_IS_NOT_A_TEXT("<error>Hologram <c:#FFFFFF>%name% <error>is not a text hologram."),
    HOLOGRAM_IS_NOT_A_BLOCK("<error>Hologram <c:#FFFFFF>%name% <error>is not a block hologram."),
    HOLOGRAM_IS_NOT_A_ITEM("<error>Hologram <c:#FFFFFF>%name% <error>is not a item hologram."),
    HOLOGRAM_ADD_LINE("<success>You just added a line to the hologram <c:#FFFFFF>%name%<success>."),
    HOLOGRAM_SET_LINE("<success>You just set the line <c:#FFFFFF>%line%<success> to the hologram <c:#FFFFFF>%name%<success>."),
    HOLOGRAM_INSERT_BEFORE_LINE("<success>You just set a new line before the line <c:#FFFFFF>%line%<success> to the hologram <c:#FFFFFF>%name%<success>."),
    HOLOGRAM_INSERT_AFTER_LINE("<success>You just set a new line after the line <c:#FFFFFF>%line%<success> to the hologram <c:#FFFFFF>%name%<success>."),
    HOLOGRAM_LINE_DOESNT_EXIST("<error>The line <c:#FFFFFF>%line% <error>does not exist for the hologram <c:#FFFFFF>%name%<error>."),
    HOLOGRAM_REMOVE_LINE("<success>You just remove the line <c:#FFFFFF>%line%<success> to the hologram <c:#FFFFFF>%name%<success>."),
    HOLOGRAM_SCALE("<success>You just changed the scale of the hologram <c:#FFFFFF>%name% <success>to <c:#AAAAAA>%x%<c:#555555>, <c:#AAAAAA>%y%<c:#555555>, <c:#AAAAAA>%z%"),
    HOLOGRAM_TRANSLATION("<success>You just changed the translation of the hologram <c:#FFFFFF>%name% <success>to <c:#AAAAAA>%x%<c:#555555>, <c:#AAAAAA>%y%<c:#555555>, <c:#AAAAAA>%z%"),
    HOLOGRAM_MOVE_HERE("<success>You just teleported the hologram <c:#FFFFFF>%name%<success> to your position."),
    HOLOGRAM_MOVE_TO("<success>You just teleported the hologram <c:#FFFFFF>%name% <success>to <c:#AAAAAA>%x%<c:#555555>, <c:#AAAAAA>%y%<c:#555555>, <c:#AAAAAA>%z%"),
    HOLOGRAM_BILLBOARD("<success>You just change the billboard of the hologram <c:#FFFFFF>%name%<success> to <c:#FFFFFF>%billboard%<success>."),
    HOLOGRAM_TEXT_ALIGNMENT("<success>You just change the text alignment of the hologram <c:#FFFFFF>%name%<success> to <c:#FFFFFF>%textAlignment%<success>."),
    HOLOGRAM_YAW("<success>You just change the yaw of the hologram <c:#FFFFFF>%name%<success> to <c:#FFFFFF>%yaw%<success>."),
    HOLOGRAM_PITCH("<success>You just change the pitch of the hologram <c:#FFFFFF>%name%<success> to <c:#FFFFFF>%pitch%<success>."),
    HOLOGRAM_TEXT_BACKGROUND("<success>You just change the background color of the hologram <c:#FFFFFF>%name%<success> to <c:#FFFFFF>%color%<success>."),
    HOLOGRAM_TEXT_BACKGROUND_ERROR("<error>Could not parse background color."),
    HOLOGRAM_TEXT_SHADOW("<success>You set the text shadow to <c:#FFFFFF>%textshadow% <success>for the hologram <c:#FFFFFF>%name%<success>."),
    HOLOGRAM_SEE_THROUGH("<success>You set the see through to <c:#FFFFFF>%seethrough% <success>for the hologram <c:#FFFFFF>%name%<success>."),
    HOLOGRAM_SHADOW_STRENGTH("<success>You set the shadow strength to <c:#FFFFFF>%shadow% <success>for the hologram <c:#FFFFFF>%name%<success>."),
    HOLOGRAM_SHADOW_RADIUS("<success>You set the shadow radius to <c:#FFFFFF>%shadow% <success>for the hologram <c:#FFFFFF>%name%<success>."),
    HOLOGRAM_VIEW_DISTANCE("<success>You set the view distance to <c:#FFFFFF>%distance% <success>for the hologram <c:#FFFFFF>%name%<success>."),
    HOLOGRAM_LIST_EMPTY("<error>There is no hologram, to create one please use the command <c:#FFFFFF>/holo create <name>"),
    HOLOGRAM_LIST(MessageType.WITHOUT_PREFIX, "<c:#FFFFFF>%name% <c:#555555>- <c:#AAAAAA>%world%: %x%, %y%, %z% <c:#555555>- <hover:show_text:'<c:#AAAAAA>Click to be teleported to this hologram'><click:run_command:'/holo tp %name%'><c:#55FF55>ᴛᴇʟᴇᴘᴏʀᴛ</click> </hover><hover:show_text:'<c:#FF5555>Click to delete this hologram'><click:suggest_command:'/holo delete %name%'><c:#FF5555>ᴅᴇʟᴇᴛᴇ</click></hover>"),
    HOLOGRAM_ITEM_ERROR("<error>You can't use air for this hologram."),
    HOLOGRAM_ITEM("<success>You just modified the item of the hologram <c:#FFFFFF>%name%<success>."),
    HOLOGRAM_BLOCK("<success>You just modified the block of the hologram <c:#FFFFFF>%name%<success> to <c:#FFFFFF>%material%<success>."),

    SCOREBOARD_DISABLE("<error>You just disabled the scoreboard."),
    SCOREBOARD_ENABLE("<success>You just enabled the scoreboard."),

    COMMAND_BALTOP_ERROR("<error>Can’t find the economy <c:#FFFFFF>%name%<error>."),
    COMMAND_BALTOP_HEADER(MessageType.WITHOUT_PREFIX, "<c:#00F986>ᴍᴏsᴛ ᴍᴏɴᴇʏ <c:#555555>(<c:#FFFFFF>%page%<c:#555555>/<c:#AAAAAA>%maxPage%<c:#555555>) <click:run_command:'/baltop %previousPage%'><c:#FFFFFF>◀</click> <c:#555555>- <click:run_command:'/baltop %nextPage%'><c:#FFFFFF>▶</click>"),
    COMMAND_BALTOP(MessageType.WITHOUT_PREFIX, "<c:#00F986>#%position% <c:#FFFFFF>%name%<c:#555555>: %amount%"),

    CODE_NOT_FOUND("<c:#FF5555>Cannot find the code."),

    COMMAND_VOTEPARTY_SET("<success>You just set the voteparty to <c:#FFFFFF>%amount% <success>votes."),
    COMMAND_VOTEPARTY_ADD("<success>You just added <c:#FFFFFF>%amount% <success>vote in the voteparty."),
    COMMAND_VOTEPARTY_REMOVE("<success>You have just removed <c:#FFFFFF>%amount% <success>vote in the voteparty."),
    COMMAND_VOTEPARTY_INFORMATIONS("<c:#55FF55>VoteParty: <c:#FFFFFF>%amount%<c:#555555>/<c:#FFFF55>%objective%"),

    COMMAND_VOTE_ADD("<success>You just added a vote to <c:#FFFFFF>%player%<success>."),
    COMMAND_VOTE_ADD_ERROR("<error>The site <c:#FFFFFF>%site% <error>doesn't exist !"),
    COMMAND_VOTE(MessageType.ACTION, "<success>You just voted and received a vote key."),

    VOTE_OFFLINE(
            "",
            "<success>You have voted <c:#FFAA00>%amount% times <success>while offline, use the command <c:#FFFF55><click:run_command:'/mailbox'>/mailbox</click> <success>to get your items !",
            ""
    ),


    VAULT_LORE(
            "",
            "<c:#FFFFFF>ɪɴғᴏʀᴍᴀᴛɪᴏɴs",
            " <c:#F8B175>• <c:#AAAAAA>Quantity<c:#555555>: <c:#FFFF55>%quantity%",
            "",
            "<c:#FFFFFF><b>➥</b> <c:#FC99F8>Left Click <c:#AAAAAA>for take 1",
            "<c:#FFFFFF><b>➥</b> <c:#FC99F8>Right Click <c:#AAAAAA>for take 64",
            "<c:#FFFFFF><b>➥</b> <c:#FC99F8>Sneak + Right Click <c:#AAAAAA>for take all"
    ),
    COMMAND_VAULT_NOT_FOUND("<error>Cannot find the vault <c:#FFFFFF>%vaultId%<error>."),
    COMMAND_VAULT_NO_PERMISSION("<error>You do not have permission to view se vault."),
    COMMAND_VAULT_SET_SLOT("<success>You just defined the slot of <c:#FFFFFF>%player% <success>to <c:#AAAAAA>%slots%<success>."),
    COMMAND_VAULT_ADD_SLOT("<success>you've just added <c:#AAAAAA>%amount% <success>slot to <c:#FFFFFF>%player%<success>."),
    COMMAND_VAULT_INFO("<success>Vault info for <c:#FFFFFF>%player%<success>: max <c:#AAAAAA>%max%<success>, permission <c:#AAAAAA>%permissionSlots%<success>, plugin <c:#AAAAAA>%pluginSlots%<success>, used <c:#AAAAAA>%used%<success>, free <c:#AAAAAA>%available%<success>."),
    COMMAND_VAULT_GET_SLOT_EMPTY("<error>No item was found in vault <c:#FFFFFF>%vault% <error>slot <c:#FFFFFF>%slot% <error>for <c:#FFFFFF>%player%<error>."),
    COMMAND_VAULT_GET_SLOT_SUCCESS("<success>Vault slot for <c:#FFFFFF>%player%<success>: vault <c:#AAAAAA>%vault%<success>, slot <c:#AAAAAA>%slot%<success>, amount <c:#AAAAAA>%amount%<success>, item <c:#FFFFFF>%item%<success>."),
    COMMAND_VAULT_DELETE_SLOT_SUCCESS("<success>You just deleted slot <c:#AAAAAA>%slot% <success>from vault <c:#AAAAAA>%vault% <success>for <c:#FFFFFF>%player%<success>."),
    COMMAND_VAULT_DELETE_SLOT_EMPTY("<error>Cannot find slot <c:#FFFFFF>%slot% <error>in vault <c:#FFFFFF>%vault% <error>for <c:#FFFFFF>%player%<error>."),
    COMMAND_VAULT_CHANGE_ICON_ERROR("<error>You have an item in your hand."),
    COMMAND_VAULT_CHANGE_ICON_SUCCESS("<success>You just changed the icon of your vault."),
    COMMAND_VAULT_RESET_ICON("<success>You just reset the icon of your vault."),
    COMMAND_VAULT_RENAME_START("<success>Please write in the chat the new name of the vault."),
    COMMAND_VAULT_RENAME_SUCCESS("<success>You just set the vault name to <c:#FFFFFF>%name%<success>."),
    COMMAND_VAULT_RENAME_ERROR("<error>You cannot use this name for your vault."),
    COMMAND_VAULT_RENAME_RESET("<success>You just reset the name of your vault."),

    COMMAND_ENCHANT_INVALID("<error>You need to specify a valid player."),
    COMMAND_ENCHANT_ERROR_ENCHANT("<error>Impossible to find the enchantment <c:#FFFFFF>%enchant%<error>."),
    COMMAND_ENCHANT_ERROR_ITEM_SELF("<error>You must have an item in your hand."),
    COMMAND_ENCHANT_ERROR_ITEM_PLAYER("<error>%player% has no item in his hand."),
    COMMAND_ENCHANT_REMOVE_SELF("<success>The enchantment <c:#FFFFFF>%enchant% <success>has been removed from your item in hand."),
    COMMAND_ENCHANT_REMOVE_PLAYER("<success>The enchantment <c:#FFFFFF>%enchant% <success>has been removed from the item from <c:#FFFFFF>%player%’s<succes> hand."),
    COMMAND_ENCHANT_SUCCESS_SELF("<success>The enchantment <c:#FFFFFF>%enchant% <success>has been applied to your item in hand."),
    COMMAND_ENCHANT_SUCCESS_PLAYER("<success>The enchantment <c:#FFFFFF>%enchant% <success>has been applied to the item from <c:#FFFFFF>%player%’s<succes> hand."),

    COMMAND_SUDO_ERROR("<error>You cannot force the player to execute commands."),
    COMMAND_SUDO_COMMAND("<success>You just forced the player <c:#FFFFFF>%player% <success>to execute the command <c:#FFFFFF>%command%<success>."),
    COMMAND_SUDO_MESSAGE("<success>You just forced the player <c:#FFFFFF>%player% <success>to send the message <c:#FFFFFF>%message%<success>."),

    COMMAND_WORLDEDIT_GIVE_ERROR("<error>Unable to find item <c:#FFFFFF>%name%<error>"),
    COMMAND_WORLDEDIT_GIVE_SENDER("<success>You just gave <c:#FFFFFF>%item% <success>to the player <c:#FFFFFF>%player%<success>."),
    COMMAND_WORLDEDIT_GIVE_RECEIVER("<success>you just received <c:#FFFFFF>%item%<success>."),
    COMMAND_WORLDEDIT_MATERIAL_NOT_FOUND("<error>Unable to find the material <c:#FFFFFF>%material%<error>."),
    COMMAND_WORLDEDIT_CONFIRM_PRICE(
            "",
            "<success>Price<c:#555555>: <c:#FFFFFF>%price%",
            "<success>Blocks<c:#555555>: <c:#FFFFFF>%materials%",
            "<success>Duration<c:#555555>: <c:#FFFFFF>%duration% <c:#555555>(<c:#AAAAAA>%blocks% at %speed% block%s%/s)",
            "",
            "<c:#45FF55><hover:show_text:'<c:#FFFFFF>Click to confirm action'><click:run_command:'/pw confirm'>ᴄʟɪᴄᴋ ʜᴇʀᴇ ᴛᴏ ᴄᴏɴғɪʀᴍ</click></hover><c:#AAAAAA>"
    ),
    COMMAND_WORLDEDIT_CONFIRM_PRICE_CUT(
            "",
            "<success>Price<c:#555555>: <c:#FFFFFF>%price%",
            "<success>Duration<c:#555555>: <c:#FFFFFF>%duration% <c:#555555>(<c:#AAAAAA>%blocks% at %speed% block%s%/s)",
            "",
            "<c:#45FF55><hover:show_text:'<c:#FFFFFF>Click to confirm action'><click:run_command:'/pw confirm'>ᴄʟɪᴄᴋ ʜᴇʀᴇ ᴛᴏ ᴄᴏɴғɪʀᴍ</click></hover><c:#AAAAAA>"
    ),
    COMMAND_WORLDEDIT_CONFIRM_MATERIAL("<hover:show_text:'<c:#FFFFFF>Amount<c:#555555>: <c:#55FFFF>%amount%<newline><c:#FFFFFF>Total Price<c:#555555>: <c:#55FFFF>%price%<newline><c:#FFFFFF>Price per block<c:#555555>: <c:#55FFFF>%price-per-block%'><lang:%translation-key%></hover>"),
    COMMAND_WORLDEDIT_CONFIRM_ERROR("<error>You can’t do that now."),

    COMMAND_WORLDEDIT_STOP_EMPTY("<error>You have no current edition."),
    COMMAND_WORLDEDIT_STOP_ERROR("<error>You cannot stop editing currently."),
    COMMAND_WORLDEDIT_ERROR_ITEM("<error>You must have the worldedit tool in your hand."),
    COMMAND_WORLDEDIT_ERROR_MAX("<error>You can only use this tool, it has been used to the maximum."),
    COMMAND_WORLDEDIT_OPTION_INVENTORY_ENABLE("<success>You have just activated worldedit to use your inventory."),
    COMMAND_WORLDEDIT_OPTION_INVENTORY_DISABLE("<error>You just disabled worldedit to use your inventory."),
    COMMAND_WORLDEDIT_OPTION_BOSSBAR_ENABLE("<success>You have just activated worldedit progress bar."),
    COMMAND_WORLDEDIT_OPTION_BOSSBAR_DISABLE("<error>You just disabled worldedit worldedit progress bar."),
    COMMAND_WORLDEDIT_CANCEL("<success>You have just cancelled your current selection."),

    WORLDEDIT_SELECTION_ERROR("<error>You must select two positions before you make this command."),
    WORLDEDIT_SELECTION_VOLUME("<error>You cannot change more than <c:#FFFFFF>%blocks% <error>at the same time."),
    WORLDEDIT_SELECTION_DISTANCE("<error>The distance between your two points in your selection is too large. <c:#555555>(<c:#AAAAAA>Maximum <c:#FFFFFF>%distance% <c:#AAAAAA>blocks<c:#555555>)"),
    WORLDEDIT_ALREADY_RUNNING("<error>You already have an edit in progress, please wait until it is finished."),
    WORLDEDIT_NOT_ENOUGH_MONEY("<error>You don’t have enough money to make this edition."),
    WORLDEDIT_NOT_ENOUGH_ITEMS("<error>You don’t have the items in your inventory, you can’t do that."),
    WORLDEDIT_SELECTION_POS1("<success>You have just defined the first position."),
    WORLDEDIT_SELECTION_POS2("<success>You have just defined the second position."),
    WORLDEDIT_SPEED_ERROR("<error>You have no permission to set speed per second."),
    WORLDEDIT_PERMISSION_ERROR("<error>You cannot use the worldedit here."),

    WORLDEDIT_START_CALCULATE_PRICE("<c:#AAAAAA>Price calculation in progress, please wait..."),
    WORLDEDIT_START_CHECK_INVENTORY("<c:#AAAAAA>Checking that you have enough items in your inventory, please wait..."),
    WORLDEDIT_START_RUNNING("<c:#AAAAAA>Launch of current edition, please wait..."),
    WORLDEDIT_FINISH("<success>Edition completed !"),
    WORLDEDIT_REFUND(
            "<success>ʏᴏᴜ'ᴠᴇ ᴊᴜsᴛ ʙᴇᴇɴ ʀᴇғᴜɴᴅ",
            "",
            "<success>Price<c:#555555>: <c:#FFFFFF>%price%",
            "<success>Blocks<c:#555555>:%materials%"
    ),
    WORLDEDIT_REFUND_MATERIAL(" <c:#FFFFFF><hover:show_text:'<c:#FFFFFF>Amount<c:#555555>: <c:#55FFFF>%amount%<newline><c:#FFFFFF>Total Price<c:#555555>: <c:#55FFFF>%price%<newline><c:#FFFFFF>Price per block<c:#555555>: <c:#55FFFF>%price-per-block%'><lang:%translation-key%></hover><c:#AAAAAA>"),
    WORLDEDIT_REFUND_EMPTY("<error>No block to refund"),
    WORLDEDIT_BOSSBAR("<c:#45FF45>Time remaining<c:#555555>: <c:#FFFFFF>%time%"),

    EXPERIENCE_TAKEN("<success>You have just taken <c:#FFFFFF>%amount% %type% <success>to <c:#FFFFFF>%player%<success>."),
    EXPERIENCE_GRANTED("<success>You have just given <c:#FFFFFF>%amount% %type% <success>to <c:#FFFFFF>%player%<success>."),
    EXPERIENCE_SETTED("<success>You have just set <c:#FFFFFF>%amount% %type% <success>to <c:#FFFFFF>%player%<success>."),
    EXPERIENCE_QUERIED("<success><c:#FFFFFF>%player% <success>have <c:#FFFFFF>%amount% <success>%type%."),
    DESCRIPTION_EXPERIENCE_QUERY("Query player experience"),
    DESCRIPTION_EXPERIENCE_SET("Set player experience"),
    DESCRIPTION_EXPERIENCE_GRANT("Grant experience to player"),
    DESCRIPTION_EXPERIENCE_TAKE("Take experience to player"),
    RANDOM_WORD_CLEAR("<success>The random word has been cleared."),


    COMMAND_LINK_ACCOUNT_ALREADY_LINKED("<error>You are already linked to an account."),
    COMMAND_LINK_ACCOUNT_INVALID_CODE("<error>Invalid code."),
    COMMAND_LINK_ACCOUNT_SUCCESS("<success>You have just linked your account <c:#FFFFFF>%discord%<success>."),
    COMMAND_LINK_ACCOUNT_DISABLED("<error>You cannot link your account at this time."),
    COMMAND_LINK_ACCOUNT_NOT_LINKED("<error>You are not linked to an account."),
    COMMAND_LINK_ACCOUNT_RESET("<success>You have just reset your account link."),

    COMMAND_PUB_PATTERN_ERROR("<error>Your message contains forbidden characters."),
    COMMAND_PUB(MessageType.WITHOUT_PREFIX, "<c:#555555>[<c:#FFFFFF>PUB<c:#555555>] <c:#FFFFFF>%player%<c:#555555>: <c:#FF5555>%message%"),
    STEP_DOESNT_EXIST("<error>Step <c:#FFFFFF>%step% <error>does not exist."),
    STEP_ALREADY_EXIST("<error>Step <c:#FFFFFF>%step% <error>already started."),
    STEP_DOESNT_STARTED("<error>Step <c:#FFFFFF>%step% <error>doesn't started."),

    // ItemFrame
    DESCRIPTION_ITEMFRAME("Toggle item frame visibility"),
    COMMAND_ITEMFRAME_INVISIBLE("<success>Item frame is now <c:#FFFFFF>invisible<success>."),
    COMMAND_ITEMFRAME_VISIBLE("<success>Item frame is now <c:#FFFFFF>visible<success>."),
    COMMAND_ITEMFRAME_NOT_FOUND("<error>No item frame found. Look at an item frame and try again."),

    // Death Message Module
    DESCRIPTION_DEATH_MESSAGE("Custom death messages"),

    DEATH_GENERIC("<c:#C084FC>☠ <c:#E9D5FF>%player% #B794F4died."),
    DEATH_BY_PLAYER("<c:#C084FC>⚔ <c:#E9D5FF>%player% <c:#B794F4>was slain by <c:#E9D5FF>%killer%<c:#B794F4>."),
    DEATH_BY_MOB("<c:#C084FC>☠ <c:#E9D5FF>%player% <c:#B794F4>was killed by <c:#E9D5FF>%killer%<c:#B794F4>."),
    DEATH_BY_PROJECTILE("<c:#C084FC>🏹 <c:#E9D5FF>%player% <c:#B794F4>was shot by <c:#E9D5FF>%killer%<c:#B794F4>."),
    DEATH_BY_MOB_EXPLOSION("<c:#C084FC>💥 <c:#E9D5FF>%player% <c:#B794F4>was blown up by <c:#E9D5FF>%killer%<c:#B794F4>."),
    DEATH_BY_FALL("<c:#C084FC>⬇ <c:#E9D5FF>%player% #B794F4fell from a high place."),
    DEATH_BY_FIRE("<c:#C084FC>🔥 <c:#E9D5FF>%player% #B794F4burned to death."),
    DEATH_BY_LAVA("<c:#C084FC>🌋 <c:#E9D5FF>%player% <c:#B794F4>tried to swim in lava."),
    DEATH_BY_DROWNING("<c:#C084FC>💧 <c:#E9D5FF>%player% #B794F4drowned."),
    DEATH_BY_EXPLOSION("<c:#C084FC>💥 <c:#E9D5FF>%player% <c:#B794F4>was blown up."),
    DEATH_BY_LIGHTNING("<c:#C084FC>⚡ <c:#E9D5FF>%player% <c:#B794F4>was struck by lightning."),
    DEATH_BY_SUFFOCATION("<c:#C084FC>▓ <c:#E9D5FF>%player% <c:#B794F4>suffocated in a wall."),
    DEATH_BY_STARVATION("<c:#C084FC>🍖 <c:#E9D5FF>%player% <c:#B794F4>starved to death."),
    DEATH_BY_POISON("<c:#C084FC>☣ <c:#E9D5FF>%player% <c:#B794F4>was poisoned."),
    DEATH_BY_MAGIC("<c:#C084FC>✦ <c:#E9D5FF>%player% <c:#B794F4>was killed by magic."),
    DEATH_BY_WITHER("<c:#C084FC>💀 <c:#E9D5FF>%player% <c:#B794F4>withered away."),
    DEATH_BY_FALLING_BLOCK("<c:#C084FC>▼ <c:#E9D5FF>%player% <c:#B794F4>was squashed by a falling block."),
    DEATH_BY_FLY_INTO_WALL("<c:#C084FC>💨 <c:#E9D5FF>%player% #B794F4experienced kinetic energy."),
    DEATH_BY_VOID("<c:#C084FC>✧ <c:#E9D5FF>%player% #B794F4fell out of the world."),
    DEATH_BY_CACTUS("<c:#C084FC>🌵 <c:#E9D5FF>%player% <c:#B794F4>was pricked to death."),
    DEATH_BY_CRAMMING("<c:#C084FC>⊞ <c:#E9D5FF>%player% <c:#B794F4>was squished too much."),
    DEATH_BY_FREEZE("<c:#C084FC>❄ <c:#E9D5FF>%player% #B794F4froze to death."),
    DEATH_BY_SONIC_BOOM("<c:#C084FC>◈ <c:#E9D5FF>%player% <c:#B794F4>was obliterated by a sonic boom."),
    DEATH_BY_MAGMA("<c:#C084FC>♨ <c:#E9D5FF>%player% #B794F4discovered that the floor was lava."),
    DEATH_BY_THORNS("<c:#C084FC>✵ <c:#E9D5FF>%player% <c:#B794F4>was killed trying to hurt someone."),
    DEATH_BY_CAMPFIRE("<c:#C084FC>🔥 <c:#E9D5FF>%player% <c:#B794F4>walked into a campfire."),

    DEATH_HOVER_DETAILS("<c:#D8B4FE>☠ Death Location<c:#B794F4>:\n<c:#E9D5FF>▸ World: <c:#D8B4FE>%world%\n<c:#E9D5FF>▸ Position: <c:#D8B4FE>%x%<c:#E9D5FF>, <c:#D8B4FE>%y%<c:#E9D5FF>, <c:#D8B4FE>%z%\n\n<c:#9F7AEA><italic>Click to teleport"),

    COMMAND_DEATH_MESSAGE_TOGGLE_DISABLE("<c:#C084FC>☠ #B794F4Death messages are now <c:#E9D5FF>hidden #B794F4for %player%<c:#B794F4>."),
    COMMAND_DEATH_MESSAGE_TOGGLE_ENABLE("<c:#C084FC>☠ #B794F4Death messages are now <c:#E9D5FF>visible #B794F4for %player%<c:#B794F4>."),
    DESCRIPTION_DEATH_MESSAGE_TOGGLE("Toggle death messages visibility"),

    // Death messages used by DeathMessageModule (fallback messages when no custom config is set)
    DEATH_MESSAGE_GENERIC("<c:#99E0FF>%player% <c:#AAAAAA>died."),
    DEATH_MESSAGE_PLAYER("<c:#99E0FF>%player% <c:#AAAAAA>was slain by <c:#34CFE0>%killer% <c:#AAAAAA>using %weapon%<c:#AAAAAA>."),
    DEATH_MESSAGE_MOB("<c:#99E0FF>%player% <c:#AAAAAA>was killed by <c:#FF5555>%mob%<c:#AAAAAA>."),
    DEATH_MESSAGE_MYTHIC_MOB("<c:#99E0FF>%player% <c:#AAAAAA>was slain by <gradient:#ff6600:#ff0000>%mob%</gradient><c:#AAAAAA>!"),
    DEATH_MESSAGE_FISTS("<c:#AAAAAA>fists"),
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