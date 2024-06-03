package fr.maxlego08.essentials.commands.commands.utils.admins;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
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

        plugin.getScheduler().runAsync(wrappedTask -> {

            OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0);
            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);

            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            meta.setOwningPlayer(player);
            itemStack.setItemMeta(meta);

            this.plugin.give(this.player, itemStack);
            message(this.sender, Message.COMMAND_SKULL, "%name%", offlinePlayer.getName());
        });

        return CommandResultType.SUCCESS;
    }
}
