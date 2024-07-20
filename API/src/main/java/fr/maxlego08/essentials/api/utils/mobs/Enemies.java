package fr.maxlego08.essentials.api.utils.mobs;

public enum Enemies {
    FRIENDLY("friendly"),
    NEUTRAL("neutral"),
    ENEMY("enemy"),
    ADULT_ENEMY("adult_enemy");

    private final String type;

    Enemies(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}