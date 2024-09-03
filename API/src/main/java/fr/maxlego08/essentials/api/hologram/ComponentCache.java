package fr.maxlego08.essentials.api.hologram;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code ComponentCache} class manages a cache of text components and provides
 * methods to manipulate and retrieve the components. It supports updating, merging,
 * and retrieving components.
 */
public class ComponentCache {

    private Component component;
    private List<Component> components = new ArrayList<>();

    /**
     * Updates the component at the specified index in the list of components.
     * If the index is out of bounds, the method does nothing.
     *
     * @param index     the index of the component to update
     * @param component the new component to set at the specified index
     */
    public void updateComponent(int index, Component component) {
        if (index >= this.components.size()) return;
        this.components.set(index, component);
        this.component = null;
    }

    /**
     * Returns the list of components currently stored in the cache.
     *
     * @return a list of components
     */
    public List<Component> getComponents() {
        return this.components;
    }

    /**
     * Sets the list of components in the cache, replacing the current list with a new one.
     *
     * @param components the new list of components to store in the cache
     */
    public void setComponents(List<Component> components) {
        this.components = new ArrayList<>(components);
    }

    /**
     * Merges the list of components into a single {@link Component} with each component
     * separated by a newline. If the components have already been merged, it returns
     * the cached result.
     *
     * @return the merged component
     */
    public Component merge() {

        if (this.component != null) return this.component;

        TextComponent.Builder builder = Component.text();

        for (int i = 0; i < components.size(); i++) {
            builder.append(components.get(i));
            if (i < components.size() - 1) {
                builder.append(Component.newline());
            }
        }

        return component = builder.build();
    }

    /**
     * Checks if the cache of components is empty.
     *
     * @return {@code true} if the list of components is empty, otherwise {@code false}
     */
    public boolean isEmpty() {
        return this.components.isEmpty();
    }
}
