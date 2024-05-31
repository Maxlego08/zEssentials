package fr.maxlego08.essentials.api.sanction;

import fr.maxlego08.essentials.api.commands.Permission;

public enum SanctionType {

    KICK(Permission.ESSENTIALS_KICK),
    MUTE( Permission.ESSENTIALS_MUTE),
    BAN( Permission.ESSENTIALS_BAN),
    UNBAN( Permission.ESSENTIALS_UNBAN),
    UNMUTE( Permission.ESSENTIALS_UNMUTE),
    WARN( Permission.ESSENTIALS_WARN)

    ;

    private final Permission permission;

    SanctionType(Permission permission) {
        this.permission = permission;
    }

    public Permission getPermission() {
        return permission;
    }
}
