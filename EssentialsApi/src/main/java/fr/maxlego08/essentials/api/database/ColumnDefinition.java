package fr.maxlego08.essentials.api.database;

public class ColumnDefinition {
    private String name;
    private String type;
    private int length;
    private int decimal;
    private boolean nullable = false;
    private String defaultValue;
    private boolean isPrimaryKey = false;
    private boolean is = false;
    private String referenceTable;
    private Object object;
    private boolean isAutoIncrement;

    public ColumnDefinition(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public ColumnDefinition(String name) {
        this.name = name;
    }

    public String build() {
        StringBuilder columnSQL = new StringBuilder(name + " " + type);

        if (length != 0 && decimal != 0) {
            columnSQL.append("(").append(length).append(",").append(decimal).append(")");
        } else if (length != 0) {
            columnSQL.append("(").append(length).append(")");
        }

        if (isAutoIncrement) {
            columnSQL.append(" AUTO_INCREMENT");
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

    public ColumnDefinition setLength(int length) {
        this.length = length;
        return this;
    }

    public ColumnDefinition setDecimal(Integer decimal) {
        this.decimal = decimal;
        return this;
    }

    public Boolean getNullable() {
        return nullable;
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

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isIs() {
        return is;
    }

    public void setIs(boolean is) {
        this.is = is;
    }

    public Object getObject() {
        return object;
    }

    public ColumnDefinition setObject(Object object) {
        this.object = object;
        return this;
    }

    public ColumnDefinition setAutoIncrement(boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
        return this;
    }
}
