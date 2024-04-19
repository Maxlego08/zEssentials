package fr.maxlego08.essentials.buttons.sanction;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.sanction.SanctionType;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.SanctionModule;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.time.Duration;

public class ButtonSanction extends ZButton {

    private final EssentialsPlugin plugin;
    private final Duration duration;
    private final SanctionType type;
    private final String reason;

    public ButtonSanction(EssentialsPlugin plugin, Duration duration, SanctionType type, String reason) {
        this.plugin = plugin;
        this.duration = duration;
        this.type = type;
        this.reason = reason;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot, Placeholders placeholders) {
        super.onClick(player, event, inventory, slot, placeholders);

        User user = this.plugin.getUser(player.getUniqueId());
        if (user == null) return;

        User targetuser = user.getTargetUser();
        if (targetuser == null) return;

        SanctionModule sanctionModule = this.plugin.getModuleManager().getModule(SanctionModule.class);
        player.closeInventory();
        switch (type) {

            case KICK -> sanctionModule.kick(player, targetuser.getUniqueId(), targetuser.getName(), reason);
            case MUTE -> sanctionModule.mute(player, targetuser.getUniqueId(), targetuser.getName(), duration, reason);
            case BAN -> sanctionModule.ban(player, targetuser.getUniqueId(), targetuser.getName(), duration, reason);
            case UNBAN -> sanctionModule.unban(player, targetuser.getUniqueId(), targetuser.getName(), reason);
            case UNMUTE -> sanctionModule.unmute(player, targetuser.getUniqueId(), targetuser.getName(), reason);
            case WARN -> {
                player.sendMessage("§cTODO");
            }
        }
    }
}
