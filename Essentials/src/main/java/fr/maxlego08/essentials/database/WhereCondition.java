package fr.maxlego08.essentials.database;

public class WhereCondition {
    private final String column;
    private final Object value;
    private final String operator;

    public WhereCondition(String column, String operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    public WhereCondition(String column, Object value) {
        this(column, "=", value);
    }

    public String getCondition() {
        return column + " " + operator + " ?";
    }

    public Object getValue() {
        return value;
    }

    public String getColumn() {
        return column;
    }
}

