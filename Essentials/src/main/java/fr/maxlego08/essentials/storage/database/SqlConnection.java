package fr.maxlego08.essentials.storage.database;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Represents a connection to a MySQL database.
 * This class handles establishing and managing the connection to the database.
 */
public class SqlConnection {

    private final EssentialsPlugin plugin;
    private final DatabaseConfiguration databaseConfiguration;
    private Connection connection;

    /**
     * Constructs a new SqlConnection instance with the specified EssentialsPlugin instance.
     *
     * @param plugin The EssentialsPlugin instance associated with this connection.
     */
    public SqlConnection(EssentialsPlugin plugin) {
        this.plugin = plugin;
        this.databaseConfiguration = plugin.getConfiguration().getDatabaseConfiguration();
    }

    /**
     * Gets the EssentialsPlugin instance associated with this connection.
     *
     * @return The EssentialsPlugin instance.
     */
    public EssentialsPlugin getPlugin() {
        return plugin;
    }

    /**
     * Gets the DatabaseConfiguration instance associated with this connection.
     *
     * @return The DatabaseConfiguration instance.
     */
    public DatabaseConfiguration getDatabaseConfiguration() {
        return databaseConfiguration;
    }

    /**
     * Checks if the connection to the database is valid.
     *
     * @return true if the connection is valid, false otherwise.
     */
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

    /**
     * Checks if the given database connection is connected and valid.
     *
     * @param connection The database connection to check.
     * @return true if the connection is valid, false otherwise.
     */
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

    /**
     * Disconnects from the database.
     */
    public void disconnect() {
        if (isConnected(connection)) {
            try {
                connection.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Establishes a connection to the database.
     */
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

    /**
     * Gets the connection to the database.
     * If the connection is not established, it attempts to connect first.
     *
     * @return The database connection.
     */
    public Connection getConnection() {
        connect();
        return connection;
    }
}
