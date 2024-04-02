package fr.maxlego08.essentials.module;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.modules.Loadable;
import fr.maxlego08.essentials.api.modules.Module;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ZModule implements Module {

    protected final ZEssentialsPlugin plugin;
    protected final String name;
    protected boolean isEnable = false;

    public ZModule(ZEssentialsPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    @Override
    public void loadConfiguration() {

        File folfer = getFolder();
        if (!folfer.exists()) {
            folfer.mkdirs();
            this.plugin.saveResource("modules/" + name + "/config.yml", false);
        }

        YamlConfiguration configuration = getConfiguration();
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            String configKey = field.getName().replaceAll("([A-Z])", "-$1").toLowerCase();

            try {
                if (field.getType().equals(boolean.class)) {
                    field.setBoolean(this, configuration.getBoolean(configKey));
                } else if (field.getType().equals(int.class)) {
                    field.setInt(this, configuration.getInt(configKey));
                } else if (field.getType().equals(String.class)) {
                    field.set(this, configuration.getString(configKey));
                } else if (field.getType().equals(List.class)) {

                    Type genericFieldType = field.getGenericType();
                    if (genericFieldType instanceof ParameterizedType type) {
                        Class<?> fieldArgClass = (Class<?>) type.getActualTypeArguments()[0];

                        if (Loadable.class.isAssignableFrom(fieldArgClass)) {
                            field.set(this, loadObjects(fieldArgClass, configuration.getMapList(configKey)));
                            continue;
                        }
                    }

                    field.set(this, configuration.getStringList(configKey));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        this.isEnable = configuration.getBoolean("enable", true);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public File getFolder() {
        return new File(this.plugin.getDataFolder(), "modules/" + name);
    }

    @Override
    public YamlConfiguration getConfiguration() {
        return YamlConfiguration.loadConfiguration(new File(getFolder(), "config.yml"));
    }

    @Override
    public boolean isEnable() {
        return this.isEnable;
    }

    private List<Object> loadObjects(Class<?> fieldArgClass, List<Map<?, ?>> maps) {
        Constructor<?> constructor = fieldArgClass.getConstructors()[0];
        return maps.stream().map(map -> createInstanceFromMap(constructor, map)).collect(Collectors.toList());
    }

    private Object createInstanceFromMap(Constructor<?> constructor, Map<?, ?> map) {
        try {
            Object[] arguments = new Object[constructor.getParameterCount()];
            java.lang.reflect.Parameter[] parameters = constructor.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                String paramName = parameters[i].getName();
                Object value = map.get(paramName);
                arguments[i] = value;
            }
            return constructor.newInstance(arguments);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to create instance from map", exception);
        }
    }
}
