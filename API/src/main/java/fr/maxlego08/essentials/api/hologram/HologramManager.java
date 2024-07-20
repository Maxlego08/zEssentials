package fr.maxlego08.essentials.api.hologram;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.TabCompletion;
import fr.maxlego08.essentials.api.hologram.configuration.HologramConfiguration;
import fr.maxlego08.essentials.api.modules.Module;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

public interface HologramManager extends Module {

    Optional<Hologram> getHologram(String name);

    Collection<Hologram> getHolograms();

    void addHologram(Hologram hologram);

    void removeHologram(Hologram hologram);

    Hologram create(Player player, HologramType hologramType, String name);

    void delete(Player player, String name);

    void loadHolograms();

    void saveHolograms();

    void saveHologram(Hologram hologram);

    void loadHologram(File file);

    TabCompletion getHologramCompletion();

    TabCompletion getHologramCompletion(HologramType hologramType);

    Hologram createHologram(HologramType hologramType, HologramConfiguration configuration, String fileName, String name, Location location);
}
