package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.database.dto.OptionDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.essentials.storage.database.SqlConnection;

import java.util.List;
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

    public List<OptionDTO> selectOptions(UUID uuid) {
        return select(OptionDTO.class, table -> table.where("unique_id", uuid.toString()));
    }
}
