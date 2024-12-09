package fr.maxlego08.essentials.commands;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandManager;
import fr.maxlego08.essentials.commands.commands.chat.CommandChatBroadcast;
import fr.maxlego08.essentials.commands.commands.chat.CommandChatClear;
import fr.maxlego08.essentials.commands.commands.chat.CommandChatDisable;
import fr.maxlego08.essentials.commands.commands.chat.CommandChatEnable;
import fr.maxlego08.essentials.commands.commands.chat.CommandChatHistory;
import fr.maxlego08.essentials.commands.commands.chat.CommandShowItem;
import fr.maxlego08.essentials.commands.commands.clearinventory.ClearInventoryCommand;
import fr.maxlego08.essentials.commands.commands.cooldown.CommandCooldown;
import fr.maxlego08.essentials.commands.commands.economy.CommandBalanceTop;
import fr.maxlego08.essentials.commands.commands.economy.CommandEconomy;
import fr.maxlego08.essentials.commands.commands.economy.CommandMoney;
import fr.maxlego08.essentials.commands.commands.economy.CommandPay;
import fr.maxlego08.essentials.commands.commands.economy.CommandPayToggle;
import fr.maxlego08.essentials.commands.commands.enderchest.CommandEnderChest;
import fr.maxlego08.essentials.commands.commands.enderchest.CommandEnderSee;
import fr.maxlego08.essentials.commands.commands.gamemode.CommandGameMode;
import fr.maxlego08.essentials.commands.commands.gamemode.CommandGameModeAdventure;
import fr.maxlego08.essentials.commands.commands.gamemode.CommandGameModeCreative;
import fr.maxlego08.essentials.commands.commands.gamemode.CommandGameModeSpectator;
import fr.maxlego08.essentials.commands.commands.gamemode.CommandGameModeSurvival;
import fr.maxlego08.essentials.commands.commands.hologram.CommandHologram;
import fr.maxlego08.essentials.commands.commands.home.CommandDelHome;
import fr.maxlego08.essentials.commands.commands.home.CommandDelHomeConfirm;
import fr.maxlego08.essentials.commands.commands.home.CommandHome;
import fr.maxlego08.essentials.commands.commands.home.CommandSetHome;
import fr.maxlego08.essentials.commands.commands.home.CommandSetHomeConfirm;
import fr.maxlego08.essentials.commands.commands.items.CommandGive;
import fr.maxlego08.essentials.commands.commands.items.CommandGiveAll;
import fr.maxlego08.essentials.commands.commands.items.CommandItemLore;
import fr.maxlego08.essentials.commands.commands.items.CommandItemName;
import fr.maxlego08.essentials.commands.commands.kits.CommandKit;
import fr.maxlego08.essentials.commands.commands.kits.CommandKitCreate;
import fr.maxlego08.essentials.commands.commands.kits.CommandKitDelete;
import fr.maxlego08.essentials.commands.commands.kits.CommandKitEditor;
import fr.maxlego08.essentials.commands.commands.kits.CommandShowKit;
import fr.maxlego08.essentials.commands.commands.mail.CommandMail;
import fr.maxlego08.essentials.commands.commands.messages.CommandMessage;
import fr.maxlego08.essentials.commands.commands.messages.CommandMessageToggle;
import fr.maxlego08.essentials.commands.commands.messages.CommandReply;
import fr.maxlego08.essentials.commands.commands.messages.CommandSocialSpy;
import fr.maxlego08.essentials.commands.commands.sanction.*;
import fr.maxlego08.essentials.commands.commands.scoreboard.CommandScoreboard;
import fr.maxlego08.essentials.commands.commands.spawn.CommandSetSpawn;
import fr.maxlego08.essentials.commands.commands.spawn.CommandSpawn;
import fr.maxlego08.essentials.commands.commands.teleport.CommandBottom;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleport;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportAccept;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportAll;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportBack;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportCancel;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportDeny;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportHere;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportRandom;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportTo;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTeleportWorld;
import fr.maxlego08.essentials.commands.commands.teleport.CommandTop;
import fr.maxlego08.essentials.commands.commands.utils.CommandCompact;
import fr.maxlego08.essentials.commands.commands.utils.CommandCompactAll;
import fr.maxlego08.essentials.commands.commands.utils.CommandCraft;
import fr.maxlego08.essentials.commands.commands.utils.CommandExt;
import fr.maxlego08.essentials.commands.commands.utils.CommandFeed;
import fr.maxlego08.essentials.commands.commands.utils.CommandFurnace;
import fr.maxlego08.essentials.commands.commands.utils.CommandHat;
import fr.maxlego08.essentials.commands.commands.utils.CommandHeal;
import fr.maxlego08.essentials.commands.commands.utils.CommandMore;
import fr.maxlego08.essentials.commands.commands.utils.CommandNear;
import fr.maxlego08.essentials.commands.commands.utils.CommandNightVision;
import fr.maxlego08.essentials.commands.commands.utils.CommandPlayTime;
import fr.maxlego08.essentials.commands.commands.utils.CommandRepair;
import fr.maxlego08.essentials.commands.commands.utils.CommandRepairAll;
import fr.maxlego08.essentials.commands.commands.utils.CommandRules;
import fr.maxlego08.essentials.commands.commands.utils.CommandSudo;
import fr.maxlego08.essentials.commands.commands.utils.CommandSuicide;
import fr.maxlego08.essentials.commands.commands.utils.CommandTrash;
import fr.maxlego08.essentials.commands.commands.utils.CommandVersion;
import fr.maxlego08.essentials.commands.commands.utils.admins.CommandEnchant;
import fr.maxlego08.essentials.commands.commands.fly.CommandFly;
import fr.maxlego08.essentials.commands.commands.utils.admins.CommandGod;
import fr.maxlego08.essentials.commands.commands.utils.admins.CommandInvsee;
import fr.maxlego08.essentials.commands.commands.utils.admins.CommandKillAll;
import fr.maxlego08.essentials.commands.commands.utils.admins.CommandKittyCannon;
import fr.maxlego08.essentials.commands.commands.utils.admins.CommandPowerTools;
import fr.maxlego08.essentials.commands.commands.utils.admins.CommandPowerToolsToggle;
import fr.maxlego08.essentials.commands.commands.utils.admins.CommandSkull;
import fr.maxlego08.essentials.commands.commands.utils.admins.CommandSpeed;
import fr.maxlego08.essentials.commands.commands.utils.blocks.CommandAnvil;
import fr.maxlego08.essentials.commands.commands.utils.blocks.CommandCartographyTable;
import fr.maxlego08.essentials.commands.commands.utils.blocks.CommandEnchanting;
import fr.maxlego08.essentials.commands.commands.utils.blocks.CommandGrindStone;
import fr.maxlego08.essentials.commands.commands.utils.blocks.CommandLoom;
import fr.maxlego08.essentials.commands.commands.utils.blocks.CommandSmithingTable;
import fr.maxlego08.essentials.commands.commands.utils.blocks.CommandStoneCutter;
import fr.maxlego08.essentials.commands.commands.utils.experience.CommandExperience;
import fr.maxlego08.essentials.commands.commands.vault.CommandVault;
import fr.maxlego08.essentials.commands.commands.vote.CommandVote;
import fr.maxlego08.essentials.commands.commands.vote.CommandVoteParty;
import fr.maxlego08.essentials.commands.commands.warp.CommandDelWarp;
import fr.maxlego08.essentials.commands.commands.warp.CommandSetWarp;
import fr.maxlego08.essentials.commands.commands.warp.CommandWarp;
import fr.maxlego08.essentials.commands.commands.warp.CommandWarps;
import fr.maxlego08.essentials.commands.commands.weather.CommandDay;
import fr.maxlego08.essentials.commands.commands.weather.CommandNight;
import fr.maxlego08.essentials.commands.commands.weather.CommandPlayerTime;
import fr.maxlego08.essentials.commands.commands.weather.CommandPlayerWeather;
import fr.maxlego08.essentials.commands.commands.weather.CommandSun;
import fr.maxlego08.essentials.commands.commands.worldedit.CommandWorldEdit;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

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
        register("clearinventory", ClearInventoryCommand.class, "clear", "ci");
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
        register("rules", CommandRules.class, "?", "help", "aide");

        register("tp", CommandTeleport.class);
        register("tpall", CommandTeleportAll.class);
        register("tphere", CommandTeleportHere.class, "s");
        register("tpa", CommandTeleportTo.class);
        register("tpaccept", CommandTeleportAccept.class, "tpyes");
        register("tpdeny", CommandTeleportDeny.class, "tpno");
        register("tpacancel", CommandTeleportCancel.class);
        register("back", CommandTeleportBack.class);
        register("tpr", CommandTeleportRandom.class, "rtp");

        register("balancetop", CommandBalanceTop.class, "baltop");
        register("economy", CommandEconomy.class, "eco");
        register("money", CommandMoney.class, "balance");
        register("pay", CommandPay.class);
        register("paytoggle", CommandPayToggle.class);

        register("setspawn", CommandSetSpawn.class);
        register("spawn", CommandSpawn.class);

        register("setwarp", CommandSetWarp.class, "wcreate");
        register("warp", CommandWarp.class, "w");
        register("warps", CommandWarps.class, "wlist");
        register("delwarp", CommandDelWarp.class, "wdelete");

        register("sethome", CommandSetHome.class, "hcreate", "hc");
        register("sethomeconfirm", CommandSetHomeConfirm.class);
        register("delhomeconfirm", CommandDelHomeConfirm.class);
        register("delhome", CommandDelHome.class, "hdelete", "hd");
        register("home", CommandHome.class, "h", "homes");

        register("freeze", CommandFreeze.class);
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
        register("showitem", CommandShowItem.class);

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
        register("seen", CommandSeen.class, "whois");
        register("seenip", CommandSeenIp.class, "whoisip");
        register("enchant", CommandEnchant.class, "enchantment");
        register("nightvision", CommandNightVision.class, "nv");
        register("sudo", CommandSudo.class, "su");

        register("kit", CommandKit.class, "kits");
        register("showkit", CommandShowKit.class);
        register("kiteditor", CommandKitEditor.class, "keditor");
        register("kitcreate", CommandKitCreate.class, "kcreate");
        register("kitdelete", CommandKitDelete.class, "delete");

        register("cooldown", CommandCooldown.class);
        register("itemname", CommandItemName.class, "iname", "itemrename", "irename");
        register("itemlore", CommandItemLore.class, "ilore", "itemlore", "lore");
        register("mail", CommandMail.class, "mailbox", "mb");
        register("give", CommandGive.class);
        register("giveall", CommandGiveAll.class);
        register("powertools", CommandPowerTools.class, "pt");
        register("powertools-toggle", CommandPowerToolsToggle.class, "pt-toggle");

        register("experience", CommandExperience.class, "xp", "exp", "level", "levels");

        register("hologram", CommandHologram.class, "holo", "ho");
        register("sb", CommandScoreboard.class);

        register("voteparty", CommandVoteParty.class, "vp");
        register("vote", CommandVote.class);
        register("vault", CommandVault.class, "sac", "bag", "b", "coffre", "chest");
        register("player-worldedit", CommandWorldEdit.class, "pwe", "ess-worldedit", "eworldedit", "ew");

        register("suicide", CommandSuicide.class);

        for (RegisterCommand registerCommand : this.commands) {
            try {
                commandManager.registerCommand(this.plugin, registerCommand.command, registerCommand.commandClass.getConstructor(EssentialsPlugin.class).newInstance(this.plugin), registerCommand.aliases);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        commandManager.saveCommands();
    }

    private void register(String command, Class<? extends VCommand> commandClass, String... aliases) {
        this.commands.add(new RegisterCommand(command, commandClass, Arrays.asList(aliases)));
    }

    public record RegisterCommand(String command, Class<? extends VCommand> commandClass, List<String> aliases) {

    }
}