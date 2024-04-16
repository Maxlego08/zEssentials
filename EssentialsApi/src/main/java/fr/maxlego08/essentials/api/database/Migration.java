package fr.maxlego08.essentials.api.database;

/**
 * Represents a database migration for creating or modifying tables.
 */
public abstract class Migration {

    /**
     * The prefix used for table names.
     */
    protected String prefix;

    /**
     * Performs the migration to create or modify tables.
     */
    public abstract void up();

    /**
     * Sets the prefix used for table names.
     *
     * @param prefix The prefix to set.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}

