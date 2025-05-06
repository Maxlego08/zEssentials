package fr.maxlego08.essentials.migrations.create;

import fr.maxlego08.sarah.database.Migration;

public class CreatePlayerSlots extends Migration {
    @Override
    public void up() {
        create("%prefix%player_slots", table -> {
            table.uuid("unique_id").primary();
            table.bigInt("slots").primary();
        });
    }
}
