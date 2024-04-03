package fr.maxlego08.essentials.storage.requests;


import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlConnection {

    private final DatabaseConfiguration databaseConfiguration;
    private Connection connection;

    public MySqlConnection(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
    }

    public DatabaseConfiguration getDatabaseConfiguration() {
        return databaseConfiguration;
    }

    public boolean isValid() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            return false;
        }

        if (!isConnected(connection)) {
            try {
                Properties properties = new Properties();
                properties.setProperty("useSSL", "false");
                properties.setProperty("user", databaseConfiguration.user());
                properties.setProperty("password", databaseConfiguration.password());
                Connection temp_connection = DriverManager.getConnection("jdbc:mysql://" + databaseConfiguration.host() + ":" + databaseConfiguration.port() + "/" + databaseConfiguration.database(), properties);

                if (isConnected(temp_connection)) {
                    temp_connection.close();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
                return false;
            }
        }

        return true;
    }

    private boolean isConnected(Connection connection) {
        if (connection == null) {
            return false;
        }

        try {
            return connection.isValid(1);
        } catch (SQLException exception) {
            return false;
        }
    }

    public void disconnect() {
        if (isConnected(connection)) {
            try {
                connection.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void connect() {
        if (!isConnected(connection)) {
            try {
                Properties properties = new Properties();
                properties.setProperty("useSSL", "false");
                properties.setProperty("user", databaseConfiguration.user());
                properties.setProperty("password", databaseConfiguration.password());
                connection = DriverManager.getConnection("jdbc:mysql://" + databaseConfiguration.host() + ":" + databaseConfiguration.port() + "/" + databaseConfiguration.database(), properties);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        connect();
        return connection;
    }
}
