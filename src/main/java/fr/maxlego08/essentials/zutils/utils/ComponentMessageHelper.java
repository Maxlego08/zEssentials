package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.utils.component.ComponentMessage;
import fr.maxlego08.essentials.zutils.utils.paper.PaperComponent;
import fr.maxlego08.essentials.zutils.utils.spigot.SpigotComponent;

public class ComponentMessageHelper {

    public static ComponentMessage componentMessage;

    static {
        try {
            Class.forName("net.kyori.adventure.text.minimessage.MiniMessage");
            componentMessage = new PaperComponent();
        } catch (Exception ignored) {
            componentMessage = new SpigotComponent();
        }
    }

}
