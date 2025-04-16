package fr.maxlego08.essentials.commands.commands.cooldown;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandCooldown extends VCommand {
    public CommandCooldown(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_COOLDOWN);
        this.setDescription(Message.DESCRIPTION_COOLDOWN);
        this.addSubCommand(new CommandCooldownDelete(plugin));
        this.addSubCommand(new CommandCooldownCreate(plugin));
        this.addSubCommand(new CommandCooldownShow(plugin));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        syntaxMessage();
        return CommandResultType.SUCCESS;
    }
}
