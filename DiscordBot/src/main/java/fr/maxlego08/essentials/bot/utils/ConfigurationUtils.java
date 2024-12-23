package fr.maxlego08.essentials.bot.utils;

import fr.maxlego08.essentials.api.configuration.NonLoadable;
import fr.maxlego08.essentials.api.modules.Loadable;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class ConfigurationUtils {

    public void loadConfiguration(Map<String, Object> configuration) {

        for (Field field : this.getClass().getDeclaredFields()) {

            if (field.isAnnotationPresent(NonLoadable.class)) continue;

            field.setAccessible(true);

            String configKey = field.getName().replaceAll("([A-Z])", "-$1").toLowerCase();

            try {
                if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                    field.setBoolean(this, Boolean.parseBoolean(configuration.get(configKey).toString()));
                } else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                    field.setInt(this, Integer.parseInt(configuration.get(configKey).toString()));
                } else if (field.getType().equals(long.class) || field.getType().equals(Long.class)) {
                    field.setLong(this, Long.parseLong(configuration.get(configKey).toString()));
                } else if (field.getType().equals(String.class)) {
                    field.set(this, configuration.get(configKey));
                } else if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
                    field.set(this, configuration.get(configKey));
                } else if (field.getType().equals(float.class) || field.getType().equals(Float.class)) {
                    field.set(this, configuration.get(configKey));
                } else if (field.getType().equals(BigDecimal.class)) {
                    field.set(this, new BigDecimal(configuration.getOrDefault(configKey, 0).toString()));
                } else if (field.getType().isEnum()) {
                    Class<? extends Enum> enumType = (Class<? extends Enum>) field.getType();
                    field.set(this, Enum.valueOf(enumType, configuration.getOrDefault(configKey, "").toString().toUpperCase()));
                } else if (field.getType().equals(List.class)) {

                    Type genericFieldType = field.getGenericType();
                    if (genericFieldType instanceof ParameterizedType type) {
                        Class<?> fieldArgClass = (Class<?>) type.getActualTypeArguments()[0];

                        if (Loadable.class.isAssignableFrom(fieldArgClass)) {
                            List<Map<?, ?>> list = (List<Map<?, ?>>) configuration.get(configKey);
                            field.set(this, loadObjects(fieldArgClass, list));
                            continue;
                        } else if (NonLoadable.class.isAssignableFrom(fieldArgClass)) {
                            continue;
                        }
                    }

                    field.set(this, configuration.get(configKey));
                } else {

                    Map<?, ?> map = (Map<?, ?>) configuration.get(configKey);
                    field.set(this, createInstanceFromMap(((Class<?>) field.getGenericType()).getConstructors()[0], map));
                }

            } catch (Exception exception) {
                System.err.println("An error with loading field " + field.getName() + ": " + exception.getMessage());
            }
        }
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
                Class<?> paramType = parameters[i].getType();
                String paramName = parameters[i].getName();
                Object value = map.get(paramName);

                if (value != null) {

                    try {
                        if (value instanceof Map<?, ?> newMap) {
                            value = createInstanceFromMap(paramType.getConstructors()[0], newMap);
                        } else if (paramType.isArray()) {
                            Class<?> componentType = paramType.getComponentType();
                            List<?> list = (List<?>) value;
                            Object array = Array.newInstance(componentType, list.size());
                            for (int j = 0; j < list.size(); j++) {
                                Object element = list.get(j);
                                element = convertToRequiredType(element, componentType);
                                Array.set(array, j, element);
                            }
                            value = array;
                        } else {
                            value = convertToRequiredType(value, paramType);
                        }
                    } catch (Exception exception) {
                        System.err.printf("Error converting value '%s' for parameter '%s' to type '%s'%n", value, paramName, paramType.getName());
                    }
                }

                arguments[i] = value;
            }
            return constructor.newInstance(arguments);
        } catch (Exception exception) {
            System.err.printf("Failed to create instance from map with constructor %s%n", constructor);
            System.err.printf("Constructor parameters: %s%n", (Object) constructor.getParameters());
            System.err.printf("Map content: %s%n", map);
            exception.printStackTrace();
        }
        return null;
    }

    private Object convertToRequiredType(Object value, Class<?> type) {
        if (value == null) {
            return null;
        } else if (type.isEnum()) {
            try {
                return Enum.valueOf((Class<Enum>) type, (String) value);
            } catch (IllegalArgumentException exception) {
                System.err.printf("Failed to convert '%s' to enum type '%s'%n", value, type.getName());
            }
        } else if (type == BigDecimal.class) {
            try {
                return new BigDecimal(value.toString());
            } catch (NumberFormatException exception) {
                System.err.printf("Failed to convert '%s' to BigDecimal%n", value);
            }
        } else if (type == UUID.class) {
            try {
                return UUID.fromString((String) value);
            } catch (IllegalArgumentException exception) {
                System.err.printf("Failed to convert '%s' to UUID%n", value);
            }
        } else if (type == Integer.class || type == int.class) {
            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException e) {
                System.err.printf("Failed to convert '%s' to Integer%n", value);
                throw e;
            }
        } else if (type == Double.class || type == double.class) {
            try {
                return Double.parseDouble(value.toString());
            } catch (NumberFormatException exception) {
                System.err.printf("Failed to convert '%s' to Double%n", value);
            }
        } else if (type == Long.class || type == long.class) {
            try {
                return Long.parseLong(value.toString());
            } catch (NumberFormatException exception) {
                System.err.printf("Failed to convert '%s' to Long%n", value);
            }
        } else if (type == Boolean.class || type == boolean.class) {
            try {
                return Boolean.parseBoolean(value.toString());
            } catch (Exception exception) {
                System.err.printf("Failed to convert '%s' to Boolean%n", value);
            }
        } else if (type == Float.class || type == float.class) {
            try {
                return Float.parseFloat(value.toString());
            } catch (NumberFormatException exception) {
                System.err.printf("Failed to convert '%s' to Float%n", value);
            }
        }
        return value;
    }

}
