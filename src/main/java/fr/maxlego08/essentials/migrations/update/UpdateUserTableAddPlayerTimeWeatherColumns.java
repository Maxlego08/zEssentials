package fr.maxlego08.essentials.migrations.update;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;

public class UpdateUserTableAddPlayerTimeWeatherColumns extends Migration {
    @Override
    public void up() {
        SchemaBuilder.alter(this, "%prefix%users", schema -> {
            schema.bigInt("player_time").defaultValue(0);
            schema.string("player_weather", 32).nullable();
        });
    }
}
