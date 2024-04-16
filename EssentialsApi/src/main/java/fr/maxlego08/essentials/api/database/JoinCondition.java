package fr.maxlego08.essentials.api.database;

public class JoinCondition {
    private final String primaryTable;
    private final String primaryColumnAlias;
    private final String primaryColumn;
    private final String foreignTable;
    private final String foreignColumn;

    public JoinCondition(String primaryTable, String primaryTableAlias, String primaryColumn, String foreignTable, String foreignColumn) {
        this.primaryTable = primaryTable;
        this.primaryColumnAlias = primaryTableAlias;
        this.primaryColumn = primaryColumn;
        this.foreignTable = foreignTable;
        this.foreignColumn = foreignColumn;
    }

    public String getJoinClause() {
        return "LEFT JOIN " + this.primaryTable + " as " + this.primaryColumnAlias + " ON " + this.primaryColumnAlias + "." + this.primaryColumn + " = " + this.foreignTable + "." + this.foreignColumn;
    }
}
