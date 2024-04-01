package fr.maxlego08.essentials.api;

import java.util.UUID;

public interface User {

    UUID getUniqueId();

    String getName();

    void setName(String name);
}
