package fr.maxlego08.essentials.zutils.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class AttributeUtils {

    private static final Map<String, Attribute> attributeCache = new HashMap<>();

    public static Attribute getAttribute(String name) {
        return attributeCache.computeIfAbsent(name, key -> {
            try {
                return Registry.ATTRIBUTE.getOrThrow(NamespacedKey.minecraft(key));
            } catch (NoSuchElementException ignored) {
                try {
                    return Registry.ATTRIBUTE.getOrThrow(NamespacedKey.minecraft("generic." + key));
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }
}
