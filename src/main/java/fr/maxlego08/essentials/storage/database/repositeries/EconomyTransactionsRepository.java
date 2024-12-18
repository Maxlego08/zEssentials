package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.EconomyTransactionDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EconomyTransactionsRepository extends Repository {

    public EconomyTransactionsRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "economy_transactions");
    }

    public void upsert(UUID fromUuid, UUID toUuid, Economy economy, BigDecimal fromAmount, BigDecimal toAmount, String reason) {
        insert(table -> {
            table.uuid("from_unique_id", fromUuid);
            table.uuid("to_unique_id", toUuid);
            table.string("economy_name", economy.getName());
            table.string("reason", reason);
            table.decimal("amount", toAmount.subtract(fromAmount));
            table.decimal("from_amount", fromAmount);
            table.decimal("to_amount", toAmount);
        });
    }

    public List<EconomyTransactionDTO> selectTransactions(UUID toUuid, Economy economy) {
        return this.select(EconomyTransactionDTO.class, table -> table.where("to_unique_id", toUuid).where("economy_name", economy.getName())).stream().map(transactionDTO -> {
            return new EconomyTransactionDTO(transactionDTO.from_unique_id(), transactionDTO.to_unique_id(), transactionDTO.economy_name(), transactionDTO.reason() == null ? "No reason" : transactionDTO.reason(), transactionDTO.amount(), transactionDTO.from_amount(), transactionDTO.to_amount(), transactionDTO.created_at(), transactionDTO.updated_at());
        }).collect(Collectors.toList());
    }
}
