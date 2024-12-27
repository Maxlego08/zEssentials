package fr.maxlego08.essentials.api.discord;

public enum DiscordAction {

    CREATE_CODE, // When the player creates a code on Discord
    ASK_CODE, // When the player requests their code again
    LINK_ACCOUNT, // When the player links their account
    UNLINK_ACCOUNT, // When the player unlinks their account
    TRY_LINK_ACCOUNT, // When the player tries to link their account, but the code is incorrect

}
