package fr.maxlego08.essentials.api.commands;

public enum Permission {

    ESSENTIALS_USE,
    ESSENTIALS_RELOAD,

    ESSENTIALS_GAMEMODE,
    ESSENTIALS_GAMEMODE_OTHER,

    ;

    public String asPermission() {
        return name().toLowerCase().replace("_", ".");
    }

}
