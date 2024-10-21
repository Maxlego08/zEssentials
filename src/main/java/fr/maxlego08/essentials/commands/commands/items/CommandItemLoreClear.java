package fr.maxlego08.essentials.commands.commands.items;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.ItemModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;

public class CommandItemLoreClear extends VCommand {

    private final NamespacedKey loreLineRaw;

    public CommandItemLoreClear(EssentialsPlugin plugin) {
        super(plugin);
        this.loreLineRaw = new NamespacedKey(plugin, "lore-line-raw");
        this.setModule(ItemModule.class);
        this.setPermission(Permission.ESSENTIALS_ITEM_LORE_CLEAR);
        this.setDescription(Message.DESCRIPTION_ITEM_LORE_CLEAR);
        this.addSubCommand("clear");
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        ItemStack itemStack = this.player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) {
            message(sender, Message.COMMAND_ITEM_EMPTY);
            return CommandResultType.DEFAULT;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.lore(new ArrayList<>());

        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.remove(this.loreLineRaw);

        itemStack.setItemMeta(itemMeta);

        message(sender, Message.COMMAND_ITEM_LORE_CLEAR);

        return CommandResultType.SUCCESS;
    }
}
