package fr.maxlego08.essentials.commands.commands.essentials;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.convert.cmi.CMIConvert;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.Arrays;

public class CommandEssentialsConvert extends VCommand {

    public CommandEssentialsConvert(EssentialsPlugin plugin) {
        super(plugin);
        this.addSubCommand("convert", "conv", "c");
        this.setPermission(Permission.ESSENTIALS_CONVERT);
        this.addOptionalArg("plugin", (a, b) -> Arrays.asList("CMI", "EssentialsX", "Sunlight"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String pluginName = this.argAsString(0);
        if (pluginName.equalsIgnoreCase("CMI")) {

            CMIConvert cmiConvert = new CMIConvert(plugin);
            cmiConvert.convert(sender);

            return CommandResultType.SUCCESS;
        }

        return CommandResultType.SYNTAX_ERROR;
    }
}
