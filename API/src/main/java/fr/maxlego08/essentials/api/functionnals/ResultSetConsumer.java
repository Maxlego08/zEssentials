package fr.maxlego08.essentials.api.functionnals;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetConsumer {

    void accept(ResultSet statement) throws SQLException;

}
