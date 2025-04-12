package fr.maxlego08.essentials.api.commands;

/**
 * Represents permissions related to commands.
 * This enum provides methods to generate permission strings based on its constants.
 */
public enum Permission {

    ESSENTIALS_USE,
    ESSENTIALS_RELOAD,
    ESSENTIALS_DELETE_WORLD,
    ESSENTIALS_CONVERT,
    ESSENTIALS_GAMEMODE,
    ESSENTIALS_GAMEMODE_OTHER,
    ESSENTIALS_GAMEMODE_CREATIVE,
    ESSENTIALS_GAMEMODE_SURVIVAL,
    ESSENTIALS_GAMEMODE_SPECTATOR,
    ESSENTIALS_GAMEMODE_ADVENTURE,
    ESSENTIALS_DAY,
    ESSENTIALS_NIGHT,
    ESSENTIALS_SUN,
    ESSENTIALS_ENDERCHEST,
    ESSENTIALS_ENDERSEE,
    ESSENTIALS_ENDERSEE_OFFLINE("Allows you to see the enderchest of an offline player"),
    ESSENTIALS_TOP,
    ESSENTIALS_GOD,
    ESSENTIALS_HEAL,
    ESSENTIALS_SPEED,
    ESSENTIALS_TELEPORT_BYPASS("Allows to bypass the waiting time for teleportation"),
    ESSENTIALS_TPA,
    ESSENTIALS_TPA_HERE,
    ESSENTIALS_TPA_ACCEPT,
    ESSENTIALS_TPA_DENY,
    ESSENTIALS_TPA_CANCEL,
    ESSENTIALS_BYPASS_COOLDOWN("Allows not to have a cooldown on all commands"),
    ESSENTIALS_MORE,
    ESSENTIALS_TP_WORLD,
    ESSENTIALS_TP_WORLD_OTHER("Allows to teleport another player into a world"),
    ESSENTIALS_TRASH,
    ESSENTIALS_FEED,
    ESSENTIALS_INVSEE,
    ESSENTIALS_INVSEE_OFFLINE("Allows to listen to the inventory of an offline player"),
    ESSENTIALS_FEED_OTHER("Allows to feed another player"),
    ESSENTIALS_HEAL_OTHER("Allows to heal another player"),
    ESSENTIALS_CRAFT,
    ESSENTIALS_ENCHANTING,
    ESSENTIALS_INVSEE_INTERACT("Allows to interact with a player’s inventory"),
    ESSENTIALS_CLEARINVENTORY,
    ESSENTIALS_CLEARINVENTORY_OTHER("Allows to clear the inventory of another player"),
    ESSENTIALS_COMPACT,
    ESSENTIALS_HAT,
    ESSENTIALS_PLAYER_WEATHER,
    ESSENTIALS_PLAYER_TIME,
    ESSENTIALS_TP,
    ESSENTIALS_ECO_USE,
    ESSENTIALS_ECO_GIVE,
    ESSENTIALS_ECO_TAKE,
    ESSENTIALS_ECO_GIVE_ALL,
    ESSENTIALS_MONEY,
    ESSENTIALS_MONEY_OTHER,
    ESSENTIALS_ECO_SET,
    ESSENTIALS_ECO_RESET,
    ESSENTIALS_ECO_SHOW,
    ESSENTIALS_PAY,
    ESSENTIALS_TP_HERE,
    ESSENTIALS_FLY,
    ESSENTIALS_FLY_BYPASS_WORLD("Allows to activate fly even in the world where the fly is disabled"),
    ESSENTIALS_FLY_ADD,
    ESSENTIALS_FLY_GET,
    ESSENTIALS_FLY_INFO,
    ESSENTIALS_FLY_REMOVE,
    ESSENTIALS_FLY_SET,
    ESSENTIALS_FLY_UNLIMITED("Allows to have the fly unlimited"),
    ESSENTIALS_FLY_OTHER("Allows to activate the fly for another player"),
    ESSENTIALS_SILENT_QUIT("Allows you to leave the server silently"),
    ESSENTIALS_SILENT_JOIN("Allows you to join the server silently"),
    ESSENTIALS_ANVIL,
    ESSENTIALS_CARTOGRAPHYTABLE,
    ESSENTIALS_GRINDSTONE,
    ESSENTIALS_LOOM,
    ESSENTIALS_STONECUTTER,
    ESSENTIALS_SMITHINGTABLE,
    ESSENTIALS_BACK,
    ESSENTIALS_BACK_DEATH("Grant this permission to allow players to use the /back command to return to their death location."),
    ESSENTIALS_SET_SPAWN,
    ESSENTIALS_SPAWN,
    ESSENTIALS_WARP_SET,
    ESSENTIALS_WARP,
    ESSENTIALS_WARP_("Allows to teleport to a specific warp", "warp name"),
    ESSENTIALS_WARPS,
    ESSENTIALS_WARP_DEL,
    ESSENTIALS_TP_RANDOM,
    ESSENTIALS_SET_HOME,
    ESSENTIALS_SET_HOME_CONFIRM,
    ESSENTIALS_SET_HOME_OTHER("Allows to set another player’s home"),
    ESSENTIALS_HOME,
    ESSENTIALS_HOME_OTHER("Allows to teleport to another player’s home"),
    ESSENTIALS_DEL_HOME,
    ESSENTIALS_DEL_HOME_CONFIRM,
    ESSENTIALS_FREEZE,
    ESSENTIALS_KICK,
    ESSENTIALS_KICK_NOTIFY,
    ESSENTIALS_KICK_ALL,
    ESSENTIALS_KICK_BYPASS_ALL("Allows not to be kicked during the kickall"),
    ESSENTIALS_KITTY_CANNON,
    ESSENTIALS_MUTE,
    ESSENTIALS_BAN,
    ESSENTIALS_UNBAN,
    ESSENTIALS_UNMUTE,
    ESSENTIALS_WARN,
    ESSENTIALS_BAN_NOTIFY("Allows you to receive notifications when a player is banned"),
    ESSENTIALS_MUTE_NOTIFY("Allows you to receive notifications when a player is muted"),
    ESSENTIALS_UNMUTE_NOTIFY("Allows you to receive notifications when a player is unmuted"),
    ESSENTIALS_UNBAN_NOTIFY("Allows you to receive notifications when a player is unbanned"),
    ESSENTIALS_UN_MUTE,
    ESSENTIALS_UN_BAN,
    ESSENTIALS_SANCTION,
    ESSENTIALS_CHAT_BYPASS_ALPHANUMERIC("Allows not to be affected by the limitation of characters"),
    ESSENTIALS_CHAT_BYPASS_DYNAMIC_COOLDOWN("Allows not to be affected by dynamic cooldown"),
    ESSENTIALS_CHAT_BYPASS_LINK("Allows to send a message with a link"),
    ESSENTIALS_CHAT_BYPASS_SAME_MESSAGE("Allows to send a message even if it is the same as the previous one"),
    ESSENTIALS_CHAT_BYPASS_CAPS("Allows you to send a message even if it contains uppercase letters"),
    ESSENTIALS_CHAT_BYPASS_FLOOD("Allows to send a message even if it is floods"),
    ESSENTIALS_CHAT_BYPASS_DISABLE("Allows to send a message in the chat even if it is disabled"),
    ESSENTIALS_CHAT_MODERATOR("Allows you to receive messages from players with the vision of a moderator"),
    ESSENTIALS_CHAT_COLOR("Allows you to use the color (minecraft color and hex color) mini message format"),
    ESSENTIALS_CHAT_DECORATION("Allows you to use the decoration (<bold>, <italic>, <underlined>, <strikethrough> and <obfuscated>) mini message format"),
    ESSENTIALS_CHAT_RAINBOW("Allows you to use the <rainbow> mini message format"),
    ESSENTIALS_CHAT_GRADIENT("Allows you to use the <gradient mini message format"),
    ESSENTIALS_CHAT_CLICK("Allows you to use the <click> mini message format"),
    ESSENTIALS_CHAT_HOVER("Allows you to use the <hover> mini message format"),
    ESSENTIALS_CHAT_NEWLINE("Allows you to use the <newline> mini message format"),
    ESSENTIALS_CHAT_RESET("Allows you to use the <reset> mini message format"),
    ESSENTIALS_CHAT_FONT("Allows you to use the <font> mini message format"),
    ESSENTIALS_CHAT_KEYBIND("Allows you to use the <key> mini message format"),
    ESSENTIALS_CHAT_LINK("Allows to transform the links in the chat into clickable link"),
    ESSENTIALS_CHAT_HISTORY,
    ESSENTIALS_CHAT_CLEAR,
    ESSENTIALS_CHAT_ENABLE,
    ESSENTIALS_CHAT_DISABLE,
    ESSENTIALS_CHAT_BROADCAST,
    ESSENTIALS_MESSAGE,
    ESSENTIALS_REPLY,
    ESSENTIALS_MESSAGE_TOGGLE,
    ESSENTIALS_SOCIALSPY,
    ESSENTIALS_COOLDOWN_COMMAND_BYPASS,
    ESSENTIALS_COMPACT_ALL,
    ESSENTIALS_FURNACE,
    ESSENTIALS_SKULL,
    ESSENTIALS_BOTTOM,
    ESSENTIALS_REPAIR,
    ESSENTIALS_REPAIR_ALL,
    ESSENTIALS_REPAIR_ALL_OTHER("Repair all items of a player"),
    ESSENTIALS_EXT,
    ESSENTIALS_NEAR,
    ESSENTIALS_PLAY_TIME,
    ESSENTIALS_KILL_ALL,
    ESSENTIALS_SEEN,
    ESSENTIALS_SEEN_IP,
    ESSENTIALS_KIT,
    ESSENTIALS_KIT_("Give a kit to a player", "kit name"),
    ESSENTIALS_KIT_BYPASS_COOLDOWN("Bypass the cooldown of the kits"),
    ESSENTIALS_KIT_EDITOR,
    ESSENTIALS_KIT_CREATE,
    ESSENTIALS_COOLDOWN,
    ESSENTIALS_KIT_DELETE,
    ESSENTIALS_COOLDOWN_DELETE,
    ESSENTIALS_COOLDOWN_CREATE,
    ESSENTIALS_ITEM_NAME,
    ESSENTIALS_ITEM_LORE,
    ESSENTIALS_ITEM_LORE_ADD,
    ESSENTIALS_ITEM_LORE_SET,
    ESSENTIALS_PAY_TOGGLE,
    ESSENTIALS_PAY_TOGGLE_OTHER,
    ESSENTIALS_MESSAGE_TOGGLE_OTHER,
    ESSENTIALS_GIVE,
    ESSENTIALS_GIVE_ALL,
    ESSENTIALS_ITEM_LORE_CLEAR,
    ESSENTIALS_POWER_TOOLS,
    ESSENTIALS_POWER_TOOLS_TOGGLE,
    ESSENTIALS_POWER_TOOLS_TOGGLE_OTHER,
    ESSENTIALS_KIT_SHOW,
    ESSENTIALS_MAIL,
    ESSENTIALS_TP_ALL,
    ESSENTIALS_RULES,
    ESSENTIALS_HOLOGRAM,
    ESSENTIALS_HOLOGRAM_CREATE,
    ESSENTIALS_HOLOGRAM_DELETE,
    ESSENTIALS_HOLOGRAM_ADD_LINE,
    ESSENTIALS_HOLOGRAM_SET_LINE,
    ESSENTIALS_HOLOGRAM_REMOVE_LINE,
    ESSENTIALS_HOLOGRAM_SCALE,
    ESSENTIALS_HOLOGRAM_TRANSLATION,
    ESSENTIALS_HOLOGRAM_MOVE_HERE,
    ESSENTIALS_HOLOGRAM_BILLBOARD,
    ESSENTIALS_HOLOGRAM_TEXT_ALIGNMENT,
    ESSENTIALS_HOLOGRAM_YAW,
    ESSENTIALS_HOLOGRAM_MOVE_TO,
    ESSENTIALS_HOLOGRAM_INSERT_BEFORE_LINE,
    ESSENTIALS_HOLOGRAM_INSERT_AFTER_LINE,
    ESSENTIALS_HOLOGRAM_TEXT_BACKGROUND,
    ESSENTIALS_HOLOGRAM_LIST,
    ESSENTIALS_HOLOGRAM_TELEPORT,
    ESSENTIALS_HOLOGRAM_SEE_THROUGH,
    ESSENTIALS_HOLOGRAM_TEXT_SHADOW,
    ESSENTIALS_HOLOGRAM_SHADOW_STRENGTH,
    ESSENTIALS_HOLOGRAM_SHADOW_RADIUS,
    ESSENTIALS_SCOREBOARD,
    ESSENTIALS_BALANCE_TOP,
    ESSENTIALS_BALANCE_TOP_REFRESH,
    ESSENTIALS_MAIL_OPEN,
    ESSENTIALS_VOTEPARTY_USE,
    ESSENTIALS_VOTEPARTY_SET,
    ESSENTIALS_VOTEPARTY_REMOVE,
    ESSENTIALS_VOTEPARTY_ADD,
    ESSENTIALS_VOTE_USE,
    ESSENTIALS_VOTE_SET,
    ESSENTIALS_VOTE_REMOVE,
    ESSENTIALS_VOTE_ADD,
    ESSENTIALS_VAULT_USE,
    ESSENTIALS_VAULT_SET_SLOT,
    ESSENTIALS_VAULT_ADD_SLOT,
    ESSENTIALS_VAULT_GIVE,
    ESSENTIALS_ENCHANT,
    ESSENTIALS_NIGHTVISION,
    ESSENTIALS_SUDO,
    ESSENTIALS_SUDO_BYPASS,
    ESSENTIALS_MAIL_GIVE,
    ESSENTIALS_MAIL_CLEAR,
    ESSENTIALS_MAIL_GIVEALL,
    ESSENTIALS_WORLDEDIT_USE,
    ESSENTIALS_WORLDEDIT_GIVE,
    ESSENTIALS_WORLDEDIT_SET,
    ESSENTIALS_WORLDEDIT_CONFIRM,
    ESSENTIALS_WORLDEDIT_CANCEL,
    ESSENTIALS_WORLDEDIT_CUT,
    ESSENTIALS_WORLDEDIT_FILL,
    ESSENTIALS_WORLDEDIT_STOP,
    ESSENTIALS_WORLDEDIT_WALLS,
    ESSENTIALS_WORLDEDIT_SPHERE,
    ESSENTIALS_WORLDEDIT_POS1,
    ESSENTIALS_WORLDEDIT_POS2,
    ESSENTIALS_WORLDEDIT_CYL,
    ESSENTIALS_WORLDEDIT_OPTION_INVENTORY,
    ESSENTIALS_WORLDEDIT_OPTION,
    ESSENTIALS_WORLDEDIT_OPTION_BOSSBAR,
    ESSENTIALS_SHOW_ITEM,
    ESSENTIALS_EXPERIENCE,
    ESSENTIALS_EXPERIENCE_GRANT,
    ESSENTIALS_EXPERIENCE_QUERY,
    ESSENTIALS_EXPERIENCE_SET,
    ESSENTIALS_SPAWN_OTHER,
    ESSENTIALS_FLY_SAFELOGIN("Players with this permission will automatically enter fly mode upon logging in if they are suspended in the air."),
    ESSENTIALS_SUICIDE,
    ESSENTIALS_ECO_GIVE_RANDOM,
    ESSENTIALS_CLEAR_RANDOM_WORD,
    ESSENTIALS_DISCORD_LINK,
    ESSENTIALS_DISCORD_UNLINK,
    ESSENTIALS_PUB,
    ESSENTIALS_STEP,
    ESSENTIALS_HOME_LIST,
    ESSENTIALS_AFK_BYPASS("Allows to not be affected by the anti-AFK"),
    ESSENTIALS_SET_FIRST_SPAWN,
    ESSENTIALS_SPAWN_FIRST,
    ESSENTIALS_EXPERIENCE_TAKE,
    ESSENTIALS_SIGN_COLOR("Change sign color");

    private final String description;
    private final String[] args;

    Permission() {
        this.description = "";
        this.args = new String[0];
    }

    Permission(String description, String... args) {
        this.description = description;
        this.args = args;
    }

    /**
     * Generates the permission string for this enum constant.
     * Converts the enum constant name to lowercase and replaces underscores with periods.
     *
     * @return The permission string.
     */
    public String asPermission() {
        return name().toLowerCase().replace("_", ".");
    }

    public String toPermission() {
        StringBuilder builder = new StringBuilder( asPermission());
        for (int i = 0; i < this.args.length; i++) {
            builder.append("<");
            builder.append(this.args[i]);
            builder.append(">");
            if (i < this.args.length - 1) {
                builder.append(".");
            }
        }
        return builder.toString();
    }

    /**
     * Generates a permission string for this enum constant with an additional suffix.
     * Appends the specified suffix to the permission string generated by {@link #asPermission()}.
     *
     * @param with The suffix to append.
     * @return The permission string with the suffix.
     */
    public String asPermission(String with) {
        return asPermission() + with;
    }

    public String getDescription() {
        return description;
    }

    public String[] getArgs() {
        return args;
    }
}