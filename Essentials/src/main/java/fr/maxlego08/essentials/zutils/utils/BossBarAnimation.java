package fr.maxlego08.essentials.zutils.utils;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;

public class BossBarAnimation implements Runnable {

    private final Player player;
    private final BossBar bossBar;
    private final WrappedTask wrappedTask;
    private final long duration;
    private long remainingTicks;

    public BossBarAnimation(EssentialsPlugin plugin, Player player, BossBar bossBar, long duration) {
        this.player = player;
        this.bossBar = bossBar;
        this.remainingTicks = duration;
        this.duration = duration;
        this.wrappedTask = plugin.getScheduler().runTimer(this, 0L, 1L);
    }

    @Override
    public void run() {
        double progress = (double) this.remainingTicks / this.duration;
        this.bossBar.progress((float) progress);

        if (remainingTicks <= 0) {
            this.player.hideBossBar(this.bossBar);
            this.wrappedTask.cancel();
        }

        this.remainingTicks -= 1;
    }
}
