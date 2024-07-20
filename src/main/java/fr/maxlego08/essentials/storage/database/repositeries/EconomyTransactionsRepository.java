package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.math.BigDecimal;
import java.util.UUID;

public class EconomyTransactionsRepository extends Repository {

    public EconomyTransactionsRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "economy_transactions");
    }

    public void upsert(UUID fromUuid, UUID toUuid, Economy economy, BigDecimal fromAmount, BigDecimal toAmount) {
        insert(table -> {
            table.uuid("from_unique_id", fromUuid);
            table.uuid("to_unique_id", toUuid);
            table.string("economy_name", economy.getName());
            table.decimal("amount", toAmount.subtract(fromAmount));
            table.decimal("from_amount", fromAmount);
            table.decimal("to_amount", toAmount);
        });
    }
}
