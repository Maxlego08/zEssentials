package fr.maxlego08.essentials.commands.commands.utils.experience;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandSetExperience extends VCommand {

    public CommandSetExperience(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_EXPERIENCE_SET);
        this.setDescription(Message.DESCRIPTION_EXPERIENCE_SET);
        this.addSubCommand("set");
        this.setSyntax("/experience set <player> <amount> <type>");
        this.addRequirePlayerNameArg();
        this.addRequireArg("amount", (sender, objects) -> Arrays.asList("1", "10", "30" ,"100", "1000"));
        this.addRequireArg("type", (sender, objects) -> Arrays.asList("levels", "experience"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        Player player = this.argAsPlayer(0);
        int amount = this.argAsInteger(1);
        String type = this.argAsString(2);

        if (type.equalsIgnoreCase("levels") || type.equalsIgnoreCase("level") || type.equalsIgnoreCase("l")) {
            player.setLevel(amount);
        } else {
            player.setExperienceLevelAndProgress(amount);
        }
        message(sender, Message.EXPERIENCE_SETTED, player, "%amount%",amount, "%type%", type);
        return CommandResultType.SUCCESS;
    }
}
