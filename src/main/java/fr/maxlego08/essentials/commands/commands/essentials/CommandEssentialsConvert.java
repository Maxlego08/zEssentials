package fr.maxlego08.essentials.commands.commands.essentials;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.convert.Convert;
import fr.maxlego08.essentials.convert.axvault.AxVaultsConvert;
import fr.maxlego08.essentials.convert.cmi.CMIConvert;
import fr.maxlego08.essentials.convert.coinsengine.CoinsEngineConvert;
import fr.maxlego08.essentials.convert.essentialsx.EssentialsXConvert;
import fr.maxlego08.essentials.convert.huskhomes.HuskHomesConvert;
import fr.maxlego08.essentials.convert.playervaultx.PlayerVaultXConvert;
import fr.maxlego08.essentials.convert.sunlight.SunlightConvert;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.Arrays;

public class CommandEssentialsConvert extends VCommand {

    public CommandEssentialsConvert(EssentialsPlugin plugin) {
        super(plugin);
        this.addSubCommand("convert", "conv", "c");
        this.setPermission(Permission.ESSENTIALS_CONVERT);
        this.addOptionalArg("plugin", (a, b) -> Arrays.stream(Plugins.values()).map(Enum::name).map(String::toLowerCase).toList());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String pluginName = this.argAsString(0);
        Plugins plugins = Plugins.valueOf(pluginName.toUpperCase());
        var converClass = plugins.getConvertClass();
        try {
            var constructor = converClass.getConstructor(EssentialsPlugin.class);
            Convert convert = constructor.newInstance(plugin);
            convert.convert(sender);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CommandResultType.SYNTAX_ERROR;
        }

        return CommandResultType.SUCCESS;
    }

    public enum Plugins {

        ESSENTIALSX(EssentialsXConvert.class),
        CMI(CMIConvert.class),
        SUNLIGHT(SunlightConvert.class),
        PLAYERVAULTX(PlayerVaultXConvert.class),
        COINSENGINE(CoinsEngineConvert.class),
        HUSKHOMES(HuskHomesConvert.class),
        AXVAULTS(AxVaultsConvert.class),

        ;

        private final Class<? extends Convert> convertClass;

        Plugins(Class<? extends Convert> convertClass) {
            this.convertClass = convertClass;
        }

        public Class<? extends Convert> getConvertClass() {
            return convertClass;
        }
    }

}
