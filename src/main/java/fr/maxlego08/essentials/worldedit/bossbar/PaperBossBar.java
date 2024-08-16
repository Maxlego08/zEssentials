package fr.maxlego08.essentials.worldedit.bossbar;

import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.utils.component.AdventureComponent;
import fr.maxlego08.essentials.api.worldedit.WorldeditBossBar;
import fr.maxlego08.essentials.api.worldedit.WorldeditBossBarConfiguration;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PaperBossBar extends ZUtils implements WorldeditBossBar {


    private final AdventureComponent adventureComponent;
    private final Map<UUID, BossBar> bars = new HashMap<>();

    public PaperBossBar(AdventureComponent adventureComponent) {
        this.adventureComponent = adventureComponent;
    }

    @Override
    public void create(Player player, WorldeditBossBarConfiguration configuration, long duration) {
        BossBar bossBar = this.adventureComponent.createBossBar(
                getMessage(Message.WORLDEDIT_BOSSBAR, "%time%", TimerBuilder.getStringTime(duration)),
                BossBar.Color.valueOf(configuration.color().toUpperCase()),
                BossBar.Overlay.valueOf(configuration.style().toUpperCase())
        );
        bossBar.addViewer(player);
        bars.put(player.getUniqueId(), bossBar);
    }

    @Override
    public void update(Player player, float percent, long duration) {
        var bar = bars.get(player.getUniqueId());
        if (bar != null) {
            bar.progress(percent);
            bar.name(this.adventureComponent.getComponent(getMessage(Message.WORLDEDIT_BOSSBAR, "%time%", TimerBuilder.getStringTime(duration))));
        }
    }

    @Override
    public void remove(Player player) {
        var bar = bars.remove(player.getUniqueId());
        if (bar != null) player.hideBossBar(bar);
    }
}
