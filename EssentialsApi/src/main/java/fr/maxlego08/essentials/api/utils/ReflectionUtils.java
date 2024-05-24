package fr.maxlego08.essentials.api.utils;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static Object getStaticValue(Class<?> clazz, String name) {
        Object result = null;

        try {
            Field field = clazz.getDeclaredField(name);

            field.setAccessible(true);
            result = field.get(clazz);
            field.setAccessible(false);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return result;
    }

}

