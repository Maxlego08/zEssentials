package fr.maxlego08.essentials.api.worldedit;

public enum WorldeditStatus {

    NOTHING,
    CALCULATE_PRICE, // Running
    WAITING_RESPONSE_PRICE,
    CHECK_INVENTORY_CONTENT, // Running
    NOT_ENOUGH_ITEMS,
    RUNNING, // Running
    FINISH,
    CANCELLED,
    ;

    public boolean isRunning() {
        return this == RUNNING || this == CALCULATE_PRICE || this == CHECK_INVENTORY_CONTENT;
    }
}
