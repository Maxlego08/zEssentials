package fr.maxlego08.essentials.api.hologram;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class ComponentCache {

    private Component component;
    private List<Component> components = new ArrayList<>();

    public void updateLine(int index, Component component) {
        if (this.components.size() >= index) return;
        this.components.set(index, component);
    }

    public List<Component> getComponents() {
        return this.components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

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

    public boolean isEmpty() {
        return this.components.isEmpty();
    }
}
