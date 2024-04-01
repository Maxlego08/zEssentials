package fr.maxlego08.essentials.api.commands;

public enum Permission {

    ESSENTIALS_USE,
    ESSENTIALS_RELOAD,

    ESSENTIALS_GAMEMODE,
    ESSENTIALS_GAMEMODE_OTHER,
    ESSENTIALS_GAMEMODE_CREATIVE,
    ESSENTIALS_GAMEMODE_SURVIVAL,
    ESSENTIALS_GAMEMODE_SPECTATOR,
    ESSENTIALS_GAMEMODE_ADVENTURE,

    DAY_USE,
    NIGHT_USE,
    SUN_USE, ENDERCHEST_USE, ENDERSEE_USE, TOP_USE, SPEED_USE, TPA_USE, TPA_ACCEPT_USE;


    public String asPermission() {
        return name().toLowerCase().replace("_", ".");
    }

}
