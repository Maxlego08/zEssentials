package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.UserDTO;
import fr.maxlego08.essentials.api.dto.UserEconomyRankingDTO;
import fr.maxlego08.essentials.api.dto.UserVoteDTO;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.convert.cmi.CMIUser;
import fr.maxlego08.essentials.convert.sunlight.SunlightUser;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;
import fr.maxlego08.sarah.conditions.JoinCondition;
import fr.maxlego08.sarah.database.DatabaseType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserRepository extends Repository {

    public UserRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "users");
    }

    /**
     * Allows updating the player’s data
     *
     * @param uuid The user's uuid
     * @param name The user's name
     */
    public void upsert(UUID uuid, String name) {
        upsert(table -> {
            table.uuid("unique_id", uuid).primary();
            table.string("name", name);
        });
    }

    /**
     * Allows updating the player’s data with the last location
     *
     * @param user The user to update
     */
    public void upsert(User user) {
        upsert(table -> {
            table.uuid("unique_id", user.getUniqueId()).primary();
            table.string("name", user.getName());
            table.string("last_location", locationAsString(user.getLastLocation()));
        });
    }

    /**
     * Removes sanctions that have expired
     * We will execute an UPDATE query with a LEFT JOIN
     */
    public void clearExpiredSanctions() {
        if (this.connection.getDatabaseConfiguration().getDatabaseType() == DatabaseType.SQLITE) {

            // TODO - Update Sarah SQLITE for left join
            plugin.getLogger().warning("Attention, SQLITE does not allow to execute all sql queries, the query that allows to delete inactive sanctions is currently not working.");
            /*update(table -> {
                table.string("ban_sanction_id", null);
                table.whereIn("ban_sanction_id", "%prefix%sanctions", "zs", "id", "%prefix%users", "mute_sanction_id", subTable -> {
                    subTable.where("zs.expired_at", "<", new Date());
                });
            });

            update(table -> {
                table.string("mute_sanction_id", null);
                table.whereIn("ban_sanction_id", "%prefix%sanctions", "zs", "id", "%prefix%users", "ban_sanction_id", subTable -> {
                    subTable.where("zs.expired_at", "<", new Date());
                });
            });*/

        } else {
            // Removes ban sanctions
            update(table -> {
                table.leftJoin("%prefix%sanctions", "zs", "id", "%prefix%users", "ban_sanction_id");
                table.string("ban_sanction_id", null);
                table.where("zs", "expired_at", "<", new Date());
            });
            // Removes mute sanctions
            update(table -> {
                table.leftJoin("%prefix%sanctions", "zs", "id", "%prefix%users", "mute_sanction_id");
                table.string("mute_sanction_id", null);
                table.where("zs", "expired_at", "<", new Date());
            });
        }
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
     * Selects a user based on their unique UUID, for vote data
     *
     * @param uniqueId The unique UUID of the user to search for.
     * @return A list of UserDTO objects corresponding to the found user.
     */
    public List<UserDTO> selectUser(UUID uniqueId) {
        return select(UserDTO.class, table -> table.where("unique_id", uniqueId));
    }

    /**
     * Selects a user based on their unique UUID.
     *
     * @param uniqueId The unique UUID of the user to search for.
     * @return A list of UserDTO objects corresponding to the found user.
     */
    public List<UserVoteDTO> selectVoteUser(UUID uniqueId) {
        return select(UserVoteDTO.class, table -> table.where("unique_id", uniqueId));
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

    public List<UserDTO> getUsers(String ip) {
        return select(UserDTO.class, table -> {
            table.distinct();
            table.leftJoin("%prefix%user_play_times", "pt", "unique_id", "%prefix%users", "unique_id");
            table.where("pt.address", ip);
        });
    }

    public List<UserEconomyRankingDTO> getBalanceRanking(String economyName) {
        return select(UserEconomyRankingDTO.class, table -> {

            table.addSelect("%prefix%users", "unique_id");
            table.addSelect("name");
            table.addSelect("ze", "amount", "amount", 0);
            JoinCondition joinCondition = JoinCondition.and("ze", "economy_name", economyName);
            table.leftJoin("%prefix%economies", "ze", "unique_id", "%prefix%users", "unique_id", joinCondition);
            table.orderByDesc("amount");

        });
    }

    public void setVote(UUID uniqueId, long vote, long offline) {
        update(table -> {
            if (vote >= 0) {
                table.object("vote", vote);
            }
            if (offline >= 0) {
                table.object("vote_offline", offline);
            }
            table.where("unique_id", uniqueId);
        });
    }

    public void resetVotes() {
        update(table -> table.object("vote", 0));
    }

    public void upsert(CMIUser cmiUser) {

        upsert(table -> {
            table.uuid("unique_id", cmiUser.player_uuid()).primary();
            table.string("name", cmiUser.username());
            table.bigInt("play_time", cmiUser.TotalPlayTime() / 1000);

            if (cmiUser.LogOutLocation() != null) {
                table.string("last_location", cmiUser.LogOutLocation().replace(":", ","));
            }

            if (cmiUser.LastLoginTime() > 0) {
                var date = new Date(cmiUser.LastLoginTime());
                table.object("created_at", date);
                table.object("updated_at", date);
            }
        });
    }

    public void upsert(SunlightUser sunlightUser) {

        upsert(table -> {
            table.uuid("unique_id", sunlightUser.uuid()).primary();
            table.string("name", sunlightUser.name());

            long createdAt = sunlightUser.dateCreated();
            if (createdAt > 0) {
                Date date = new Date(createdAt);
                table.object("created_at", date);
                table.object("updated_at", date);
            }
        });
    }
}
