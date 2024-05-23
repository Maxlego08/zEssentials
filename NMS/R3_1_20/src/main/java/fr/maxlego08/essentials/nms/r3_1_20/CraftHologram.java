package fr.maxlego08.essentials.nms.r3_1_20;

import fr.maxlego08.essentials.api.hologram.Hologram;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CraftHologram extends Hologram {

    private Display.TextDisplay display;

    public CraftHologram(Location location) {
        super(location);

        ServerLevel serverLevel = ((CraftWorld) location.getWorld()).getHandle();
        this.display = new Display.TextDisplay(EntityType.TEXT_DISPLAY, serverLevel);

        display.setPosRaw(location.x(), location.y(), location.z());
        display.setYRot(location.getYaw());
        display.setXRot(location.getPitch());
        this.display.setBillboardConstraints(Display.BillboardConstraints.CENTER);
    }

    @Override
    public void create(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.send(new ClientboundAddEntityPacket(display));
    }

    @Override
    public void delete(Player player) {

    }

    @Override
    public void update(Player player) {

        ((CraftPlayer) player).getHandle().connection.send(new ClientboundTeleportEntityPacket(display));

        display.setText(PaperAdventure.asVanilla(Component.text("SUPER", NamedTextColor.RED)));

        final var values = display.getEntityData().getNonDefaultValues();

        ((CraftPlayer) player).getHandle().connection.send(new ClientboundSetEntityDataPacket(display.getId(), values == null ? new ArrayList<>() : values));

    }
}
