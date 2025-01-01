package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.DiscordAccountDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.Optional;
import java.util.UUID;

public class LinkAccountRepository extends Repository {

    public LinkAccountRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "link_accounts");
    }

    public void insert(UUID uniqueId, String minecraftName, String discordName, long userId) {
        this.insert(table -> {
            table.uuid("unique_id", uniqueId);
            table.string("minecraft_name", minecraftName);
            table.string("discord_name", discordName);
            table.bigInt("user_id", userId);
        });
    }

    public Optional<DiscordAccountDTO> select(UUID uniqueId) {
        return this.select(DiscordAccountDTO.class, table -> table.where("unique_id", uniqueId)).stream().findFirst();
    }

    public void delete(UUID uniqueId) {
        this.delete(table -> table.where("unique_id", uniqueId));
    }
}
