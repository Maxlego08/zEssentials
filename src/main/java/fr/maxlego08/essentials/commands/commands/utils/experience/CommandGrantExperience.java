package fr.maxlego08.essentials.commands.commands.utils.experience;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandGrantExperience extends VCommand {

    public CommandGrantExperience(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_EXPERIENCE);
        this.setDescription(Message.DESCRIPTION_EXPERIENCE);
        this.addSubCommand("grant");
        this.addRequirePlayerNameArg();
        this.addRequireArg("amount", (sender, objects) -> Arrays.asList("1", "10", "30" ,"100", "1000"));
        this.addRequireArg("type", (sender, objects) -> Arrays.asList("level", "points"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        Player player = this.argAsPlayer(0);
        int amount = this.argAsInteger(1);
        String type = this.argAsString(2);

        if (type.equalsIgnoreCase("level")) {
            player.giveExpLevels(amount);
        } else {
            player.giveExp(amount);
        }
        message(sender, Message.EXPERIENCE_GRANTED, player.getName(), amount, "%amount%", type, "%type%");
        return CommandResultType.SUCCESS;
    }
}
