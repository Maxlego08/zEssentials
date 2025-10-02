package fr.maxlego08.essentials.commands.commands.utils.lag;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

public class CommandLag extends VCommand {

    public CommandLag(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_LAG);
        this.setDescription(Message.DESCRIPTION_LAG);
        this.addSubCommand(new CommandLagWorld(plugin));
        this.addSubCommand(new CommandLagClear(plugin));
        this.addSubCommand(new CommandLagClearTimer(plugin));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        for (World world : Bukkit.getWorlds()) {
            int monster = 0;
            int peaceful = 0;
            int items = 0;
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Monster) {
                    monster++;
                } else if (entity instanceof Item) {
                    items++;
                } else if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                    peaceful++;
                }
            }
            message(this.sender, Message.COMMAND_LAG_WORLD_LINE,
                    "%world%", world.getName(),
                    "%peaceful%", peaceful,
                    "%monsters%", monster,
                    "%items%", items);
        }

        return CommandResultType.SUCCESS;
    }
}
