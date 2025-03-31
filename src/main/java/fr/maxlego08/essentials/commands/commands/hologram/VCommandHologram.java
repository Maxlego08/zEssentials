package fr.maxlego08.essentials.commands.commands.hologram;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramLine;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.hologram.HologramModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class VCommandHologram extends VCommand {

    private final HologramType hologramType;

    public VCommandHologram(EssentialsPlugin plugin, HologramType hologramType) {
        super(plugin);
        this.hologramType = hologramType;
        this.setModule(HologramModule.class);
        this.addRequireArg("name", plugin.getHologramManager().getHologramCompletion(hologramType));
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String name = this.argAsString(0);
        HologramManager manager = plugin.getHologramManager();

        Optional<Hologram> optional = manager.getHologram(name);
        if (optional.isEmpty()) {
            message(player, Message.HOLOGRAM_DOESNT_EXIST, "%name%", name);
            return CommandResultType.DEFAULT;
        }

        Hologram hologram = optional.get();
        if (this.hologramType != null && hologram.getHologramType() != this.hologramType) {
            message(player, this.hologramType == HologramType.TEXT ? Message.HOLOGRAM_IS_NOT_A_TEXT : this.hologramType == HologramType.BLOCK ? Message.HOLOGRAM_IS_NOT_A_BLOCK : Message.HOLOGRAM_IS_NOT_A_ITEM, "%name%", name);
            return CommandResultType.DEFAULT;
        }

        this.perform(plugin, hologram, manager);

        return CommandResultType.SUCCESS;
    }

    protected abstract void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager);

    protected void addRequireArgHologram(String message, TabCompletionHologram runnable) {
        HologramManager manager = this.plugin.getHologramManager();
        this.addRequireArg(message, (sender, args) -> {
            if (args.length >= 2) {
                String hologramName = args[1];
                Optional<Hologram> optional = manager.getHologram(hologramName);
                if (optional.isPresent()) {
                    return runnable.accept(sender, optional.get());
                }
            }
            return new ArrayList<>();
        });
    }

    protected List<String> lineToList(Hologram hologram) {
        return hologram.getHologramLines().stream().map(HologramLine::getLine).map(String::valueOf).toList();
    }

}
