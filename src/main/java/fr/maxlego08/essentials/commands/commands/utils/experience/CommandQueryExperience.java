package fr.maxlego08.essentials.commands.commands.utils.experience;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandQueryExperience extends VCommand {

    public CommandQueryExperience(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_EXPERIENCE_QUERY);
        this.setDescription(Message.DESCRIPTION_EXPERIENCE_QUERY);
        this.addSubCommand("query");
        this.setSyntax("/experience query <player> <type>");
        this.addRequirePlayerNameArg();
        this.addRequireArg("type", (sender, objects) -> Arrays.asList("levels", "points"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        Player player = this.argAsPlayer(0);
        String type = this.argAsString(1);

        int amount = type.equalsIgnoreCase("levels") ? player.getLevel() : this.getPlayerExp(player);

        message(sender, Message.EXPERIENCE_QUERIED, player, "%amount%",amount, "%type%", type);
        return CommandResultType.SUCCESS;
    }

    private int getExpToLevelUp(int level){
        if(level <= 15){
            return 2*level+7;
        } else if(level <= 30){
            return 5*level-38;
        } else {
            return 9*level-158;
        }
    }

    private int getExpAtLevel(int level){
        if(level <= 16){
            return (int) (Math.pow(level,2) + 6*level);
        } else if(level <= 31){
            return (int) (2.5*Math.pow(level,2) - 40.5*level + 360.0);
        } else {
            return (int) (4.5*Math.pow(level,2) - 162.5*level + 2220.0);
        }
    }

    private int getPlayerExp(Player player){
        int exp = 0;
        int level = player.getLevel();

        // Get the amount of XP in past levels
        exp += getExpAtLevel(level);

        // Get amount of XP towards next level
        exp += Math.round(getExpToLevelUp(level) * player.getExp());

        return exp;
    }
}
