package fr.maxlego08.essentials.zutils.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AttributeUtils {

    // Thread-safe cache for attribute lookups
    private static final Map<String, Attribute> attributeCache = new ConcurrentHashMap<>();

    public static Attribute getAttribute(String name) {
        return attributeCache.computeIfAbsent(name, key -> {
            // Try with the key as-is first
            Attribute attribute = Registry.ATTRIBUTE.get(NamespacedKey.minecraft(key));
            if (attribute != null) {
                return attribute;
            }

            // Try with "generic." prefix and return result (null if not found)
            return Registry.ATTRIBUTE.get(NamespacedKey.minecraft("generic." + key));
        });
    }
}
