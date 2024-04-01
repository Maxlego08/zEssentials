package fr.maxlego08.essentials.storage.storages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.User;
import fr.maxlego08.essentials.api.event.UserEvent;
import fr.maxlego08.essentials.api.event.events.UserFirstJoinEvent;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.storage.Persist;
import fr.maxlego08.essentials.storage.ZUser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JsonStorage implements IStorage {

    private final EssentialsPlugin plugin;
    private final Map<UUID, User> users = new HashMap<>();

    public JsonStorage(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    public File getFolder() {
        return new File(this.plugin.getDataFolder(), "users");
    }

    private void createFolder() {
        File folder = getFolder();
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    @Override
    public void onEnable() {
        this.createFolder();
    }

    @Override
    public void onDisable() {
        this.createFolder();
    }

    private File getUserFile(UUID uniqueId) {
        return new File(getFolder(), uniqueId + ".json");
    }

    @Override
    public void createOrLoad(UUID uniqueId, String playerName) {

        this.createFolder();

        File file = getUserFile(uniqueId);
        Persist persist = this.plugin.getPersist();
        User user = persist.load(User.class, file);
        System.out.println(user);

        // If user is null, we need to create a new user
        if (user == null) {

            user = new ZUser(uniqueId);
            user.setName(playerName);

            this.plugin.getLogger().info(String.format("%s (%s) is a new player !", playerName, uniqueId));
            UserEvent event = new UserFirstJoinEvent(user);
            this.plugin.getScheduler().runNextTick(wrappedTask -> event.callEvent());

            persist.save(user, file);
        }

        this.users.put(uniqueId, user);
    }
}
