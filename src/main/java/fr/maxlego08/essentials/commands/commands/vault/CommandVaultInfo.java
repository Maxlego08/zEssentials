package fr.maxlego08.essentials.commands.commands.vault;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.vault.PermissionSlotsVault;
import fr.maxlego08.essentials.api.vault.PlayerVaults;
import fr.maxlego08.essentials.module.modules.vault.VaultModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

public class CommandVaultInfo extends VCommand {

    public CommandVaultInfo(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(VaultModule.class);
        this.setPermission(Permission.ESSENTIALS_VAULT_INFO);
        this.setDescription(Message.DESCRIPTION_VAULT_INFO);
        this.addSubCommand("info");
        this.addRequirePlayerNameArg();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player target = this.argAsPlayer(0);
        if (target == null) {
            return CommandResultType.DEFAULT;
        }

        VaultModule module = plugin.getModuleManager().getModule(VaultModule.class);
        PlayerVaults playerVaults = module.getPlayerVaults(target);

        int pluginSlots = playerVaults.getSlots();
        int permissionSlots = module.getVaultPermissions().stream()
                .filter(permission -> target.hasPermission(permission.permission()))
                .mapToInt(PermissionSlotsVault::slots)
                .max()
                .orElse(0);

        int maxSlots = module.getMaxSlotsPlayer(target);
        int usedSlots = playerVaults.getVaults().values().stream().mapToInt(v -> v.getVaultItems().size()).sum();
        int availableSlots = Math.max(0, maxSlots - usedSlots);

        message(sender, Message.COMMAND_VAULT_INFO,
                "%player%", target.getName(),
                "%max%", maxSlots,
                "%permissionSlots%", permissionSlots,
                "%pluginSlots%", pluginSlots,
                "%used%", usedSlots,
                "%available%", availableSlots);

        return CommandResultType.SUCCESS;
    }
}
