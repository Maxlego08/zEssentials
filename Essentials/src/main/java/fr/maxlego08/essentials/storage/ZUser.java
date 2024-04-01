package fr.maxlego08.essentials.storage;

import fr.maxlego08.essentials.api.User;

import java.util.UUID;

public class ZUser implements User {

    private final UUID uniqueId;
    private String name;

    public ZUser(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
