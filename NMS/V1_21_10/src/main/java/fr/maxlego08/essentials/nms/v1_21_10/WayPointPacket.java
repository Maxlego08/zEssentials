package fr.maxlego08.essentials.nms.v1_21_10;

import fr.maxlego08.essentials.api.waypoint.WayPointHelper;
import fr.maxlego08.essentials.api.waypoint.WayPointIcon;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.waypoints.Waypoint;
import net.minecraft.world.waypoints.WaypointStyleAssets;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class WayPointPacket implements WayPointHelper {

    @Override
    public void addWayPoint(Player player, UUID uniqueId, Location location, WayPointIcon wayPointIcon) {

        var icon = toIcon(wayPointIcon);
        var blockPos = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        var packet = ClientboundTrackedWaypointPacket.addWaypointPosition(uniqueId, icon, blockPos);

        sendPacket(player, packet);
    }

    @Override
    public void updateWayPointPosition(Player player, UUID uniqueId, Location location, WayPointIcon wayPointIcon) {

        var icon = toIcon(wayPointIcon);
        var blockPos = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        var packet = ClientboundTrackedWaypointPacket.updateWaypointPosition(uniqueId, icon, blockPos);

        sendPacket(player, packet);
    }

    @Override
    public void addWayPoint(Player player, UUID uniqueId, Chunk chunk, WayPointIcon wayPointIcon) {

        var icon = toIcon(wayPointIcon);
        var chunkPos = new ChunkPos(chunk.getX(), chunk.getZ());
        var packet = ClientboundTrackedWaypointPacket.addWaypointChunk(uniqueId, icon, chunkPos);

        sendPacket(player, packet);
    }

    @Override
    public void updateWayPointPosition(Player player, UUID uniqueId, Chunk chunk, WayPointIcon wayPointIcon) {

        var icon = toIcon(wayPointIcon);
        var chunkPos = new ChunkPos(chunk.getX(), chunk.getZ());
        var packet = ClientboundTrackedWaypointPacket.updateWaypointChunk(uniqueId, icon, chunkPos);

        sendPacket(player, packet);
    }

    @Override
    public void removeWayPoint(Player player, UUID uniqueId) {

        var packet = ClientboundTrackedWaypointPacket.removeWaypoint(uniqueId);
        sendPacket(player, packet);
    }

    private void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer) player).getHandle().connection.send(packet);
    }

    /**
     * Converts a {@link WayPointIcon} to a {@link Waypoint.Icon}.
     * <p>
     * If the given {@link WayPointIcon} has a texture, then the style of the resulting icon will be set to the
     * death waypoint style. Otherwise, the style will be left unchanged.
     * <p>
     * The color of the resulting icon will be set to the color of the given {@link WayPointIcon}.
     *
     * @param wayPointIcon the icon to convert
     * @return the converted icon
     */
    private Waypoint.Icon toIcon(WayPointIcon wayPointIcon) {
        var icon = new Waypoint.Icon();
        if (wayPointIcon.texture() != null) {
            icon.style = WaypointStyleAssets.createId(wayPointIcon.texture());
        }
        icon.color = Optional.of(wayPointIcon.color().getRGB() & 0xFFFFFF);
        return icon;
    }
}
