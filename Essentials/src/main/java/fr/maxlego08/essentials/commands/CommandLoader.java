package fr.maxlego08.essentials.commands;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandManager;
import fr.maxlego08.essentials.commands.commands.chat.CommandChatBroadcast;
import fr.maxlego08.essentials.commands.commands.chat.CommandChatClear;
import fr.maxlego08.essentials.commands.commands.chat.CommandChatDisable;
import fr.maxlego08.essentials.commands.commands.chat.CommandChatEnable;
import fr.maxlego08.essentials.commands.commands.chat.CommandChatHistory;
import fr.maxlego08.essentials.commands.commands.economy.CommandEconomy;
import fr.maxlego08.essentials.commands.commands.economy.CommandMoney;
import fr.maxlego08.essentials.commands.commands.economy.CommandPay;
import fr.maxlego08.essentials.commands.commands.enderchest.CommandEnderChest;
import fr.maxlego08.essentials.commands.commands.enderchest.CommandEnderSee;
import fr.maxlego08.essentials.commands.commands.gamemode.CommandGameMode;
import fr.maxlego08.essentials.commands.commands.gamemode.CommandGameModeAdventure;
import fr.maxlego08.essentials.commands.commands.gamemode.CommandGameModeCreative;
import fr.maxlego08.essentials.commands.commands.gamemode.CommandGameModeSpectator;
import fr.maxlego08.essentials.commands.commands.gamemode.CommandGameModeSurvival;
import fr.maxlego08.essentials.commands.commands.home.CommandDelHome;
import fr.maxlego08.essentials.commands.commands.home.CommandHome;
import fr.maxlego08.essentials.commands.commands.home.CommandSetHome;
import fr.maxlego08.essentials.commands.commands.messages.CommandMessage;
import fr.maxlego08.essentials.commands.commands.messages.CommandMessageToggle;
import fr.maxlego08.essentials.commands.commands.messages.CommandReply;
import fr.maxlego08.essentials.commands.commands.messages.CommandSocialSpy;
import fr.maxlego08.essentials.commands.commands.sanction.CommandBan;
import fr.maxlego08.essentials.commands.commands.sanction.CommandKick;
import fr.maxlego08.essentials.commands.commands.sanction.CommandKickAll;
import fr.maxlego08.essentials.commands.commands.sanction.CommandMute;
import fr.maxlego08.essentials.commands.commands.sanction.CommandSanction;
import fr.maxlego08.essentials.commands.commands.sanction.CommandUnBan;
import fr.maxlego08.essentials.commands.commands.sanction.CommandUnMute;
import fr.maxlego08.essentials.commands.commands.spawn.CommandSetSpawn;
import fr.maxlego08.essentials.commands.commands.spawn.CommandSpawn;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleport;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportAccept;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportBack;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportCancel;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportDeny;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportHere;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportRandom;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportTo;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportWorld;
import fr.maxlego08.essentials.commands.commands.utils.CommandAnvil;
import fr.maxlego08.essentials.commands.commands.utils.CommandBottom;
import fr.maxlego08.essentials.commands.commands.utils.CommandCartographyTable;
import fr.maxlego08.essentials.commands.commands.utils.CommandCompact;
import fr.maxlego08.essentials.commands.commands.utils.CommandCompactAll;
import fr.maxlego08.essentials.commands.commands.utils.CommandCraft;
import fr.maxlego08.essentials.commands.commands.utils.CommandEnchanting;
import fr.maxlego08.essentials.commands.commands.utils.CommandExt;
import fr.maxlego08.essentials.commands.commands.utils.CommandFeed;
import fr.maxlego08.essentials.commands.commands.utils.CommandFly;
import fr.maxlego08.essentials.commands.commands.utils.CommandFurnace;
import fr.maxlego08.essentials.commands.commands.utils.CommandGod;
import fr.maxlego08.essentials.commands.commands.utils.CommandGrindStone;
import fr.maxlego08.essentials.commands.commands.utils.CommandHat;
import fr.maxlego08.essentials.commands.commands.utils.CommandHeal;
import fr.maxlego08.essentials.commands.commands.utils.CommandInvsee;
import fr.maxlego08.essentials.commands.commands.utils.CommandKillAll;
import fr.maxlego08.essentials.commands.commands.utils.CommandKittyCannon;
import fr.maxlego08.essentials.commands.commands.utils.CommandLoom;
import fr.maxlego08.essentials.commands.commands.utils.CommandMore;
import fr.maxlego08.essentials.commands.commands.utils.CommandNear;
import fr.maxlego08.essentials.commands.commands.utils.CommandPlayTime;
import fr.maxlego08.essentials.commands.commands.utils.CommandRepair;
import fr.maxlego08.essentials.commands.commands.utils.CommandRepairAll;
import fr.maxlego08.essentials.commands.commands.utils.CommandSkull;
import fr.maxlego08.essentials.commands.commands.utils.CommandSmithingTable;
import fr.maxlego08.essentials.commands.commands.utils.CommandSpeed;
import fr.maxlego08.essentials.commands.commands.utils.CommandStoneCutter;
import fr.maxlego08.essentials.commands.commands.utils.CommandTop;
import fr.maxlego08.essentials.commands.commands.utils.CommandTrash;
import fr.maxlego08.essentials.commands.commands.utils.CommandVersion;
import fr.maxlego08.essentials.commands.commands.warp.CommandDelWarp;
import fr.maxlego08.essentials.commands.commands.warp.CommandSetWarp;
import fr.maxlego08.essentials.commands.commands.warp.CommandWarp;
import fr.maxlego08.essentials.commands.commands.warp.CommandWarps;
import fr.maxlego08.essentials.commands.commands.weather.CommandDay;
import fr.maxlego08.essentials.commands.commands.weather.CommandNight;
import fr.maxlego08.essentials.commands.commands.weather.CommandPlayerTime;
import fr.maxlego08.essentials.commands.commands.weather.CommandPlayerWeather;
import fr.maxlego08.essentials.commands.commands.weather.CommandSun;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLoader {

    private final ZEssentialsPlugin plugin;
    private final List<RegisterCommand> commands = new ArrayList<>();

    public CommandLoader(ZEssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadCommands(CommandManager commandManager) {

        register("gamemode", CommandGameMode.class, "gm");
        register("gmc", CommandGameModeCreative.class, "creat");
        register("gma", CommandGameModeAdventure.class, "advent");
        register("gms", CommandGameModeSurvival.class, "survi");
        register("gmsp", CommandGameModeSpectator.class, "spec");

        register("day", CommandDay.class);
        register("night", CommandNight.class);
        register("sun", CommandSun.class);
        register("player-weather", CommandPlayerWeather.class, "pweather");
        register("player-time", CommandPlayerTime.class, "ptime");

        register("enderchest", CommandEnderChest.class, "ec");
        register("endersee", CommandEnderSee.class, "ecsee");

        register("top", CommandTop.class);
        register("bottom", CommandBottom.class);
        register("speed", CommandSpeed.class);
        register("god", CommandGod.class);
        register("heal", CommandHeal.class);
        register("more", CommandMore.class);
        register("worldtp", CommandTeleportWorld.class, "wtp");
        register("trash", CommandTrash.class, "poubelle");
        register("feed", CommandFeed.class, "eat");
        register("craft", CommandCraft.class);
        register("enchanting", CommandEnchanting.class);
        register("invsee", CommandInvsee.class);
        register("compact", CommandCompact.class, "blocks", "condense");
        register("compactall", CommandCompactAll.class, "blocksall", "condenseall");
        register("hat", CommandHat.class);
        register("fly", CommandFly.class);
        register("anvil", CommandAnvil.class);
        register("cartographytable", CommandCartographyTable.class);
        register("grindstone", CommandGrindStone.class);
        register("loom", CommandLoom.class);
        register("stonecutter", CommandStoneCutter.class);
        register("smithingtable", CommandSmithingTable.class);
        register("furnace", CommandFurnace.class, "burn");
        register("skull", CommandSkull.class);

        register("tp", CommandTeleport.class);
        register("tphere", CommandTeleportHere.class, "s");
        register("tpa", CommandTeleportTo.class);
        register("tpaccept", CommandTeleportAccept.class, "tpyes");
        register("tpdeny", CommandTeleportDeny.class, "tpno");
        register("tpacancel", CommandTeleportCancel.class);
        register("back", CommandTeleportBack.class);
        register("tpr", CommandTeleportRandom.class, "rtp");

        register("economy", CommandEconomy.class, "eco");
        register("money", CommandMoney.class, "balance");
        register("pay", CommandPay.class);

        register("setspawn", CommandSetSpawn.class);
        register("spawn", CommandSpawn.class);

        register("setwarp", CommandSetWarp.class, "wcreate");
        register("warp", CommandWarp.class, "w");
        register("warps", CommandWarps.class, "wlist");
        register("delwarp", CommandDelWarp.class, "wdelete");

        register("sethome", CommandSetHome.class, "hcreate", "hc");
        register("delhome", CommandDelHome.class, "hdelete", "hd");
        register("home", CommandHome.class, "h");

        register("ban", CommandBan.class);
        register("mute", CommandMute.class);
        register("unmute", CommandUnMute.class);
        register("unban", CommandUnBan.class);
        register("kick", CommandKick.class);
        register("kickall", CommandKickAll.class);
        register("sanction", CommandSanction.class, "sc");

        register("kittycannon", CommandKittyCannon.class);

        register("chathistory", CommandChatHistory.class, "ct");
        register("chatclear", CommandChatClear.class, "cl");
        register("chatenable", CommandChatEnable.class, "ce");
        register("chatdisable", CommandChatDisable.class, "cd");
        register("broadcast", CommandChatBroadcast.class, "bc");

        register("message", CommandMessage.class, "msg", "tell", "whisper", "m");
        register("reply", CommandReply.class, "r");
        register("messagetoggle", CommandMessageToggle.class, "msgtoggle", "mtg");
        register("socialspy", CommandSocialSpy.class);

        register("repair", CommandRepair.class, "fix");
        register("repairall", CommandRepairAll.class, "fixall");
        register("ext", CommandExt.class);
        register("near", CommandNear.class);
        register("playtime", CommandPlayTime.class);
        register("essversion", CommandVersion.class, "ev");
        register("killall", CommandKillAll.class);

        File file = new File(plugin.getDataFolder(), "commands.yml");
        if (!file.exists()) this.plugin.saveResource("commands.yml", false);

        this.loadCommands(YamlConfiguration.loadConfiguration(file), commandManager);
    }

    private void register(String command, Class<? extends VCommand> commandClass, String... aliases) {
        commands.add(new RegisterCommand(command, commandClass, Arrays.asList(aliases)));
    }

    private void loadCommands(YamlConfiguration configuration, CommandManager commandManager) {

        for (RegisterCommand command : commands) {
            if (configuration.getBoolean(command.command, true)) {
                try {
                    commandManager.registerCommand(this.plugin, command.command, command.commandClass.getConstructor(EssentialsPlugin.class).newInstance(this.plugin), command.aliases);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public record RegisterCommand(String command, Class<? extends VCommand> commandClass, List<String> aliases) {

    }
}
