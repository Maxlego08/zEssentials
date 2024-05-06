package fr.maxlego08.essentials.commands.commands.utils.admins;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.utils.mobs.KillAllType;
import fr.maxlego08.essentials.api.utils.mobs.Mob;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommandKillAll extends VCommand {
    public CommandKillAll(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_KILL_ALL);
        this.setDescription(Message.DESCRIPTION_KILL_ALL);
        this.addRequireArg("type", (a, b) -> Arrays.stream(KillAllType.values()).map(type -> type.name().toLowerCase()).toList());
        this.addOptionalArg("world", (a, b) -> Bukkit.getWorlds().stream().map(world -> world.getName().toLowerCase()).toList());
        this.addOptionalArg("radius", (a, b) -> Arrays.asList("10", "20", "30", "40", "50", "60", "70", "80", "90"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String typeAsString = this.argAsString(0);
        World world = this.argAsWorld(1, this.sender instanceof Player ? this.player.getWorld() : null);
        if (world == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        int radius = this.argAsInteger(2, 0);

        List<KillAllType> removeTypes = typeAsString.contains(",") ? fromString(typeAsString) : List.of(KillAllType.valueOf(typeAsString.toUpperCase()));
        List<EntityType> mobList = removeTypes.isEmpty() ? new ArrayList<>() : removeTypes.get(0) == KillAllType.CUSTOM ? fromTypeString(typeAsString) : new ArrayList<>();
        return remove(removeTypes, radius, world, mobList.stream().map(Mob::fromBukkitType).toList());
    }

    private CommandResultType remove(List<KillAllType> removeTypes, int radius, World world, List<Mob> customRemoveTypes) {

        if (removeTypes.isEmpty()) return CommandResultType.SYNTAX_ERROR;

        int removed = 0;
        for (Chunk chunk : world.getLoadedChunks()) {
            for (Entity entity : chunk.getEntities()) {
                if (entity instanceof HumanEntity) continue;

                for (KillAllType type : removeTypes) {

                    // We should skip any animals tamed by players unless we are specifically targetting them.
                    if (entity instanceof Tameable tameable && tameable.isTamed() && (tameable.getOwner() instanceof Player || tameable.getOwner() instanceof OfflinePlayer) && !removeTypes.contains(KillAllType.TAMED)) {
                        continue;
                    }

                    // We should skip any NAMED animals unless we are specifically targetting them.
                    if (entity instanceof LivingEntity && entity.customName() != null && !removeTypes.contains(KillAllType.NAMED)) {
                        continue;
                    }

                    switch (type) {
                        case TAMED -> {
                            if (entity instanceof Tameable tameable && tameable.isTamed()) {
                                entity.remove();
                                removed++;
                            }
                        }
                        case NAMED -> {
                            if (entity instanceof LivingEntity && entity.customName() != null) {
                                entity.remove();
                                removed++;
                            }
                        }
                        case CUSTOM -> {
                            for (Mob mob : customRemoveTypes) {
                                if (entity.getType() == mob.getType()) {
                                    entity.remove();
                                    removed++;
                                }
                            }
                        }
                        default -> {
                            if (type.checkType(entity)) {
                                entity.remove();
                                removed++;
                            }
                        }
                    }
                }
            }
        }

        message(sender, Message.COMMAND_REMOVE, "%amount%", removed);

        return CommandResultType.SUCCESS;
    }


    private List<EntityType> fromTypeString(String typeAsString) {
        return typeAsString.contains(",") ? Arrays.stream(typeAsString.split(",")).map(entityType -> {
            try {
                return EntityType.valueOf(entityType.toUpperCase());
            } catch (Exception ignored) {
                return null;
            }
        }).filter(Objects::nonNull).toList() : List.of(EntityType.valueOf(typeAsString));
    }

    private List<KillAllType> fromString(String typeAsString) {
        return Arrays.stream(typeAsString.split(",")).map(entityType -> {
            try {
                return KillAllType.valueOf(entityType.toUpperCase());
            } catch (Exception ignored) {
                try {
                    return KillAllType.valueOf(entityType.concat("S").toUpperCase());
                } catch (final Exception ignored2) {
                    return KillAllType.CUSTOM;
                }
            }
        }).toList();
    }
}
