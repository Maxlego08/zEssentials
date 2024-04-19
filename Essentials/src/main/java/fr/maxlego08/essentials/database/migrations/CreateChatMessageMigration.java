package fr.maxlego08.essentials.database.migrations;

import fr.maxlego08.essentials.api.database.Migration;
import fr.maxlego08.essentials.database.SchemaBuilder;

public class CreateChatMessageMigration extends Migration {
    @Override
    public void up() {
        SchemaBuilder.create(this, "%prefix%chat_message", table -> {
            table.uuid("unique_id").foreignKey("%prefix%users");
            table.longText("content");
            table.timestamps();
        });
    }
}
