package fr.maxlego08.essentials.api.storage;

import org.bukkit.plugin.Plugin;

public class StorageKey implements Key {

    private final String plugin;
    private final String name;

    public StorageKey(String plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public StorageKey(Plugin plugin, String name) {
        this.plugin = plugin.getName().toLowerCase();
        this.name = name;
    }

    public StorageKey(String key) {
        String[] keys = key.split(":", 2);
        this.plugin = keys[0];
        this.name = keys[1];
    }

    @Override
    public String getPlugin() {
        return this.plugin;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getKey() {
        return getPlugin() + ":" + getName();
    }
}
