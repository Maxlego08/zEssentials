package fr.maxlego08.essentials.commands.commands.utils.admins.items;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import fr.maxlego08.essentials.zutils.utils.paper.PaperComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandItemLore extends VCommand {
    public CommandItemLore(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_ITEM_LORE);
        this.setDescription(Message.DESCRIPTION_ITEM_LORE);
        this.addSubCommand(new CommandItemLoreAdd(plugin));
        this.addSubCommand(new CommandItemLoreSet(plugin));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        syntaxMessage();
        return CommandResultType.SUCCESS;
    }
}
