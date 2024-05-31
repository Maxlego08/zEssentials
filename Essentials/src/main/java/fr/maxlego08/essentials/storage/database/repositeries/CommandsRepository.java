package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.database.dto.CommandDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.List;
import java.util.UUID;

public class CommandsRepository extends Repository {
    public CommandsRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "commands");
    }

    public void insert(CommandDTO chatMessage) {
        insert(table -> {
            table.uuid("unique_id", chatMessage.unique_id());
            table.string("command", chatMessage.command());
        });
    }

    public List<CommandDTO> getCommands(UUID uuid) {
        return this.select(CommandDTO.class, table -> {
            table.uuid("unique_id", uuid);
            table.orderByDesc("created_at");
        });
    }
}
