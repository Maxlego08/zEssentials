package fr.maxlego08.essentials.database;

import fr.maxlego08.essentials.api.database.JoinCondition;
import fr.maxlego08.essentials.api.database.Migration;
import fr.maxlego08.essentials.api.database.Schema;
import fr.maxlego08.essentials.api.database.SchemaType;
import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Location;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class SchemaBuilder extends ZUtils implements Schema {
    private final String tableName;
    private final SchemaType schemaType;
    private final List<ColumnDefinition> columns = new ArrayList<>();
    private final List<String> primaryKeys = new ArrayList<>();
    private final List<String> foreignKeys = new ArrayList<>();
    private final List<WhereCondition> whereConditions = new ArrayList<>();
    private final List<JoinCondition> joinConditions = new ArrayList<>();
    private Migration migration;

    private SchemaBuilder(String tableName, SchemaType schemaType) {
        this.tableName = tableName;
        this.schemaType = schemaType;
    }

    public static Schema create(Migration migration, String tableName, Consumer<Schema> consumer) {
        SchemaBuilder schema = new SchemaBuilder(tableName, SchemaType.CREATE);
        if (migration != null) {
            schema.migration = migration;
            ZMigrationManager.registerSchema(schema);
        }
        consumer.accept(schema);
        return schema;
    }

    public static Schema upsert(String tableName, Consumer<Schema> consumer) {
        Schema schema = new SchemaBuilder(tableName, SchemaType.UPSERT);
        consumer.accept(schema);
        return schema;
    }

    public static Schema alter(Migration migration, String tableName, Consumer<Schema> consumer) {
        SchemaBuilder schema = new SchemaBuilder(tableName, SchemaType.ALTER);
        if (migration != null) {
            schema.migration = migration;
            ZMigrationManager.registerSchema(schema);
        }
        consumer.accept(schema);
        return schema;
    }

    public static Schema insert(String tableName, Consumer<Schema> consumer) {
        Schema schema = new SchemaBuilder(tableName, SchemaType.INSERT);
        consumer.accept(schema);
        return schema;
    }

    public static Schema update(String tableName, Consumer<Schema> consumer) {
        Schema schema = new SchemaBuilder(tableName, SchemaType.UPDATE);
        consumer.accept(schema);
        return schema;
    }

    public static Schema select(String tableName) {
        return new SchemaBuilder(tableName, SchemaType.SELECT);
    }

    public static Schema selectCount(String tableName) {
        return new SchemaBuilder(tableName, SchemaType.SELECT);
    }

    public static Schema delete(String tableName) {
        return new SchemaBuilder(tableName, SchemaType.DELETE);
    }

    @Override
    public Schema where(String columnName, Object value) {
        this.whereConditions.add(new WhereCondition(columnName, value));
        return this;
    }

    @Override
    public Schema where(String columnName, UUID value) {
        return this.where(columnName, value.toString());
    }

    @Override
    public Schema where(String columnName, String operator, Object value) {
        this.whereConditions.add(new WhereCondition(columnName, operator, value));
        return this;
    }

    @Override
    public Schema whereNotNull(String columnName) {
        this.whereConditions.add(new WhereCondition(columnName));
        return this;
    }

    @Override
    public Schema uuid(String columnName) {
        this.string(columnName, 36);
        return this;
    }

    @Override
    public Schema uuid(String columnName, UUID value) {
        return this.addColumn(new ColumnDefinition(columnName).setObject(value.toString()));
    }

    @Override
    public Schema string(String columnName, int length) {
        return addColumn(new ColumnDefinition(columnName, "VARCHAR").setLength(length));
    }

    @Override
    public Schema text(String columnName) {
        return addColumn(new ColumnDefinition(columnName, "TEXT"));
    }

    @Override
    public Schema longText(String columnName) {
        return addColumn(new ColumnDefinition(columnName, "LONGTEXT"));
    }

    @Override
    public Schema decimal(String columnName) {
        return this.decimal(columnName, 65, 30);
    }

    @Override
    public Schema decimal(String columnName, int length, int decimal) {
        return addColumn(new ColumnDefinition(columnName, "DECIMAL").setLength(length).setDecimal(decimal));
    }

    @Override
    public Schema string(String columnName, String value) {
        return this.addColumn(new ColumnDefinition(columnName).setObject(value));
    }

    @Override
    public Schema location(String columnName, Location location) {
        return this.addColumn(new ColumnDefinition(columnName).setObject(locationAsString(location)));
    }

    @Override
    public Schema decimal(String columnName, Number value) {
        return this.addColumn(new ColumnDefinition(columnName).setObject(value));
    }

    @Override
    public Schema date(String columnName, Date value) {
        // return this.addColumn(new ColumnDefinition(columnName).setObject(new java.sql.Date(value.getTime())));
        return this.addColumn(new ColumnDefinition(columnName).setObject(value));
    }

    @Override
    public Schema bigInt(String columnName) {
        return addColumn(new ColumnDefinition(columnName, "BIGINT"));
    }

    @Override
    public Schema integer(String columnName) {
        return addColumn(new ColumnDefinition(columnName, "INT"));
    }

    @Override
    public Schema bigInt(String columnName, long value) {
        return this.addColumn(new ColumnDefinition(columnName).setObject(value));
    }

    @Override
    public Schema bool(String columnName) {
        return addColumn(new ColumnDefinition(columnName, "BOOLEAN"));
    }

    @Override
    public Schema bool(String columnName, boolean value) {
        return this.addColumn(new ColumnDefinition(columnName).setObject(value));
    }

    @Override
    public Schema foreignKey(String referenceTable) {
        if (this.columns.isEmpty()) throw new IllegalStateException("No column defined to apply foreign key.");
        ColumnDefinition lastColumn = this.columns.get(this.columns.size() - 1);

        String fkDefinition = String.format("FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE CASCADE", lastColumn.getName(), referenceTable, lastColumn.getName());
        this.foreignKeys.add(fkDefinition);
        return this;
    }

    @Override
    public Schema foreignKey(String referenceTable, String columnName, boolean onCascade) {
        if (this.columns.isEmpty()) throw new IllegalStateException("No column defined to apply foreign key.");
        ColumnDefinition lastColumn = this.columns.get(this.columns.size() - 1);

        String fkDefinition = String.format("FOREIGN KEY (%s) REFERENCES %s(%s)%s", lastColumn.getName(), referenceTable, columnName, onCascade ? " ON DELETE CASCADE" : "");
        this.foreignKeys.add(fkDefinition);
        return this;
    }

    @Override
    public Schema createdAt() {
        ColumnDefinition column = new ColumnDefinition("created_at", "TIMESTAMP");
        column.setDefaultValue("CURRENT_TIMESTAMP");
        this.columns.add(column);
        return this;
    }

    @Override
    public Schema timestamp(String columnName) {
        return this.addColumn(new ColumnDefinition(columnName, "TIMESTAMP"));
    }

    @Override
    public Schema autoIncrement(String columnName) {
        return addColumn(new ColumnDefinition(columnName, "INT").setAutoIncrement(true));
    }

    @Override
    public Schema updatedAt() {
        ColumnDefinition column = new ColumnDefinition("updated_at", "TIMESTAMP");
        column.setDefaultValue("CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");
        this.columns.add(column);
        return this;
    }

    @Override
    public Schema nullable() {
        getLastColumn().setNullable(true);
        return this;
    }

    @Override
    public Schema defaultValue(String value) {
        getLastColumn().setDefaultValue(value);
        return this;
    }

    @Override
    public Schema primary() {
        ColumnDefinition lastColumn = getLastColumn();
        lastColumn.setPrimaryKey(true);
        primaryKeys.add(lastColumn.getName());
        return this;
    }

    private Schema addColumn(ColumnDefinition column) {
        columns.add(column);
        return this;
    }

    @Override
    public Schema timestamps() {
        this.createdAt();
        this.updatedAt();
        return this;
    }

    @Override
    public int execute(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) throws SQLException {
        switch (this.schemaType) {
            case CREATE -> this.executeCreate(connection, databaseConfiguration, logger);
            case ALTER -> this.executeAlter(connection, databaseConfiguration, logger);
            case UPSERT -> this.executeUpsert(connection, databaseConfiguration, logger);
            case UPDATE -> this.executeUpdate(connection, databaseConfiguration, logger);
            case INSERT -> {
                return this.executeInsert(connection, databaseConfiguration, logger);
            }
            case DELETE -> this.executeDelete(connection, databaseConfiguration, logger);
            case SELECT, SELECT_COUNT -> throw new IllegalArgumentException("Wrong method !");
            default -> throw new Error("Schema type not found !");
        }
        return -1;
    }

    private void executeUpsert(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) throws SQLException {

        StringBuilder insertQuery = new StringBuilder("INSERT INTO " + this.tableName + " (");
        StringBuilder valuesQuery = new StringBuilder("VALUES (");
        StringBuilder onUpdateQuery = new StringBuilder(" ON DUPLICATE KEY UPDATE ");

        List<Object> values = new ArrayList<>();

        for (int i = 0; i < this.columns.size(); i++) {
            ColumnDefinition columnDefinition = this.columns.get(i);
            insertQuery.append(i > 0 ? ", " : "").append(columnDefinition.getName());
            valuesQuery.append(i > 0 ? ", " : "").append("?");
            onUpdateQuery.append(i > 0 ? ", " : "").append(columnDefinition.getName()).append(" = VALUES(").append(columnDefinition.getName()).append(")");
            values.add(columnDefinition.getObject());
        }

        insertQuery.append(") ");
        valuesQuery.append(")");
        String upsertQuery = databaseConfiguration.replacePrefix(insertQuery + valuesQuery.toString() + onUpdateQuery);

        if (databaseConfiguration.debug()) {
            logger.info("Executing SQL: " + upsertQuery);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(upsertQuery)) {
            for (int i = 0; i < values.size(); i++) {
                preparedStatement.setObject(i + 1, values.get(i));
            }
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new SQLException("Failed to execute upsert: " + exception.getMessage(), exception);
        }

    }

    private int executeInsert(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) throws SQLException {
        StringBuilder insertQuery = new StringBuilder("INSERT INTO " + this.tableName + " (");
        StringBuilder valuesQuery = new StringBuilder("VALUES (");

        List<Object> values = new ArrayList<>();

        for (int i = 0; i < this.columns.size(); i++) {
            ColumnDefinition columnDefinition = this.columns.get(i);
            insertQuery.append(i > 0 ? ", " : "").append(columnDefinition.getName());
            valuesQuery.append(i > 0 ? ", " : "").append("?");
            values.add(columnDefinition.getObject());
        }

        insertQuery.append(") ");
        valuesQuery.append(")");
        String upsertQuery = databaseConfiguration.replacePrefix(insertQuery + valuesQuery.toString());

        if (databaseConfiguration.debug()) {
            logger.info("Executing SQL: " + upsertQuery);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(upsertQuery, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < values.size(); i++) {
                preparedStatement.setObject(i + 1, values.get(i));
            }
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);  // Retourne l'index généré (généralement l'ID)
                } else {
                    return -1;
                }
            } catch (Exception exception) {
                return -1;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new SQLException("Failed to execute upsert: " + exception.getMessage(), exception);
        }
    }

    private void executeUpdate(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) throws SQLException {
        StringBuilder updateQuery = new StringBuilder("UPDATE " + this.tableName);

        if (!this.joinConditions.isEmpty()) {
            for (JoinCondition join : this.joinConditions) {
                updateQuery.append(" ").append(join.getJoinClause());
            }
        }

        updateQuery.append(" SET ");

        List<Object> values = new ArrayList<>();

        for (int i = 0; i < this.columns.size(); i++) {
            ColumnDefinition columnDefinition = this.columns.get(i);
            updateQuery.append(i > 0 ? ", " : "").append(columnDefinition.getName()).append(" = ?");
            values.add(columnDefinition.getObject());
        }

        this.whereConditions(updateQuery);
        String upsertQuery = databaseConfiguration.replacePrefix(updateQuery.toString());

        if (databaseConfiguration.debug()) {
            logger.info("Executing SQL: " + upsertQuery);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(upsertQuery)) {
            for (int i = 0; i < values.size(); i++) {
                preparedStatement.setObject(i + 1, values.get(i));
            }
            applyWhereConditions(preparedStatement, values.size() + 1);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new SQLException("Failed to execute upsert: " + exception.getMessage(), exception);
        }
    }


    private void executeAlter(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) throws SQLException {

        StringBuilder alterTableSQL = new StringBuilder("ALTER TABLE ");
        alterTableSQL.append(this.tableName).append(" ");

        List<String> columnSQLs = new ArrayList<>();
        for (ColumnDefinition column : this.columns) {
            columnSQLs.add("ADD COLUMN " + column.build());
        }
        alterTableSQL.append(String.join(", ", columnSQLs));

        if (!this.primaryKeys.isEmpty()) {
            alterTableSQL.append(", PRIMARY KEY (").append(String.join(", ", this.primaryKeys)).append(")");
        }

        for (String fk : this.foreignKeys) {
            alterTableSQL.append(", ADD ").append(fk);
        }

        String finalQuery = databaseConfiguration.replacePrefix(alterTableSQL.toString());
        if (databaseConfiguration.debug()) {
            logger.info("Executing SQL: " + finalQuery);
        }

        try (PreparedStatement statement = connection.prepareStatement(finalQuery)) {
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new SQLException("Failed to execute schema creation: " + exception.getMessage(), exception);
        }
    }

    private void executeCreate(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) throws SQLException {
        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        createTableSQL.append(this.tableName).append(" (");

        List<String> columnSQLs = new ArrayList<>();
        for (ColumnDefinition column : this.columns) {
            columnSQLs.add(column.build());
        }
        createTableSQL.append(String.join(", ", columnSQLs));

        if (!this.primaryKeys.isEmpty()) {
            createTableSQL.append(", PRIMARY KEY (").append(String.join(", ", this.primaryKeys)).append(")");
        }

        for (String fk : this.foreignKeys) {
            createTableSQL.append(", ").append(fk);
        }

        createTableSQL.append(")");

        String finalQuery = databaseConfiguration.replacePrefix(createTableSQL.toString());
        if (databaseConfiguration.debug()) {
            logger.info("Executing SQL: " + finalQuery);
        }

        try (PreparedStatement statement = connection.prepareStatement(finalQuery)) {
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new SQLException("Failed to execute schema creation: " + exception.getMessage(), exception);
        }
    }

    private ColumnDefinition getLastColumn() {
        if (columns.isEmpty()) throw new IllegalStateException("No columns defined.");
        return columns.get(columns.size() - 1);
    }

    private void whereConditions(StringBuilder sql) {
        if (!this.whereConditions.isEmpty()) {
            List<String> conditions = new ArrayList<>();
            for (WhereCondition condition : this.whereConditions) {
                conditions.add(condition.getCondition());
            }
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }
    }

    @Override
    public long executeSelectCount(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM " + tableName);
        this.whereConditions(sql);

        String finalQuery = databaseConfiguration.replacePrefix(sql.toString());
        if (databaseConfiguration.debug()) {
            logger.info("Executing SQL: " + finalQuery);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(finalQuery)) {

            applyWhereConditions(preparedStatement, 1);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new SQLException("Failed to execute schema select count: " + exception.getMessage(), exception);
        }
        return 0;
    }

    @Override
    public List<Map<String, Object>> executeSelect(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        StringBuilder selectQuery = new StringBuilder("SELECT * FROM " + tableName);

        if (!this.joinConditions.isEmpty()) {
            for (JoinCondition join : this.joinConditions) {
                selectQuery.append(" ").append(join.getJoinClause());
            }
        }

        this.whereConditions(selectQuery);

        String finalQuery = databaseConfiguration.replacePrefix(selectQuery.toString());
        if (databaseConfiguration.debug()) {
            logger.info("Executing SQL: " + finalQuery);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(finalQuery)) {
            applyWhereConditions(preparedStatement, 1);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        row.put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
                    }
                    results.add(row);
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new SQLException("Failed to execute schema select: " + exception.getMessage(), exception);
        }

        return results;
    }

    private void executeDelete(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) throws SQLException {
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(tableName);
        this.whereConditions(sql);

        String finalQuery = databaseConfiguration.replacePrefix(sql.toString());
        if (databaseConfiguration.debug()) {
            logger.info("Executing SQL: " + finalQuery);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(finalQuery)) {
            applyWhereConditions(preparedStatement, 1);
            preparedStatement.executeUpdate();
        }
    }

    private void applyWhereConditions(PreparedStatement preparedStatement, int index) throws SQLException {
        for (WhereCondition condition : this.whereConditions) {
            if (!condition.isNotNull()) {
                preparedStatement.setObject(index++, condition.getValue());
            }
        }
    }

    @Override
    public <T> List<T> executeSelect(Class<T> clazz, Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) throws Exception {
        List<Map<String, Object>> results = executeSelect(connection, databaseConfiguration, logger);
        return transformResults(results, clazz);
    }

    private <T> List<T> transformResults(List<Map<String, Object>> results, Class<T> clazz) throws Exception {
        List<T> transformedResults = new ArrayList<>();
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Constructor<?> firstConstructor = constructors[0];
        firstConstructor.setAccessible(true);

        for (Map<String, Object> row : results) {
            Object[] params = new Object[firstConstructor.getParameterCount()];
            Parameter[] parameters = firstConstructor.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                params[i] = convertToRequiredType(row.get(parameter.getName()), parameter.getType());
            }
            T instance = (T) firstConstructor.newInstance(params);
            transformedResults.add(instance);
        }
        return transformedResults;
    }

    @Override
    public Migration getMigration() {
        return migration;
    }

    @Override
    public Schema leftJoin(String primaryTable, String primaryTableAlias, String primaryColumn, String foreignTable, String foreignColumn) {
        joinConditions.add(new JoinCondition(primaryTable, primaryTableAlias, primaryColumn, foreignTable, foreignColumn));
        return this;
    }
}
