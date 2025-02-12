package fr.maxlego08.essentials.zutils.utils.cube;

import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CubeDisplay {
    private final List<TextDisplay> faces = new ArrayList<>();
    private final Location center;
    private final double width;
    private final double height;
    private final double depth;
    private final Color backgroundColor;

    public CubeDisplay(Location center, double width, double height, double depth, Color backgroundColor) {
        this.center = center;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.backgroundColor = backgroundColor;
    }

    public void spawn() {
        World world = center.getWorld();
        if (world == null) return;

        int div = 8;

        // Création des 6 faces avec leur rotation correcte
        /*faces.add(createFace(world, center.clone().add(0, 0, -depth / div), width, height, 180, 0));   // Avant
        faces.add(createFace(world, center.clone().add(0, 0, depth / div), width, height, 0, 0)); // Arrière
        faces.add(createFace(world, center.clone().add(-width / div, 0, 0), depth, height, 90, 0));  // Gauche
        faces.add(createFace(world, center.clone().add(width / div, 0, 0), depth, height, -90, 0)); // Droite*/

        faces.add(createFace(world, center.clone().add(0, 0, -(depth / div)), width, height, 180, 0));   // Avant
        faces.add(createFace(world, center.clone().add(0, 0, depth / div), width, height, 0, 0)); // Arrière
        faces.add(createFace(world, center.clone().add(-(width / div), 0, 0), depth, height, 90, 0));  // Gauche
        faces.add(createFace(world, center.clone().add(width / div, 0, 0), depth, height, -90, 0)); // Droite

        faces.add(createFace(world, center.clone().add(0, height / div, 0), width, depth, 0, -90)); // Haut
        faces.add(createFace(world, center.clone().add(0, -height / div, 0), width, depth, 0, 90)); // Bas
    }

    private TextDisplay createFace(World world, Location location, double faceWidth, double faceHeight, float yaw, float pitch) {
        location.setYaw(yaw);
        location.setPitch(pitch);

        TextDisplay display = (TextDisplay) world.spawnEntity(location, EntityType.TEXT_DISPLAY);
        display.text(Component.text(" ")); // Texte vide
        display.setPersistent(false);
        display.setBackgroundColor(backgroundColor);
        display.setBillboard(Display.Billboard.FIXED); // Fixe l'orientation de l'entité

        // Appliquer la transformation avec scale correct
        Transformation transformation = display.getTransformation();
        transformation.getScale().set((float) faceWidth * 2, (float) faceHeight, 1);
        transformation.getTranslation().set(getTranslationForFace(yaw, pitch, faceWidth, faceHeight, depth));
        display.setTransformation(transformation);

        return display;
    }

    private Vector3f getTranslationForFace(float yaw, float pitch, double faceWidth, double faceHeight, double depth) {

        return new Vector3f((float) (faceHeight * -0.025f), (float) (faceHeight * -0.125f), 0);

        /*-if (yaw == 0 && pitch == -90) { // Haut
        } else if (yaw == 0 && pitch == 90) { // Bas
            return new Vector3f((float) (faceHeight * -0.025f), (float) (faceHeight * -0.125f), 0);
        } else if (yaw == 0) { // Arrière
            return new Vector3f(0, (float) (faceHeight * -0.125f), (float) (depth * -0.025f));
        } else if (yaw == 180) { // Avant
            return new Vector3f(0, (float) (faceHeight * -0.125f), (float) (depth * 0.025f));
        } else if (yaw == 90) { // Gauche
            return new Vector3f((float) (depth * -0.025f), (float) (faceHeight * -0.125f), 0);
        } else if (yaw == -90) { // Droite
            return new Vector3f((float) (depth * 0.025f), (float) (faceHeight * -0.125f), 0);
        }
        return new Vector3f(0, (float) (faceHeight * -0.125f), 0); // Valeur par défaut*/
    }

    public void remove() {
        for (TextDisplay face : faces) {
            if (face != null) {
                face.remove();
            }
        }
        faces.clear();
    }

    @Nullable
    public List<TextDisplay> getFaces() {
        return faces.isEmpty() ? null : faces;
    }
}