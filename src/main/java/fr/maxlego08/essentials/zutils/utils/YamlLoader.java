package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.configuration.NonLoadable;
import fr.maxlego08.essentials.api.modules.Loadable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class YamlLoader extends ZUtils {

    protected void loadYamlConfirmation(EssentialsPlugin plugin, YamlConfiguration configuration) {
        for (Field field : this.getClass().getDeclaredFields()) {

            if (field.isAnnotationPresent(NonLoadable.class)) continue;

            field.setAccessible(true);

            try {

                String configKey = field.getName().replaceAll("([A-Z])", "-$1").toLowerCase();

                if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                    field.setBoolean(this, configuration.getBoolean(configKey));
                } else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                    field.setInt(this, configuration.getInt(configKey));
                } else if (field.getType().equals(long.class) || field.getType().equals(Long.class)) {
                    field.setLong(this, configuration.getLong(configKey));
                } else if (field.getType().equals(String.class)) {
                    field.set(this, configuration.getString(configKey));
                } else if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
                    field.set(this, configuration.getDouble(configKey));
                } else if (field.getType().equals(float.class) || field.getType().equals(Float.class)) {
                    field.set(this, (float) configuration.getDouble(configKey));
                } else if (field.getType().equals(BigDecimal.class)) {
                    field.set(this, new BigDecimal(configuration.getString(configKey, "0")));
                } else if (field.getType().isEnum()) {
                    Class<? extends Enum> enumType = (Class<? extends Enum>) field.getType();
                    field.set(this, Enum.valueOf(enumType, configuration.getString(configKey, "").toUpperCase()));
                } else if (field.getType().equals(List.class)) {

                    Type genericFieldType = field.getGenericType();
                    if (genericFieldType instanceof ParameterizedType type) {
                        Class<?> fieldArgClass = (Class<?>) type.getActualTypeArguments()[0];

                        if (Loadable.class.isAssignableFrom(fieldArgClass)) {
                            field.set(this, loadObjects(plugin.getLogger(), fieldArgClass, configuration.getMapList(configKey)));
                            continue;
                        } else if (NonLoadable.class.isAssignableFrom(fieldArgClass)) {
                            continue;
                        }
                    }

                    field.set(this, configuration.getStringList(configKey));
                } else if (field.getType().equals(Map.class) && isStringStringMap(field)) {
                    // Handle Map<String,String> fields with optimized loading
                    ConfigurationSection section = configuration.getConfigurationSection(configKey);
                    if (section != null) {
                        Map<String, String> map = section.getKeys(false).stream()
                            .collect(HashMap::new,
                                (m, key) -> {
                                    String value = section.getString(key);
                                    if (value != null) m.put(key, value);
                                },
                                HashMap::putAll);
                        field.set(this, map);
                    }
                    continue;
                } else {
                    ConfigurationSection configurationSection = configuration.getConfigurationSection(configKey);
                    if (configurationSection == null) continue;
                    Map<String, Object> map = new HashMap<>();
                    configurationSection.getKeys(false).forEach(key -> map.put(key, configurationSection.get(key)));
                    field.set(this, createInstanceFromMap(plugin.getLogger(), ((Class<?>) field.getGenericType()).getConstructors()[0], map));
                }
            } catch (Exception exception) {
                plugin.getLogger().severe("An error with loading field " + field.getName() + ": " + exception.getMessage());
            }
        }
    }

    private List<Object> loadObjects(Logger logger, Class<?> fieldArgClass, List<Map<?, ?>> maps) {
        Constructor<?> constructor = fieldArgClass.getConstructors()[0];
        return maps.stream().map(map -> createInstanceFromMap(logger, constructor, map)).collect(Collectors.toList());
    }

    private boolean isStringStringMap(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType paramType) {
            Type[] typeArgs = paramType.getActualTypeArguments();
            return typeArgs.length == 2 &&
                   typeArgs[0].equals(String.class) &&
                   typeArgs[1].equals(String.class);
        }
        return false;
    }
}
