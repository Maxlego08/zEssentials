package fr.maxlego08.essentials.module.modules.hologram;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramLine;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.hologram.configuration.BlockHologramConfiguration;
import fr.maxlego08.essentials.api.hologram.configuration.HologramConfiguration;
import fr.maxlego08.essentials.api.hologram.configuration.ItemHologramConfiguration;
import fr.maxlego08.essentials.api.hologram.configuration.TextHologramConfiguration;
import fr.maxlego08.essentials.api.utils.SafeLocation;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import fr.maxlego08.menu.api.exceptions.InventoryException;
import fr.maxlego08.menu.api.utils.Loader;
import fr.maxlego08.menu.zcore.utils.nms.ItemStackUtils;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.joml.Vector3f;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class HologramLoader extends ZUtils implements Loader<Hologram> {

    private final EssentialsPlugin plugin;

    public HologramLoader(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Hologram load(YamlConfiguration configuration, String path, Object... objects) throws InventoryException {

        HologramType hologramType = HologramType.valueOf(configuration.getString("type"));
        String name = configuration.getString("name");
        SafeLocation location = stringAsLocation(configuration.getString("location"));

        HologramConfiguration hologramConfiguration;
        hologramConfiguration = hologramType == HologramType.BLOCK ? new BlockHologramConfiguration() : hologramType == HologramType.ITEM ? new ItemHologramConfiguration() : new TextHologramConfiguration();

        HologramManager hologramManager = this.plugin.getHologramManager();
        Hologram hologram = hologramManager.createHologram(hologramType, hologramConfiguration, (String) objects[0], name, location);

        loadConfiguration(configuration, hologramConfiguration);

        switch (hologramType) {
            case TEXT -> {
                this.loadTextConfiguration(hologram, configuration, (TextHologramConfiguration) hologramConfiguration);
            }
            case BLOCK -> {
                this.loadBlockConfiguration(configuration, (BlockHologramConfiguration) hologramConfiguration);
            }
            case ITEM -> {
                this.loadItemConfiguration(configuration, (ItemHologramConfiguration) hologramConfiguration);
            }
        }

        return hologram;
    }

    private void loadConfiguration(YamlConfiguration configuration, HologramConfiguration hologramConfiguration) {
        hologramConfiguration.setTranslation(loadVector(configuration, "translation."));
        hologramConfiguration.setScale(loadVector(configuration, "scale."));
        hologramConfiguration.setBillboard(Display.Billboard.valueOf(configuration.getString("bill-board")));
        hologramConfiguration.setShadowRadius((float) configuration.getDouble("shadow-radius"));
        hologramConfiguration.setShadowStrength((float) configuration.getDouble("shadow-strength"));
        hologramConfiguration.setVisibilityDistance(configuration.getInt("visibility-distance"));

        ConfigurationSection configurationSection = configuration.getConfigurationSection("brightness");
        if (configurationSection != null) {
            hologramConfiguration.setBrightness(new Display.Brightness(configurationSection.getInt("block"), configurationSection.getInt("sky")));
        }
    }

    private void loadItemConfiguration(YamlConfiguration configuration, ItemHologramConfiguration itemHologramConfiguration) {
        itemHologramConfiguration.setItemStack(ItemStackUtils.deserializeItemStack(configuration.getString("itemstack", "null")));
    }

    private void loadBlockConfiguration(YamlConfiguration configuration, BlockHologramConfiguration blockHologramConfiguration) {
        blockHologramConfiguration.setMaterial(Material.valueOf(configuration.getString("block-material", "GRASS_BLOCK").toUpperCase()));
    }


    private void loadTextConfiguration(Hologram hologram, YamlConfiguration configuration, TextHologramConfiguration textHologramConfiguration) {

        textHologramConfiguration.setTextShadow(configuration.getBoolean("text-shadow"));
        textHologramConfiguration.setTextAlignment(TextDisplay.TextAlignment.valueOf(configuration.getString("text-alignment")));
        textHologramConfiguration.setSeeThrough(configuration.getBoolean("see-through"));
        textHologramConfiguration.setBackground(configureBackground(configuration));

        configuration.getMapList("lines").forEach(map -> {

            String text = (String) map.get("text");
            int line = ((Number) map.get("line")).intValue();
            String eventName = map.containsKey("event") ? (String) map.get("event") : null;
            boolean isAutoUpdate = map.containsKey("update") && (boolean) map.get("update");

            hologram.addLine(new HologramLine(line, text, eventName, isAutoUpdate));
        });
    }

    @Override
    public void save(Hologram hologram, YamlConfiguration configuration, String path, File file, Object... objects) {

        configuration.set("type", hologram.getHologramType().name());
        configuration.set("name", hologram.getName());
        configuration.set("location", locationAsString(hologram.getLocation()));

        HologramConfiguration hologramConfiguration = hologram.getConfiguration();

        configuration.set("bill-board", hologramConfiguration.getBillboard().name());
        configuration.set("shadow-radius", hologramConfiguration.getShadowRadius());
        configuration.set("shadow-strength", hologramConfiguration.getShadowStrength());
        configuration.set("visibility-distance", hologramConfiguration.getVisibilityDistance());

        Display.Brightness brightness = hologramConfiguration.getBrightness();
        if (brightness != null) {
            configuration.set("brightness.block", brightness.getBlockLight());
            configuration.set("brightness.sky", brightness.getSkyLight());
        }

        setVector(configuration, "scale.", hologramConfiguration.getScale());
        setVector(configuration, "translation.", hologramConfiguration.getTranslation());

        if (hologramConfiguration instanceof TextHologramConfiguration textHologramConfiguration) {

            configuration.set("text-alignment", textHologramConfiguration.getTextAlignment().name());
            configuration.set("text-shadow", textHologramConfiguration.isTextShadow());
            configuration.set("text-background", getColor(textHologramConfiguration));
            configuration.set("see-through", textHologramConfiguration.isSeeThrough());

            List<Map<String, Object>> lines = new ArrayList<>();

            hologram.getHologramLines().stream().sorted(Comparator.comparingInt(HologramLine::getLine)).forEach(hologramLine -> {
                Map<String, Object> map = new HashMap<>();

                map.put("line", hologramLine.getLine());
                map.put("text", hologramLine.getText());
                if (hologramLine.getEventName() != null) map.put("event", hologramLine.getEventName());

                lines.add(map);
            });

            configuration.set("lines", lines);
        } else if (hologramConfiguration instanceof BlockHologramConfiguration blockHologramConfiguration) {

            configuration.set("block-material", blockHologramConfiguration.getMaterial().name());
        } else if (hologramConfiguration instanceof ItemHologramConfiguration itemHologramConfiguration) {

            configuration.set("itemstack", ItemStackUtils.serializeItemStack(itemHologramConfiguration.getItemStack()));
        }
    }

    public void setVector(YamlConfiguration configuration, String path, Vector3f vector3f) {
        configuration.set(path + "x", vector3f.x());
        configuration.set(path + "y", vector3f.y());
        configuration.set(path + "z", vector3f.z());
    }

    public Vector3f loadVector(YamlConfiguration configuration, String path) {
        return new Vector3f((float) configuration.getDouble(path + "x"), (float) configuration.getDouble(path + "y"), (float) configuration.getDouble(path + "z"));
    }

    public String getColor(TextHologramConfiguration textHologramConfiguration) {
        TextColor background = textHologramConfiguration.getBackground();
        return Optional.ofNullable(background).map(bg -> bg == Hologram.TRANSPARENT ? "transparent" : bg instanceof NamedTextColor named ? named.toString() : bg.asHexString()).orElse("default");
    }

    public TextColor configureBackground(YamlConfiguration configuration) {
        String backgroundStr = configuration.getString("text-background", null);
        if (backgroundStr == null || backgroundStr.equalsIgnoreCase("default")) {
            return null;
        } else if (backgroundStr.equalsIgnoreCase("transparent")) {
            return Hologram.TRANSPARENT;
        } else if (backgroundStr.startsWith("#")) {
            return TextColor.fromHexString(backgroundStr);
        } else {
            String formattedName = backgroundStr.toLowerCase(Locale.ROOT).trim().replace(' ', '_');
            return NamedTextColor.NAMES.value(formattedName);
        }
    }
}
