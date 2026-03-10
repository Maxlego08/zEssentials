package fr.maxlego08.essentials.hooks;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.modules.death.MythicMobsHook;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.entity.Entity;

import java.util.Optional;

public class MythicMobsHookImpl implements MythicMobsHook {

    private final EssentialsPlugin plugin;

    public MythicMobsHookImpl(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isMythicMob(Entity entity) {
        return MythicBukkit.inst().getMobManager().isMythicMob(entity);
    }

    @Override
    public Optional<String> getMythicMobName(Entity entity) {
        if (!isMythicMob(entity)) {
            return Optional.empty();
        }

        ActiveMob activeMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity);
        if (activeMob == null) {
            return Optional.empty();
        }

        String displayName = activeMob.getDisplayName();
        if (displayName != null && !displayName.isEmpty()) {
            return Optional.of(displayName);
        }

        return Optional.of(activeMob.getMobType());
    }
}
