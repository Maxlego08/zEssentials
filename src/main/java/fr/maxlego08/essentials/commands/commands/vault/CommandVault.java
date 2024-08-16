package fr.maxlego08.essentials.commands.commands.vault;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.vault.VaultModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandVault extends VCommand {

    public CommandVault(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_VAULT_USE);
        this.setModule(VaultModule.class);
        this.onlyPlayers();
        this.addOptionalArg("vault id", (sender, args) -> sender instanceof Player player ? plugin.getVaultManager().getVaultAsTabCompletion(player) : new ArrayList<>());
        this.addSubCommand(new CommandVaultSetSlot(plugin));
        this.addSubCommand(new CommandVaultAddSlot(plugin));
        this.addSubCommand(new CommandVaultGive(plugin));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        int vaultId = this.argAsInteger(0, 1);
        plugin.getVaultManager().openVault(this.player, vaultId);

        return CommandResultType.SUCCESS;
    }

}
