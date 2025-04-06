package fr.maxlego08.essentials.commands.commands.utils.experience;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandGrantRandomExperience extends VCommand {

    public CommandGrantRandomExperience(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_EXPERIENCE_GRANT);
        this.setDescription(Message.DESCRIPTION_EXPERIENCE_GRANT);
        this.addSubCommand("grant-random");
        this.setSyntax("/experience grant-random <player> <amount> <type>");
        this.addRequirePlayerNameArg();
        this.addRequireArg("min-amount", (sender, objects) -> Arrays.asList("1", "10", "30", "100", "1000"));
        this.addRequireArg("max-amount", (sender, objects) -> Arrays.asList("1", "10", "30", "100", "1000"));
        this.addRequireArg("type", (sender, objects) -> Arrays.asList("levels", "points"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        Player player = this.argAsPlayer(0);
        int minAmount = this.argAsInteger(1);
        int maxAmount = this.argAsInteger(2);
        String type = this.argAsString(3);
        int value = minAmount + (int) (Math.random() * (maxAmount - minAmount + 1));

        if (type.equalsIgnoreCase("levels")) {
            player.giveExpLevels(value);
        } else {
            player.giveExp(value);
        }
        message(sender, Message.EXPERIENCE_GRANTED, player, "%amount%", value, "%type%", type);
        return CommandResultType.SUCCESS;
    }
}
