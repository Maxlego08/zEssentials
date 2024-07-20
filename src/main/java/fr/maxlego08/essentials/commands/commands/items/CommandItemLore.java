package fr.maxlego08.essentials.commands.commands.items;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.ItemModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandItemLore extends VCommand {
    public CommandItemLore(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(ItemModule.class);
        this.setPermission(Permission.ESSENTIALS_ITEM_LORE);
        this.setDescription(Message.DESCRIPTION_ITEM_LORE);
        this.addSubCommand(new CommandItemLoreAdd(plugin));
        this.addSubCommand(new CommandItemLoreSet(plugin));
        this.addSubCommand(new CommandItemLoreClear(plugin));
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        syntaxMessage();
        return CommandResultType.SUCCESS;
    }
}
