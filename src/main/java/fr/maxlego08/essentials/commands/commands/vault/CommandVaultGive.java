package fr.maxlego08.essentials.commands.commands.vault;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.ItemModule;
import fr.maxlego08.essentials.module.modules.vault.VaultModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandVaultGive extends VCommand {

    public CommandVaultGive(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(VaultModule.class);
        this.setPermission(Permission.ESSENTIALS_VAULT_GIVE);
        this.setDescription(Message.DESCRIPTION_VAULT_GIVE);
        this.addSubCommand("give");
        this.addRequirePlayerNameArg();
        this.addRequireArg("item", (sender, args) -> {
            List<String> materials = new ArrayList<>(plugin.getMaterials().stream()
                    .filter(material -> material.isItem() && !material.isAir())
                    .map(Material::getKey).map(NamespacedKey::getKey).toList());
            var itemModule = plugin.getModuleManager().getModule(ItemModule.class);
            if (itemModule != null) materials.addAll(itemModule.getItemsName());
            return materials;
        });
        this.addOptionalArg("amount", (sender, args) -> Arrays.asList("1", "64", "500", "1000", "10000", "100000"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0);
        String itemName = this.argAsString(1);
        long amount = this.argAsLong(2, 1);
        var module = plugin.getModuleManager().getModule(ItemModule.class);

        ItemStack itemStack = module.getItemStack(itemName, offlinePlayer);
        if (itemStack == null || itemStack.getType().isAir()) {
            message(sender, Message.COMMAND_GIVE_ERROR, "%item%", itemName);
            return CommandResultType.DEFAULT;
        }

        plugin.getVaultManager().addItem(offlinePlayer.getUniqueId(), itemStack, amount);
        message(sender, Message.COMMAND_GIVE_VAULT, "%item%", getItemName(itemStack), "%player%", offlinePlayer.getName(), "%amount%", amount);

        return CommandResultType.SUCCESS;
    }

}
