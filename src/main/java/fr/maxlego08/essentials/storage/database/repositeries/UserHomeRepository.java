package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.HomeDTO;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.essentials.user.ZHome;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.List;
import java.util.UUID;

public class UserHomeRepository extends Repository {

    public UserHomeRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "user_homes");
    }

    public void upsert(UUID uuid, Home home) {
        upsert(table -> {
            table.uuid("unique_id", uuid).primary();
            table.string("name", home.getName());
            table.string("location", locationAsString(home.getLocation()));
            table.string("material", home.getMaterial() != null ? home.getMaterial().name() : null);
        });
    }

    public List<HomeDTO> select(UUID uuid) {
        return select(HomeDTO.class, schema -> schema.where("unique_id", uuid));
    }

    public void deleteHomes(UUID uuid, String name) {
        delete(table -> table.where("unique_id", name).where("name", name));
    }

    public List<Home> getHomes(UUID uuid, String homeName) {
        return select(HomeDTO.class, schema -> schema.where("unique_id", uuid).where("name", homeName)).stream().map(homeDTO -> (Home) new ZHome(stringAsLocation(homeDTO.location()), homeDTO.name(), null)).toList();
    }

    public List<Home> getHomes(UUID uuid) {
        return select(HomeDTO.class, schema -> schema.where("unique_id", uuid)).stream().map(homeDTO -> (Home) new ZHome(stringAsLocation(homeDTO.location()), homeDTO.name(), null)).toList();
    }
}
