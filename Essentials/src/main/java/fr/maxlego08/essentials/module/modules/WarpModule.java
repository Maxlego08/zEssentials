package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.module.ZModule;

public class WarpModule extends ZModule {

    private boolean enableInventory;
    private boolean enableNoArgumentMessage;

    public WarpModule(ZEssentialsPlugin plugin) {
        super(plugin, "warp");
    }

    public boolean isEnableInventory() {
        return enableInventory;
    }

    public boolean isEnableNoArgumentMessage() {
        return enableNoArgumentMessage;
    }
}
