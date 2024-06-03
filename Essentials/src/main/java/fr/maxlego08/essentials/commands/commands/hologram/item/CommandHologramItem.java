package fr.maxlego08.essentials.commands.commands.hologram.item;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.hologram.configuration.ItemHologramConfiguration;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;
import org.bukkit.inventory.ItemStack;

public class CommandHologramItem extends VCommandHologram {

    public CommandHologramItem(EssentialsPlugin plugin) {
        super(plugin, HologramType.ITEM);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_ADD_LINE);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_ADD_LINE);
        this.addSubCommand("item");
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) {
            message(sender, Message.HOLOGRAM_ITEM_ERROR, "%name%", hologram.getName());
            return;
        }

        ((ItemHologramConfiguration) hologram.getConfiguration()).setItemStack(itemStack);

        hologram.update();
        hologram.updateForAllPlayers();
        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_ITEM, "%name%", hologram.getName());
    }
}
