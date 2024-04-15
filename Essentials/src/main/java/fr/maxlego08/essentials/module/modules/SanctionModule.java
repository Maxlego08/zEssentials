package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.module.ZModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SanctionModule extends ZModule {

    private String kickDefaultReason;

    public SanctionModule(ZEssentialsPlugin plugin) {
        super(plugin, "sanction");
    }

    public String getKickDefaultReason() {
        return kickDefaultReason;
    }

    public void kickPlayer(CommandSender sender, Player player, String reason) {

    }

}
