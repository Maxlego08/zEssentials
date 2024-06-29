package fr.maxlego08.essentials.api.chat;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public record ShowItem(Player player, ItemStack itemStack, long expiredAt, String code) {

    public boolean isExpired() {
        return System.currentTimeMillis() >= expiredAt;
    }

}
