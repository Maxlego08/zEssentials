package fr.maxlego08.essentials.migrations.update;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class UpdateUserHomeTableAddSocialColumns extends Migration {
    @Override
    public void up() {
        SchemaBuilder.alter(this, "%prefix%user_homes", schema -> {
            schema.bool("is_public").nullable();
            schema.string("category", 32).nullable();
            schema.bool("is_favorite").nullable();
        });
    }
}
