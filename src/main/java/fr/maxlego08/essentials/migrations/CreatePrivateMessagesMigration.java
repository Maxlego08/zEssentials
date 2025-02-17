package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.database.Migration;

public class CreatePrivateMessagesMigration extends Migration {
    @Override
    public void up() {
        create("%prefix%private_messages", table -> {
            table.uuid("sender_unique_id").foreignKey("%prefix%users");
            table.uuid("receiver_unique_id").foreignKey("%prefix%users");
            table.longText("content");
            table.timestamps();
        });
    }
}
