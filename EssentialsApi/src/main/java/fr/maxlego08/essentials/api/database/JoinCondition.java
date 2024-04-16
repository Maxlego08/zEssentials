package fr.maxlego08.essentials.api.database;

public class JoinCondition {
    private final String primaryTable;
    private final String foreignTable;
    private final String primaryColumn;
    private final String foreignColumn;

    public JoinCondition(String primaryTable, String foreignTable, String primaryColumn, String foreignColumn) {
        this.primaryTable = primaryTable;
        this.foreignTable = foreignTable;
        this.primaryColumn = primaryColumn;
        this.foreignColumn = foreignColumn;
    }

    public String getJoinClause() {
        return "LEFT JOIN " + this.foreignTable + " ON " + this.primaryTable + "." + this.primaryColumn + " = " + this.foreignTable + "." + this.foreignColumn;
    }
}
