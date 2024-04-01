package fr.maxlego08.essentials.api.commands;

public enum Permission {

    ESSENTIALS_USE,
    ESSENTIALS_RELOAD,

    ESSENTIALS_GAMEMODE,
    ESSENTIALS_GAMEMODE_OTHER,
    ESSENTIALS_GAMEMODE_CREATIVE,
    ESSENTIALS_GAMEMODE_SURVIVAL,
    ESSENTIALS_GAMEMODE_SPECTATOR,
    ESSENTIALS_GAMEMODE_ADVENTURE;

    public String asPermission() {
        return name().toLowerCase().replace("_", ".");
    }

}
