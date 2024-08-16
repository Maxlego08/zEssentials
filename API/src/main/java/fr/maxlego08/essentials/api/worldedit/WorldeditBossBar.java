package fr.maxlego08.essentials.api.worldedit;

import org.bukkit.entity.Player;

public interface WorldeditBossBar {

    void create(Player player, WorldeditBossBarConfiguration worldeditBossBarConfiguration, long duration);

    void update(Player player, float percent, long duration);

    void remove(Player player);

}
