package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class CommandItemFrame extends VCommand {

    public CommandItemFrame(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_ITEMFRAME);
        this.setDescription(Message.DESCRIPTION_ITEMFRAME);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection();

        RayTraceResult result = player.getWorld().rayTraceEntities(
                eyeLocation,
                direction,
                5.0,
                entity -> entity instanceof ItemFrame
        );

        if (result == null || result.getHitEntity() == null) {
            message(sender, Message.COMMAND_ITEMFRAME_NOT_FOUND);
            return CommandResultType.DEFAULT;
        }

        Entity entity = result.getHitEntity();
        if (entity instanceof ItemFrame itemFrame) {
            boolean newVisibility = !itemFrame.isVisible();
            itemFrame.setVisible(newVisibility);

            if (newVisibility) {
                message(sender, Message.COMMAND_ITEMFRAME_VISIBLE);
            } else {
                message(sender, Message.COMMAND_ITEMFRAME_INVISIBLE);
            }
            return CommandResultType.SUCCESS;
        }

        message(sender, Message.COMMAND_ITEMFRAME_NOT_FOUND);
        return CommandResultType.DEFAULT;
    }
}
