package fr.maxlego08.essentials.storage.requests.repositeries;

import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.storage.requests.Repository;
import fr.maxlego08.essentials.storage.requests.SqlConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserOptionRepository extends Repository {

    public UserOptionRepository(SqlConnection connection) {
        super(connection, "player_options");
    }

    public void upsert(UUID uuid, Option option, boolean optionValue) {
        upsert(table -> {
            table.uuid("unique_id", uuid);
            table.string("option_name", option.name());
            table.bool("option_value", optionValue);
        });
    }


    public Map<Option, Boolean> selectOptions(UUID uuid) {
        Map<Option, Boolean> options = new HashMap<>();
        String sql = "SELECT option_name, option_value FROM %s WHERE unique_id = ?";

        this.query(sql, preparedStatement -> preparedStatement.setString(1, uuid.toString()), resultSet -> {
            while (resultSet.next()) {
                options.put(Option.valueOf(resultSet.getString("option_name")), resultSet.getBoolean("option_value"));
            }
        });

        return options;
    }
}
