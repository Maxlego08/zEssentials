package fr.maxlego08.essentials.api.user;

import fr.maxlego08.essentials.api.commands.Permission;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface User {

    UUID getUniqueId();

    String getName();

    void setName(String name);

    void sendTeleportRequest(User targetUser);

    Player getPlayer();

    boolean isOnline();

    boolean isIgnore(UUID uniqueId);

    TeleportRequest getTeleportRequest();

    void setTeleportRequest(TeleportRequest teleportRequest);

    Collection<TeleportRequest> getTeleportRequests();

    void removeTeleportRequest(User user);

    void teleport(Location location);

    boolean hasPermission(Permission permission);
}
