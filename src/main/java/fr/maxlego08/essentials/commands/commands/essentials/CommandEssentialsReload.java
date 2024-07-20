package fr.maxlego08.essentials.commands.commands.essentials;

import fr.maxlego08.essentials.api.ConfigurationFile;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.modules.Module;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.Optional;

public class CommandEssentialsReload extends VCommand {

    public CommandEssentialsReload(EssentialsPlugin plugin) {
        super(plugin);
        this.addSubCommand("reload", "rl");
        this.setPermission(Permission.ESSENTIALS_RELOAD);
        this.setDescription(Message.DESCRIPTION_RELOAD);
        this.addOptionalArg("module", (a, b) -> plugin.getModuleManager().getModules().stream().map(Module::getName).toList());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String moduleName = this.argAsString(0, null);
        if (moduleName == null) {
            plugin.getInventoryManager().deleteInventories(plugin);
            plugin.getConfigurationFiles().forEach(ConfigurationFile::load);
            plugin.getModuleManager().loadConfigurations();
            message(sender, Message.COMMAND_RELOAD);
        } else {

            Optional<Module> optional = plugin.getModuleManager().getModules().stream().filter(module -> module.getName().equalsIgnoreCase(moduleName)).findFirst();
            if (optional.isEmpty()) {
                message(sender, Message.COMMAND_RELOAD_ERROR, "%module%", moduleName);
                return CommandResultType.DEFAULT;
            }

            Module module = optional.get();
            module.loadConfiguration();

            message(sender, Message.COMMAND_RELOAD_MODULE, "%module%", moduleName);
        }

        return CommandResultType.SUCCESS;
    }
}
