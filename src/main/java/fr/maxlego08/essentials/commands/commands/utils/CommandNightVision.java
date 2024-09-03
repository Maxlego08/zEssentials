package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CommandNightVision extends VCommand {
    public CommandNightVision(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_NIGHTVISION);
        this.setDescription(Message.DESCRIPTION_NIGHT_VISION);
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);

        if (this.player == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        if (player == this.player) {
            toggleNightVision(player, this.user, sender);
        } else {
            User otherUser = getUser(player);
            toggleNightVision(player, otherUser, sender);
        }

        return CommandResultType.SUCCESS;
    }

    private void toggleNightVision(Player player, User user, CommandSender sender) {

        user.setOption(Option.NIGHT_VISION, !user.getOption(Option.NIGHT_VISION));
        boolean isNightVision = user.getOption(Option.NIGHT_VISION);

        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }

        if (isNightVision) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 1, false, false, false));
        }

        Message messageKey = isNightVision ? Message.COMMAND_NIGHT_VISION_ENABLE : Message.COMMAND_NIGHT_VISION_DISABLE;
        message(sender, messageKey, "%player%", user == this.user ? Message.YOU.getMessageAsString() : player.getName());
    }
}
