package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.modules.Loadable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class YamlLoader extends ZUtils {

    protected void loadYamlConfirmation(YamlConfiguration configuration) {
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
                } else if (field.getType().equals(double.class)) {
                    field.set(this, configuration.getDouble(configKey));
                } else if (field.getType().equals(BigDecimal.class)) {
                    field.set(this, new BigDecimal(configuration.getString(configKey, "0")));
                } else if (field.getType().isEnum()) {
                    Class<? extends Enum> enumType = (Class<? extends Enum>) field.getType();
                    field.set(this, Enum.valueOf(enumType, configuration.getString(configKey).toUpperCase()));
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
                } else {
                    ConfigurationSection configurationSection = configuration.getConfigurationSection(configKey);
                    if (configurationSection == null) continue;
                    Map<String, Object> map = new HashMap<>();
                    configurationSection.getKeys(false).forEach(key -> map.put(key, configurationSection.get(key)));
                    field.set(this, createInstanceFromMap(((Class<?>) field.getGenericType()).getConstructors()[0], map));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private List<Object> loadObjects(Class<?> fieldArgClass, List<Map<?, ?>> maps) {
        Constructor<?> constructor = fieldArgClass.getConstructors()[0];
        return maps.stream().map(map -> createInstanceFromMap(constructor, map)).collect(Collectors.toList());
    }
}
