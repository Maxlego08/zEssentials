package fr.maxlego08.essentials.api;

import fr.maxlego08.essentials.api.commands.CommandCooldown;
import fr.maxlego08.essentials.api.server.RedisConfiguration;
import fr.maxlego08.essentials.api.server.ServerType;
import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;
import fr.maxlego08.essentials.api.storage.StorageType;
import fr.maxlego08.essentials.api.utils.CompactMaterial;
import org.bukkit.permissions.Permissible;

import java.util.List;
import java.util.Optional;

public interface Configuration extends ConfigurationFile {

    boolean isEnableDebug();

    boolean isEnableCooldownBypass();

    List<CommandCooldown> getCommandCooldown();

    Optional<Integer> getCooldown(Permissible permissible, String command);

    int getTrashSize();

    List<CompactMaterial> getCompactMaterials();

    StorageType getStorageType();

    DatabaseConfiguration getDatabaseConfiguration();

    ServerType getServerType();

    RedisConfiguration getRedisConfiguration();

}
