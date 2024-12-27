package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.DiscordCodeDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.Optional;

public class LinkCodeRepository extends Repository {

    public LinkCodeRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "link_codes");
    }

    public Optional<DiscordCodeDTO> getCode(String code) {
        return select(DiscordCodeDTO.class, table -> table.where("code", code)).stream().findFirst();
    }

    public void clearCode(DiscordCodeDTO code) {
        this.delete(table -> table.where("code", code.code()));
    }
}
