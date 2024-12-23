package fr.maxlego08.essentials.bot.utils;

public abstract class Arguments {

    protected String[] args;
    protected int parentCount = 0;

    protected String argAsString(int index) {
        try {
            return args[index + parentCount];
        } catch (Exception exception) {
            return null;
        }
    }

    protected String argAsString(int index, String defaultValue) {
        try {
            return args[index + parentCount];
        } catch (Exception e) {
            return defaultValue;
        }
    }

    protected int argAsInteger(int index) {
        return Integer.valueOf(argAsString(index));
    }

    protected int argAsInteger(int index, int defaultValue) {
        try {
            return Integer.valueOf(argAsString(index));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    protected long argAsLong(int index) {
        return Long.valueOf(argAsString(index));
    }

    protected long argAsLong(int index, long defaultValue) {
        try {
            return Long.valueOf(argAsString(index));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    protected double argAsDouble(int index, double defaultValue) {
        try {
            return Double.valueOf(argAsString(index).replace(",", "."));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    protected double argAsDouble(int index) {
        return Double.valueOf(argAsString(index).replace(",", "."));
    }

}
