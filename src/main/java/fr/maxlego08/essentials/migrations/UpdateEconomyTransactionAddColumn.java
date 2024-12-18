package fr.maxlego08.essentials.migrations;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class UpdateEconomyTransactionAddColumn extends Migration {
    @Override
    public void up() {
        SchemaBuilder.alter(this, "%prefix%economy_transactions", schema -> schema.longText("reason").nullable());
    }
}
