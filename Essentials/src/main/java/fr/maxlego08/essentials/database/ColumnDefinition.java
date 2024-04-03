package fr.maxlego08.essentials.database;

public class ColumnDefinition {
    private String name;
    private String type;
    private int length;
    private boolean nullable = false;
    private String defaultValue;
    private boolean isPrimaryKey = false;
    private boolean is = false;
    private String referenceTable;

    public ColumnDefinition(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String build() {
        StringBuilder columnSQL = new StringBuilder(name + " " + type);

        if (length != 0) {
            columnSQL.append("(").append(length).append(")");
        }

        if (nullable) {
            columnSQL.append(" NULL");
        } else {
            columnSQL.append(" NOT NULL");
        }

        if (defaultValue != null) {
            columnSQL.append(" DEFAULT ").append(defaultValue);
        }

        return columnSQL.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLength() {
        return length;
    }

    public ColumnDefinition setLength(Integer length) {
        this.length = length;
        return this;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public String getReferenceTable() {
        return referenceTable;
    }

    public void setReferenceTable(String referenceTable) {
        this.referenceTable = referenceTable;
    }
}
