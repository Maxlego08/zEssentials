package fr.maxlego08.essentials.commands.commands.warp;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.utils.Warp;
import fr.maxlego08.essentials.module.modules.WarpModule;
import fr.maxlego08.essentials.storage.ConfigStorage;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.Arrays;
import java.util.Optional;

public class CommandSetWarp extends VCommand {

    public CommandSetWarp(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(WarpModule.class);
        this.setPermission(Permission.ESSENTIALS_WARP_SET);
        this.setDescription(Message.DESCRIPTION_WARP_SET);
        this.addRequireArg("name", (a, b) -> Arrays.asList("spawn", "village", "castle", "forest", "mountain", "mine", "desert", "ocean", "cave", "nether", "end", "crates", "shop", "auction", "duels", "worlds", "afk", "enchants", "anvils"));
        this.addBooleanOptionalArg("overwrite");
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String warpName = this.argAsString(0);
        boolean overwrite = this.argAsBoolean(1, false);

        Optional<Warp> optional = plugin.getWarp(warpName);
        if (optional.isPresent() && !overwrite) {
            message(sender, Message.COMMAND_WARP_ALREADY_EXIST, "%name%", warpName);
            return CommandResultType.DEFAULT;
        }

        ConfigStorage.warps.removeIf(warp -> warp.getName().equalsIgnoreCase(warpName));
        ConfigStorage.warps.add(new Warp(warpName, this.player.getLocation()));
        ConfigStorage.getInstance().save(plugin.getPersist());

        message(sender, Message.COMMAND_WARP_CREATE, "%name%", warpName);

        return CommandResultType.SUCCESS;
    }
}
