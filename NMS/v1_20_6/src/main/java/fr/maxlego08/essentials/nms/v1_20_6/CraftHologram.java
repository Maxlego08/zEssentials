package fr.maxlego08.essentials.nms.v1_20_6;

import com.mojang.math.Transformation;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.hologram.configuration.BlockHologramConfiguration;
import fr.maxlego08.essentials.api.hologram.configuration.HologramConfiguration;
import fr.maxlego08.essentials.api.hologram.configuration.ItemHologramConfiguration;
import fr.maxlego08.essentials.api.hologram.configuration.TextHologramConfiguration;
import fr.maxlego08.essentials.api.utils.ReflectionUtils;
import fr.maxlego08.essentials.api.utils.SafeLocation;
import io.papermc.paper.adventure.PaperAdventure;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Brightness;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.joml.Quaternionf;

import java.util.ArrayList;

public class CraftHologram extends Hologram {

    private Display display = null;

    public CraftHologram(EssentialsPlugin plugin, HologramType hologramType, HologramConfiguration configuration, String fileName, String name, SafeLocation location) {
        super(plugin, hologramType, name, fileName, location, configuration);
    }

    @Override
    public void create(Player player) {
        send(player, new ClientboundAddEntityPacket(display));

        this.update(player);
    }

    @Override
    public void delete(Player player) {
        this.send(player, new ClientboundRemoveEntitiesPacket(display.getId()));
    }

    @Override
    public void update(Player player) {

        send(player, new ClientboundTeleportEntityPacket(display));

        if (display instanceof Display.TextDisplay textDisplay) {
            textDisplay.setText(PaperAdventure.asVanilla(getComponentText(player)));
        }

        final var values = new ArrayList<SynchedEntityData.DataValue<?>>();
        for (final var item : ((SynchedEntityData.DataItem<?>[]) ReflectionUtils.getValue(display.getEntityData(), "itemsById"))) {
            values.add(item.value());
        }
        send(player, new ClientboundSetEntityDataPacket(display.getId(), values));
    }

    @Override
    public void create() {
        ServerLevel serverLevel = ((CraftWorld) location.getLocation().getWorld()).getHandle();
        switch (hologramType) {
            case BLOCK -> this.display = new Display.BlockDisplay(EntityType.BLOCK_DISPLAY, serverLevel);
            case ITEM -> this.display = new Display.ItemDisplay(EntityType.ITEM_DISPLAY, serverLevel);
            case TEXT -> this.display = new Display.TextDisplay(EntityType.TEXT_DISPLAY, serverLevel);
        }

        display.getEntityData().set((EntityDataAccessor<Integer>) ReflectionUtils.getStaticValue(Display.class, "DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID"), 1); // Transformation duration
        display.getEntityData().set((EntityDataAccessor<Integer>) ReflectionUtils.getStaticValue(Display.class, "DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID"), 0); // Interpolation start

        this.updateLocation();
        this.update();
    }

    @Override
    public void updateLocation() {
        display.setPosRaw(location.getX(), location.getY(), location.getZ());
        display.setYRot(location.getYaw());
        display.setXRot(location.getPitch());
    }

    @Override
    public void update() {

        display.setBillboardConstraints(switch (this.configuration.getBillboard()) {
            case FIXED -> Display.BillboardConstraints.FIXED;
            case VERTICAL -> Display.BillboardConstraints.VERTICAL;
            case HORIZONTAL -> Display.BillboardConstraints.HORIZONTAL;
            case CENTER -> Display.BillboardConstraints.CENTER;
        });

        if (display instanceof Display.TextDisplay textDisplay && configuration instanceof TextHologramConfiguration textHologramConfiguration) {

            display.getEntityData().set((EntityDataAccessor<Integer>) ReflectionUtils.getStaticValue(Display.TextDisplay.class, "DATA_LINE_WIDTH_ID"), Hologram.LINE_WIDTH);

            var backgroundColor = (EntityDataAccessor<Integer>) ReflectionUtils.getStaticValue(Display.TextDisplay.class, "DATA_BACKGROUND_COLOR_ID");
            var background = textHologramConfiguration.getBackground();
            int backgroundValue = (background == null) ? Display.TextDisplay.INITIAL_BACKGROUND : (background == Hologram.TRANSPARENT) ? 0 : background.value() | 0xC8000000;
            display.getEntityData().set(backgroundColor, backgroundValue);

            byte flags = textDisplay.getFlags();
            flags = (byte) (textHologramConfiguration.isTextShadow() ? (flags | Display.TextDisplay.FLAG_SHADOW) : (flags & ~Display.TextDisplay.FLAG_SHADOW));
            flags = (byte) (textHologramConfiguration.getTextAlignment() == org.bukkit.entity.TextDisplay.TextAlignment.LEFT ? (flags | Display.TextDisplay.FLAG_ALIGN_LEFT) : (flags & ~Display.TextDisplay.FLAG_ALIGN_LEFT));
            flags = (byte) (textHologramConfiguration.isSeeThrough() ? (flags | Display.TextDisplay.FLAG_SEE_THROUGH) : (flags & ~Display.TextDisplay.FLAG_SEE_THROUGH));
            flags = (byte) (textHologramConfiguration.getTextAlignment() == org.bukkit.entity.TextDisplay.TextAlignment.RIGHT ? (flags | Display.TextDisplay.FLAG_ALIGN_RIGHT) : (flags & ~Display.TextDisplay.FLAG_ALIGN_RIGHT));
            textDisplay.setFlags(flags);
        } else if (display instanceof Display.ItemDisplay itemDisplay && configuration instanceof ItemHologramConfiguration itemHologramConfiguration) {

            itemDisplay.setItemStack(ItemStack.fromBukkitCopy(itemHologramConfiguration.getItemStack()));
        } else if (display instanceof Display.BlockDisplay blockDisplay && configuration instanceof BlockHologramConfiguration blockData) {

            Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.of("minecraft:" + blockData.getMaterial().name().toLowerCase(), ':'));
            blockDisplay.setBlockState(block.defaultBlockState());
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
