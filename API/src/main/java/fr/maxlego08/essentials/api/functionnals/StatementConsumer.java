package fr.maxlego08.essentials.api.functionnals;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementConsumer {

    void accept(PreparedStatement statement) throws SQLException;

}
