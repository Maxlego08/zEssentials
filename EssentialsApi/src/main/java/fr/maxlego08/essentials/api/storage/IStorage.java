package fr.maxlego08.essentials.api.storage;

import fr.maxlego08.essentials.api.User;

import java.util.UUID;
import java.util.function.Consumer;

public interface IStorage {

    void onEnable();

    void onDisable();

    void createOrLoad(UUID uniqueId, String playerName);
}
