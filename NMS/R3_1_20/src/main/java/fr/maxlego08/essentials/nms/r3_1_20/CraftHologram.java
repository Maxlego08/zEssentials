package fr.maxlego08.essentials.nms.r3_1_20;

import com.mojang.math.Transformation;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.hologram.configuration.HologramConfiguration;
import fr.maxlego08.essentials.api.hologram.configuration.TextHologramConfiguration;
import fr.maxlego08.essentials.api.utils.ReflectionUtils;
import io.papermc.paper.adventure.PaperAdventure;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Brightness;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.joml.Quaternionf;

import java.util.ArrayList;

public class CraftHologram extends Hologram {

    private Display display = null;

    public CraftHologram(EssentialsPlugin plugin, HologramType hologramType, HologramConfiguration configuration, String fileName, String name, Location location) {
        super(plugin, hologramType, name, fileName, location, configuration);
    }

    @Override
    public void create(Player player) {
        send(player, new ClientboundAddEntityPacket(display));

        this.update(player);
    }

    @Override
    public void delete(Player player) {
        System.out.println("JE SUPPRIMER l'entité id: " + display.getId());
        this.send(player, new ClientboundRemoveEntitiesPacket(display.getId()));
    }

    @Override
    public void update(Player player) {

        send(player, new ClientboundTeleportEntityPacket(display));

        if (display instanceof Display.TextDisplay textDisplay) {
            textDisplay.setText(PaperAdventure.asVanilla(getComponentText(player)));
        }

        final var values = display.getEntityData().getNonDefaultValues();
        send(player, new ClientboundSetEntityDataPacket(display.getId(), values == null ? new ArrayList<>() : values));
    }

    @Override
    public void create() {
        ServerLevel serverLevel = ((CraftWorld) location.getWorld()).getHandle();
        switch (hologramType) {
            case BLOCK -> this.display = new Display.BlockDisplay(EntityType.BLOCK_DISPLAY, serverLevel);
            case ITEM -> this.display = new Display.ItemDisplay(EntityType.ITEM_DISPLAY, serverLevel);
            case TEXT -> this.display = new Display.TextDisplay(EntityType.TEXT_DISPLAY, serverLevel);
        }

        display.setPosRaw(location.x(), location.y(), location.z());
        display.setYRot(location.getYaw());
        display.setXRot(location.getPitch());

        this.display.setBillboardConstraints(Display.BillboardConstraints.CENTER);

        this.update();
    }

    @Override
    public void update() {

        display.setBillboardConstraints(switch (this.configuration.getBillboard()) {
            case FIXED -> Display.BillboardConstraints.FIXED;
            case VERTICAL -> Display.BillboardConstraints.VERTICAL;
            case HORIZONTAL -> Display.BillboardConstraints.HORIZONTAL;
            case CENTER -> Display.BillboardConstraints.CENTER;
        });

        if (display instanceof Display.TextDisplay textDisplay && configuration instanceof TextHologramConfiguration configuration) {
            // line width
            final var DATA_LINE_WIDTH_ID = ReflectionUtils.getStaticValue(Display.TextDisplay.class, "aN");
            display.getEntityData().set((EntityDataAccessor<Integer>) DATA_LINE_WIDTH_ID, Hologram.LINE_WIDTH);

            // background
            final var DATA_BACKGROUND_COLOR_ID = ReflectionUtils.getStaticValue(Display.TextDisplay.class, "aO");

            final var background = configuration.getBackground();
            if (background == null) {
                display.getEntityData().set((EntityDataAccessor<Integer>) DATA_BACKGROUND_COLOR_ID, Display.TextDisplay.INITIAL_BACKGROUND);
            } else if (background == Hologram.TRANSPARENT) {
                display.getEntityData().set((EntityDataAccessor<Integer>) DATA_BACKGROUND_COLOR_ID, 0);
            } else {
                display.getEntityData().set((EntityDataAccessor<Integer>) DATA_BACKGROUND_COLOR_ID, background.value() | 0xC8000000);
            }

            // text shadow
            if (configuration.isTextShadow()) {
                textDisplay.setFlags((byte) (textDisplay.getFlags() | Display.TextDisplay.FLAG_SHADOW));
            } else {
                textDisplay.setFlags((byte) (textDisplay.getFlags() & ~Display.TextDisplay.FLAG_SHADOW));
            }

            // text alignment
            if (configuration.getTextAlignment() == org.bukkit.entity.TextDisplay.TextAlignment.LEFT) {
                textDisplay.setFlags((byte) (textDisplay.getFlags() | Display.TextDisplay.FLAG_ALIGN_LEFT));
            } else {
                textDisplay.setFlags((byte) (textDisplay.getFlags() & ~Display.TextDisplay.FLAG_ALIGN_LEFT));
            }

            // see through
            if (configuration.isSeeThrough()) {
                textDisplay.setFlags((byte) (textDisplay.getFlags() | Display.TextDisplay.FLAG_SEE_THROUGH));
            } else {
                textDisplay.setFlags((byte) (textDisplay.getFlags() & ~Display.TextDisplay.FLAG_SEE_THROUGH));
            }

            if (configuration.getTextAlignment() == org.bukkit.entity.TextDisplay.TextAlignment.RIGHT) {
                textDisplay.setFlags((byte) (textDisplay.getFlags() | Display.TextDisplay.FLAG_ALIGN_RIGHT));
            } else {
                textDisplay.setFlags((byte) (textDisplay.getFlags() & ~Display.TextDisplay.FLAG_ALIGN_RIGHT));
            }

        }

        if (this.configuration.getBrightness() != null) {
            display.setBrightnessOverride(new Brightness(this.configuration.getBrightness().getBlockLight(), this.configuration.getBrightness().getSkyLight()));
        }

        display.setTransformation(new Transformation(
                this.configuration.getTranslation(),
                new Quaternionf(),
                this.configuration.getScale(),
                new Quaternionf())
        );

        display.setShadowRadius(this.configuration.getShadowRadius());
        display.setShadowStrength(this.configuration.getShadowStrength());
    }

    private void send(Player player, Packet<?> packet) {
        ((CraftPlayer) player).getHandle().connection.send(packet);
    }

}
