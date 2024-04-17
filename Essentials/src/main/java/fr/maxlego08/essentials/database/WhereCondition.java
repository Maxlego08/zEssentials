package fr.maxlego08.essentials.database;

public class WhereCondition {
    private final String column;
    private final Object value;
    private final String operator;
    private boolean isNotNull;

    public WhereCondition(String column, String operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    public WhereCondition(String column) {
        this.column = column;
        this.value = null;
        this.operator = null;
        this.isNotNull = true;
    }

    public WhereCondition(String column, Object value) {
        this(column, "=", value);
    }

    public String getCondition() {
        if (this.isNotNull) return this.column + " IS NOT NULL";
        return this.column + " " + this.operator + " ?";
    }

    public String getOperator() {
        return this.operator;
    }

    public Object getValue() {
        return this.value;
    }

    public String getColumn() {
        return this.column;
    }

    public boolean isNotNull() {
        return isNotNull;
    }
}

