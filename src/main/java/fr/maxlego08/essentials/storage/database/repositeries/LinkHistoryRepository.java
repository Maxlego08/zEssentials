package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.discord.DiscordAction;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.UUID;

public class LinkHistoryRepository extends Repository {

    public LinkHistoryRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "link_histories");
    }

    public void insertLog(DiscordAction action, UUID uniqueId, String minecraftName, String discordName, long userId, String data) {
        this.insert(table -> {
            table.string("action", action.name());
            if (uniqueId != null) table.uuid("minecraft_id", uniqueId);
            if (minecraftName != null) table.string("minecraft_name", minecraftName);
            if (discordName != null) table.string("discord_name", discordName);
            if (userId != -1) table.bigInt("discord_id", userId);
            if (data != null) table.object("data", data);
        });
    }
}
