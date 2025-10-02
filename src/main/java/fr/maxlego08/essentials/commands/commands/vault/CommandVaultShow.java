package fr.maxlego08.essentials.commands.commands.vault;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.vault.VaultModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.OfflinePlayer;

public class CommandVaultShow extends VCommand {

    public CommandVaultShow(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(VaultModule.class);
        this.setPermission(Permission.ESSENTIALS_VAULT_SHOW);
        this.setDescription(Message.DESCRIPTION_VAULT_SHOW);
        this.onlyPlayers();
        this.addSubCommand("show");
        this.addRequirePlayerNameArg();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        OfflinePlayer target = this.argAsOfflinePlayer(0);
        plugin.getVaultManager().openVault(this.player, target, 1);
        return CommandResultType.SUCCESS;
    }
}
