package fr.maxlego08.essentials.commands.commands.utils.admins;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import fr.maxlego08.menu.ZMenuItemStack;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class CommandSkull extends VCommand {
    public CommandSkull(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_SKULL);
        this.setDescription(Message.DESCRIPTION_SKULL);
        this.addRequireArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String value = this.argAsString(0);
        plugin.getScheduler().runAsync(wrappedTask -> {

            OfflinePlayer offlinePlayer = value.length() > 16 ? null : this.argAsOfflinePlayer(0, null);
            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);

            if (offlinePlayer == null) {

                ZMenuItemStack menuItemStack = new ZMenuItemStack(plugin.getInventoryManager(), "", "");
                menuItemStack.setUrl(value);
                itemStack = menuItemStack.build(player, false);

            } else {

                SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
                meta.setOwningPlayer(offlinePlayer);
                itemStack.setItemMeta(meta);
            }

            this.plugin.give(this.player, itemStack);
            message(this.sender, Message.COMMAND_SKULL, "%name%", value);
        });

        return CommandResultType.SUCCESS;
    }
}
