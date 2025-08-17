package fr.maxlego08.essentials.api.functionnals;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetConsumer {

    /**
     * Applies this function to the given {@link ResultSet}.
     *
     * @param statement the statement to be processed
     * @throws SQLException if a database access error occurs
     */
    void accept(ResultSet statement) throws SQLException;

}
