package fr.maxlego08.essentials.commands.commands.essentials;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CommandEssentialsDeleteWorld extends VCommand {

    public CommandEssentialsDeleteWorld(EssentialsPlugin plugin) {
        super(plugin);
        this.addSubCommand("delete-world");
        this.setPermission(Permission.ESSENTIALS_DELETE_WORLD);
        this.setDescription(Message.DESCRIPTION_DELETE_WORLD);
        this.addOptionalArg("world", (a, b) -> Bukkit.getWorlds().stream().map(World::getName).toList());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String worldName = this.argAsString(0);
        if (sender instanceof Player) {
            message(sender, "<c:#FF5555>Only the console can run this command!");
            return CommandResultType.DEFAULT;
        }

        if (Bukkit.getOnlinePlayers().size() != 0) {
            message(sender, "<c:#FF5555>There must be no players connected to execute this command!");
            return CommandResultType.DEFAULT;
        }

        message(sender, "<c:#55FF55>Start of deletion of <c:#FFFFFF>" + worldName + "<c:#FF5555> world related data.");

        var storage = plugin.getStorageManager().getStorage();
        storage.deleteWorldData(worldName);

        message(sender, "<c:#FFFFFF>" + worldName + "<c:#55FF55> world data has been deleted!");

        return CommandResultType.SUCCESS;
    }
}
