package fr.maxlego08.essentials.api.worldedit;

public enum WorldeditStatus {

    NOTHING,
    CALCULATE_PRICE,
    WAITING_RESPONSE_PRICE,
    CHECK_INVENTORY_CONTENT,
    NOT_ENOUGH_ITEMS,
    RUNNING,
    FINISH,
    ;

    public boolean isRunning() {
        return this == RUNNING || this == CALCULATE_PRICE;
    }
}
