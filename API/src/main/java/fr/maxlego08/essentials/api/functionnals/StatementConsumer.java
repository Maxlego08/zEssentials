package fr.maxlego08.essentials.api.functionnals;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementConsumer {

    /**
     * Accepts a prepared statement and applies some operations to it.
     *
     * @param statement the prepared statement to be modified
     * @throws SQLException if an error occurs while applying the operations
     */
    void accept(PreparedStatement statement) throws SQLException;

}
