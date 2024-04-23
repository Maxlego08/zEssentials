package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.database.dto.UserDTO;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.essentials.storage.database.SqlConnection;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserRepository extends Repository {

    public UserRepository(SqlConnection connection) {
        super(connection, "users");
    }

    /**
     * Allows to update the player’s data
     *
     * @param uuid The user's uuid
     * @param name The user's name
     */
    public void upsert(UUID uuid, String name) {
        upsert(table -> {
            table.uuid("unique_id", uuid);
            table.string("name", name);
        });
    }

    /**
     * Allows to update the player’s data with the last location
     *
     * @param user The user to update
     */
    public void upsert(User user) {
        upsert(table -> {
            table.uuid("unique_id", user.getUniqueId());
            table.string("name", user.getName());
            table.location("last_location", user.getLastLocation());
        });
    }

    /**
     * Removes sanctions that have expired
     * We will execute an UPDATE query with a LEFT JOIN
     */
    public void clearExpiredSanctions() {
        // Removes ban sanctions
        update(table -> {
            table.leftJoin("%prefix%sanctions", "zs", "id", "%prefix%users", "ban_sanction_id");
            table.string("ban_sanction_id", null);
            table.where("zs.expired_at", "<", new Date());
        });
        // Removes mute sanctions
        update(table -> {
            table.leftJoin("%prefix%sanctions", "zs", "id", "%prefix%users", "mute_sanction_id");
            table.string("mute_sanction_id", null);
            table.where("zs.expired_at", "<", new Date());
        });
    }

    /**
     * Updates the ban sanction ID for a specified user.
     *
     * @param uuid  The UUID of the user.
     * @param index The index of the ban sanction ID.
     */
    public void updateBanId(UUID uuid, Integer index) {
        update(table -> {
            table.decimal("ban_sanction_id", index);
            table.where("unique_id", uuid);
        });
    }

    /**
     * Updates the mute sanction ID for a specified user.
     *
     * @param uuid  The UUID of the user.
     * @param index The index of the mute sanction ID.
     */
    public void updateMuteId(UUID uuid, Integer index) {
        update(table -> {
            table.decimal("mute_sanction_id", index);
            table.where("unique_id", uuid);
        });
    }

    /**
     * Selects a list of users based on their username.
     *
     * @param userName The username to search for.
     * @return A list of UserDTO objects corresponding to the found users.
     */
    public List<UserDTO> selectUsers(String userName) {
        return select(UserDTO.class, table -> table.where("name", userName));
    }

    /**
     * Selects a user based on their unique UUID.
     *
     * @param uniqueId The unique UUID of the user to search for.
     * @return A list of UserDTO objects corresponding to the found user.
     */
    public List<UserDTO> selectUser(UUID uniqueId) {
        return select(UserDTO.class, table -> table.where("unique_id", uniqueId));
    }

    /**
     * Returns the total number of users.
     *
     * @return The total number of users.
     */
    public long totalUsers() {
        return select(schema -> {
        });
    }

    public void updatePlayTime(UUID uniqueId, long sessionPlayTime) {
        update(table -> {
            table.decimal("play_time", sessionPlayTime);
            table.where("unique_id", uniqueId);
        });
    }
}
