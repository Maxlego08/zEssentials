package fr.maxlego08.essentials.api.storage;


import fr.maxlego08.essentials.api.EssentialsPlugin;

import java.io.File;
import java.lang.reflect.Type;

public class Persist {

    private EssentialsPlugin plugin;

    public Persist(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    // ------------------------------------------------------------ //
    // GET NAME - What should we call this type of object?
    // ------------------------------------------------------------ //

    public static String getName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase();
    }

    public static String getName(Object o) {
        return getName(o.getClass());
    }

    public static String getName(Type type) {
        return getName(type.getClass());
    }

    // ------------------------------------------------------------ //
    // GET FILE - In which file would we like to store this object?
    // ------------------------------------------------------------ //

    public File getFile(String name) {
        return new File(plugin.getDataFolder(), name + ".json");
    }

    public File getFile(Class<?> clazz) {
        return getFile(getName(clazz));
    }

    public File getFile(Object obj) {
        return getFile(getName(obj));
    }

    public File getFile(Type type) {
        return getFile(getName(type));
    }

    // NICE WRAPPERS

    public <T> T loadOrSaveDefault(T def, Class<T> clazz) {
        return loadOrSaveDefault(def, clazz, getFile(clazz));
    }

    public <T> T loadOrSaveDefault(T def, Class<T> clazz, String name) {
        return loadOrSaveDefault(def, clazz, getFile(name));
    }

    public <T> T loadOrSaveDefault(T def, Class<T> clazz, Folder folder, String name) {
        return loadOrSaveDefault(def, clazz, getFile(folder.toFolder() + File.separator + name));
    }

    public <T> T loadOrSaveDefault(T def, Class<T> clazz, File file) {
        if (!file.exists()) {
            plugin.getLogger().info("Creating default: " + file);
            this.save(def, file);
            return def;
        }

        T loaded = this.load(clazz, file);

        if (loaded == null) {
            plugin.getLogger().info("Using default as I failed to load: " + file);

            /*
             * Create new config backup
             */

            File backup = new File(file.getPath() + "_bad");
            if (backup.exists())
                backup.delete();
            plugin.getLogger().info("Backing up copy of bad file to: " + backup);

            file.renameTo(backup);

            return def;
        } else {

            plugin.getLogger().info(file.getAbsolutePath() + " loaded successfully !");

        }

        return loaded;
    }

    // SAVE

    public boolean save(Object instance) {
        return save(instance, getFile(instance));
    }

    public boolean save(Object instance, String name) {
        return save(instance, getFile(name));
    }

    public boolean save(Object instance, Folder folder, String name) {
        return save(instance, getFile(folder.toFolder() + File.separator + name));
    }

    public boolean save(Object instance, File file) {

        try {

            boolean result = DiscUtils.writeCatch(file, plugin.getGson().toJson(instance));
            plugin.getLogger().info(file.getAbsolutePath() + " successfully saved !");
            return result;

        } catch (Exception exception) {

            plugin.getLogger().info("cannot save file " + file.getAbsolutePath());
            exception.printStackTrace();
            return false;
        }
    }

    // LOAD BY CLASS

    public <T> T load(Class<T> clazz) {
        return load(clazz, getFile(clazz));
    }

    public <T> T load(Class<T> clazz, String name) {
        return load(clazz, getFile(name));
    }

    public <T> T load(Class<T> clazz, File file) {
        String content = DiscUtils.readCatch(file);
        if (content == null) {
            return null;
        }

        try {
            T instance = plugin.getGson().fromJson(content, clazz);
            return instance;
        } catch (Exception ex) { // output the error message rather than full
            // stack trace; error parsing the file, most
            // likely
            plugin.getLogger().info(ex.getMessage());
        }

        return null;
    }

    // LOAD BY TYPE
    @SuppressWarnings("unchecked")
    public <T> T load(Type typeOfT, String name) {
        return (T) load(typeOfT, getFile(name));
    }

    @SuppressWarnings("unchecked")
    public <T> T load(Type typeOfT, File file) {
        String content = DiscUtils.readCatch(file);
        if (content == null) {
            return null;
        }

        try {
            return (T) plugin.getGson().fromJson(content, typeOfT);
        } catch (Exception ex) { // output the error message rather than full
            // stack trace; error parsing the file, most
            // likely
            plugin.getLogger().info(ex.getMessage());
        }

        return null;
    }

}
