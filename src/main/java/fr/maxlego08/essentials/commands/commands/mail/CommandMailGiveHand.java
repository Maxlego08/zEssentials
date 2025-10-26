package fr.maxlego08.essentials.commands.commands.mail;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.MailBoxModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.inventory.ItemStack;

public class CommandMailGiveHand extends VCommand {

    public CommandMailGiveHand(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(MailBoxModule.class);
        this.setDescription(Message.DESCRIPTION_MAIL_GIVE_HAND);
        this.setPermission(Permission.ESSENTIALS_MAIL_GIVE_HAND);
        this.addSubCommand("give-hand");
        this.addRequireOfflinePlayerNameArg();
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        ItemStack itemStack = this.player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType().isAir()) {
            message(this.sender, Message.COMMAND_ITEM_EMPTY);
            return CommandResultType.DEFAULT;
        }

        String username = this.argAsString(0);
        ItemStack itemToGive = itemStack.clone();

        var module = plugin.getModuleManager().getModule(MailBoxModule.class);
        this.fetchUniqueId(username, uuid -> module.giveItemFromHand(this.sender, uuid, username, itemToGive));

        return CommandResultType.SUCCESS;
    }
}
