package fr.maxlego08.essentials.api.utils;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static Object getValue(Object instance, String name) {
        try {
            Field field = instance.getClass().getDeclaredField(name);
            field.trySetAccessible();
            return field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get value of field '" + name + "' from instance of " + instance.getClass().getName(), e);
        }
    }

    public static Object getStaticValue(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.trySetAccessible();
            return field.get(null);  // For static fields, pass 'null' as the instance
        } catch (Exception e) {
            throw new RuntimeException("Failed to get value of static field '" + name + "' from class " + clazz.getName(), e);
        }
    }
}

