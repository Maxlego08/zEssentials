package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.EconomyDTO;
import fr.maxlego08.essentials.api.dto.SanctionDTO;
import fr.maxlego08.essentials.api.event.UserEvent;
import fr.maxlego08.essentials.api.event.events.user.UserFirstJoinEvent;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.api.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class StorageHelper extends ZUtils {

    protected final EssentialsPlugin plugin;
    protected final Map<UUID, User> users = new HashMap<>();
    protected final Map<String, UUID> localUUIDS = new HashMap<>();
    protected final Map<UUID, Sanction> banSanctions = new HashMap<>();
    protected long totalUser = 0;

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

    protected Optional<UUID> getLocalUniqueId(String userName) {
        return this.users.values().stream().filter(user -> user.getName().equalsIgnoreCase(userName)).map(User::getUniqueId).findFirst();
    }

    public long totalUsers() {
        return this.totalUser;
    }

    protected void firstJoin(User user) {
        user.setFirstJoin();
        this.totalUser += 1;
        this.plugin.setLastFirstJoinPlayerName(user.getName());
        this.plugin.getLogger().info(String.format("%s (%s) is a new player !", user.getName(), user.getUniqueId()));
        UserEvent event = new UserFirstJoinEvent(user);
        this.plugin.getScheduler().runNextTick(wrappedTask -> event.callEvent());
    }

    protected void setActiveSanctions(List<SanctionDTO> activeBans) {
        activeBans.forEach(sanctionDTO -> this.banSanctions.put(sanctionDTO.player_unique_id(), Sanction.fromDTO(sanctionDTO)));
    }

    public Map<UUID, Sanction> getBanSanctions() {
        return banSanctions;
    }

    public boolean isBan(UUID uuid) {
        Sanction sanction = this.banSanctions.get(uuid);
        return sanction != null && sanction.isActive();
    }

    public Sanction getBan(UUID uuid) {
        return this.banSanctions.get(uuid);
    }
}
