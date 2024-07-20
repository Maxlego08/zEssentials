package fr.maxlego08.essentials.api.storage;

import fr.maxlego08.essentials.api.dto.ServerStorageDTO;

import java.util.List;

public interface ServerStorage {

    void setContents(List<ServerStorageDTO> serverStorageDTOS);
}
