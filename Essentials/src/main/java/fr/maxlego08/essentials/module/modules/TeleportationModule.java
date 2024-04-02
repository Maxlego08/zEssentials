package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.module.ZModule;

public class TeleportationModule extends ZModule {

    private boolean teleportSafety;
    private boolean teleportToCenter;
    private int teleportDelay;
    private boolean teleportDelayBypass;

    public TeleportationModule(ZEssentialsPlugin plugin) {
        super(plugin, "teleportation");
    }

    public boolean isTeleportSafety() {
        return teleportSafety;
    }

    public boolean isTeleportToCenter() {
        return teleportToCenter;
    }

    public int getTeleportDelay() {
        return teleportDelay;
    }

    public boolean isTeleportDelayBypass() {
        return teleportDelayBypass;
    }
}
