package fr.maxlego08.essentials.api.storage;

import fr.maxlego08.essentials.api.dto.ServerStorageDTO;

import java.util.List;

public interface ServerStorage {

    void setContents(List<ServerStorageDTO> serverStorageDTOS);

    long getLong(Key key);

    long getLong(Key key, long defaultValue);

    int getInt(Key key);

    int getInt(Key key, int defaultValue);

    String getString(Key key);

    String getString(Key key, String defaultValue);

    boolean getBoolean(Key key);

    boolean getBoolean(Key key, boolean defaultValue);

    boolean exist(Key key);

    void set(Key key, Object object);
}
