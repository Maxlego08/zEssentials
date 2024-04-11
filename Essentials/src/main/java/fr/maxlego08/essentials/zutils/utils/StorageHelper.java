package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.database.dto.EconomyDTO;
import fr.maxlego08.essentials.api.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class StorageHelper {

    protected final EssentialsPlugin plugin;
    protected final Map<UUID, User> users = new HashMap<>();

    public StorageHelper(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    protected void async(Runnable runnable) {
        this.plugin.getScheduler().runAsync(wrappedTask -> runnable.run());
    }

    protected List<EconomyDTO> getLocalEconomyDTO(String userName) {
        Optional<User> optional = this.users.values().stream().filter(user -> user.getName().equalsIgnoreCase(userName)).findFirst();
        return optional.map(user -> user.getBalances().entrySet().stream().map(e -> new EconomyDTO(e.getKey(), e.getValue())).toList()).orElseGet(ArrayList::new);
    }

}
