package fr.maxlego08.essentials.api.waypoint;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface WayPointHelper {

    /**
     * Adds a waypoint to a player's map.
     *
     * @param player       the player to add the waypoint to
     * @param uniqueId     the unique ID of the waypoint
     * @param location     the location of the waypoint
     * @param wayPointIcon the icon of the waypoint
     */
    void addWayPoint(Player player, UUID uniqueId, Location location, WayPointIcon wayPointIcon);

    /**
     * Updates the position of an already existing waypoint on a player's map.
     *
     * @param player       the player to update the waypoint on
     * @param uniqueId     the unique ID of the waypoint
     * @param location     the new location of the waypoint
     * @param wayPointIcon the icon of the waypoint
     */
    void updateWayPointPosition(Player player, UUID uniqueId, Location location, WayPointIcon wayPointIcon);

    /**
     * Adds a waypoint to a player's map.
     *
     * @param player       the player to add the waypoint to
     * @param uniqueId     the unique ID of the waypoint
     * @param chunk        the chunk where the waypoint is located
     * @param wayPointIcon the icon of the waypoint
     */
    void addWayPoint(Player player, UUID uniqueId, Chunk chunk, WayPointIcon wayPointIcon);

    /**
     * Updates the position of an already existing waypoint in a specific chunk on a player's map.
     *
     * @param player       the player whose waypoint is to be updated
     * @param uniqueId     the unique ID of the waypoint
     * @param chunk        the new chunk where the waypoint is located
     * @param wayPointIcon the icon of the waypoint
     */
    void updateWayPointPosition(Player player, UUID uniqueId, Chunk chunk, WayPointIcon wayPointIcon);

    /**
     * Removes a waypoint from a player's map.
     *
     * @param player   the player from whom to remove the waypoint
     * @param uniqueId the unique ID of the waypoint
     */
    void removeWayPoint(Player player, UUID uniqueId);

}
