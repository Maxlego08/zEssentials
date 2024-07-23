package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.dto.ServerStorageDTO;
import fr.maxlego08.essentials.api.storage.Key;
import fr.maxlego08.essentials.api.storage.ServerStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ZServerStorage implements ServerStorage {

    private final ZEssentialsPlugin plugin;
    private Map<String, Object> contents = new HashMap<>();

    public ZServerStorage(ZEssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void setContents(List<ServerStorageDTO> serverStorageDTOS) {
        System.out.println(serverStorageDTOS);
        this.contents = serverStorageDTOS.stream().collect(Collectors.toMap(ServerStorageDTO::name, ServerStorageDTO::content));
        System.out.println(contents);
    }

    @Override
    public long getLong(Key key) {
        return getLong(key, 0L);
    }

    @Override
    public long getLong(Key key, long defaultValue) {
        Object value = contents.get(key.getKey());
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaultValue;
    }

    @Override
    public int getInt(Key key) {
        return getInt(key, 0);
    }

    @Override
    public int getInt(Key key, int defaultValue) {
        Object value = contents.get(key.getKey());
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaultValue;
    }

    @Override
    public String getString(Key key) {
        return getString(key, null);
    }

    @Override
    public String getString(Key key, String defaultValue) {
        Object value = contents.get(key.getKey());
        if (value instanceof String) {
            return (String) value;
        }
        return defaultValue;
    }

    @Override
    public boolean getBoolean(Key key) {
        return getBoolean(key, false);
    }

    @Override
    public boolean getBoolean(Key key, boolean defaultValue) {
        Object value = contents.get(key.getKey());
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return defaultValue;
    }

    @Override
    public boolean exist(Key key) {
        return this.contents.containsKey(key);
    }

    @Override
    public void set(Key key, Object object) {
        this.contents.put(key.getKey(), object);
        this.plugin.getStorageManager().getStorage().updateServerStorage(key.getKey(), object);
    }
}
