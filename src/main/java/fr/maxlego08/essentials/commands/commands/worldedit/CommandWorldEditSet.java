package fr.maxlego08.essentials.commands.commands.worldedit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.WorldeditModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Material;

public class CommandWorldEditSet extends VCommand {

    public CommandWorldEditSet(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(WorldeditModule.class);
        this.setPermission(Permission.ESSENTIALS_WORLDEDIT_SET);
        this.setDescription(Message.DESCRIPTION_WORLDEDIT_SET);
        this.addSubCommand("set");
        this.addRequireArg("material", (a, b) -> plugin.getWorldeditManager().getAllowedMaterials());
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Material material = Material.valueOf(this.argAsString(0));
        plugin.getWorldeditManager().setBlocks(this.user, material);

        return CommandResultType.SUCCESS;
    }
}
