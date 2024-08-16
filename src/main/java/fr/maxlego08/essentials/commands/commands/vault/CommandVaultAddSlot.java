package fr.maxlego08.essentials.commands.commands.vault;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.vault.VaultModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.OfflinePlayer;

import java.util.stream.IntStream;

public class CommandVaultAddSlot extends VCommand {

    public CommandVaultAddSlot(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(VaultModule.class);
        this.setPermission(Permission.ESSENTIALS_VAULT_ADD_SLOT);
        this.setDescription(Message.DESCRIPTION_VAULT_ADD_SLOT);
        this.addSubCommand("add");
        this.addRequireOfflinePlayerNameArg();
        this.addRequireArg("slot", (a, b) -> IntStream.range(1, 100).mapToObj(String::valueOf).toList());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0);
        int slot = this.argAsInteger(1);
        plugin.getVaultManager().addPlayerSlot(this.sender, offlinePlayer, slot);

        return CommandResultType.SUCCESS;
    }

}
