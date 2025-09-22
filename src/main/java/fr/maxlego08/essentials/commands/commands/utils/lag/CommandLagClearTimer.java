package fr.maxlego08.essentials.commands.commands.utils.lag;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CommandLagClearTimer extends VCommand {

    public CommandLagClearTimer(EssentialsPlugin plugin) {
        super(plugin);
        this.addSubCommand("clearcooldown");
        this.setPermission(Permission.ESSENTIALS_LAG);
        this.setDescription(Message.DESCRIPTION_LAG_CLEAR_COUNTDOWN);
        this.addRequireArg("time");
        this.addOptionalArg("world/entity", (a, b) -> {
            java.util.List<String> list = new java.util.ArrayList<>();
            list.addAll(Bukkit.getWorlds().stream().map(World::getName).toList());
            for (EntityType type : EntityType.values()) {
                list.add(type.name().toLowerCase());
            }
            return list;
        });
        this.addOptionalArg("entity", (a, b) -> java.util.Arrays.stream(EntityType.values()).map(e -> e.name().toLowerCase()).toList());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        int seconds = this.argAsInteger(0, 0);
        if (seconds <= 0) {
            return CommandResultType.SYNTAX_ERROR;
        }
        World world = this.argAsWorld(1);
        EntityType type = world != null ? argAsEntityType(2) : argAsEntityType(1);

        AtomicInteger time = new AtomicInteger(seconds);
        plugin.getScheduler().runTimer(task -> {
            if (!plugin.isEnabled()) {
                task.cancel();
                return;
            }

            int remaining = time.getAndDecrement();
            if (remaining <= 0) {
                int removed = 0;
                if (world != null) {
                    removed = clear(world, type);
                } else {
                    for (World w : Bukkit.getWorlds()) {
                        removed += clear(w, type);
                    }
                }
                broadcast(Message.COMMAND_LAG_CLEAR_SUCCESS,
                        "%amount%", removed,
                        "%world%", world != null ? world.getName() : "all worlds");
                task.cancel();
                return;
            }

            broadcast(Message.COMMAND_LAG_CLEAR_COUNTDOWN, "%time%", remaining);
        }, 0, 1, TimeUnit.SECONDS);

        return CommandResultType.SUCCESS;
    }

    private int clear(World world, EntityType type) {
        int count = 0;
        for (Entity entity : world.getEntities()) {
            if (entity instanceof Player) continue;
            if (type != null && entity.getType() != type) continue;
            entity.remove();
            count++;
        }
        return count;
    }
}
