package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.database.Migration;

public class UpdatePlayerSlots extends Migration {
    @Override
    public void up() {
        modify("%prefix%player_slots", table -> {
            table.uuid("unique_id").primary();
            table.bigInt("slots");
        });
    }
}
