package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.OptionDTO;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class UserOptionRepository extends Repository {

    public UserOptionRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "user_options");
    }

    public void upsert(UUID uuid, Option option, boolean optionValue) {
        upsert(table -> {
            table.uuid("unique_id", uuid).primary();
            table.string("option_name", option.name());
            table.bool("option_value", optionValue);
        });
    }

    public List<OptionDTO> select(UUID uuid) {
        return select(OptionDTO.class, table -> table.where("unique_id", uuid));
    }

    public void select(UUID uuid, Option option, Consumer<Boolean> consumer) {
        var result = select(OptionDTO.class, table -> table.where("unique_id", uuid).where("option_name", option.name()));
        if (result.isEmpty()) {
            consumer.accept(false);
            return;
        }
        var optionDTO = result.getFirst();
        consumer.accept(optionDTO.option_value());
    }
}
