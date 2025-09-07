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

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandLagWorld extends VCommand {

    public CommandLagWorld(EssentialsPlugin plugin) {
        super(plugin);
        this.addSubCommand("world");
        this.setPermission(Permission.ESSENTIALS_LAG);
        this.setDescription(Message.DESCRIPTION_LAG_WORLD);
        this.addRequireArg("world", (a, b) -> Bukkit.getWorlds().stream().map(World::getName).toList());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        World world = this.argAsWorld(0);
        if (world == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        message(this.sender, Message.COMMAND_LAG_WORLD_HEADER, "%world%", world.getName());
        Map<EntityType, Long> counts = world.getEntities().stream()
                .filter(entity -> !(entity instanceof Player))
                .collect(Collectors.groupingBy(Entity::getType, Collectors.counting()));

        counts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> message(this.sender, Message.COMMAND_LAG_WORLD_ENTRY,
                        "%type%", entry.getKey().name().toLowerCase(),
                        "%amount%", entry.getValue()));

        return CommandResultType.SUCCESS;
    }
}
