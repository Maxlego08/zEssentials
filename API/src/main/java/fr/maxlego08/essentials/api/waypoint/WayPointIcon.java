package fr.maxlego08.essentials.api.waypoint;

import java.awt.*;

public record WayPointIcon(String texture, Color color) {

    public static WayPointIcon of(Color color) {
        return new WayPointIcon(null, color);
    }

    public static WayPointIcon of(String texture) {
        return new WayPointIcon(texture, Color.WHITE);
    }

    public static WayPointIcon of(String texture, Color color) {
        return new WayPointIcon(texture, color);
    }

}
