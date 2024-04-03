package fr.maxlego08.essentials.api.database;

public abstract class Migration {

    protected String prefix;

    public abstract void up();

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
