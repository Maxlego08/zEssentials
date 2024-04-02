package fr.maxlego08.essentials;

import fr.maxlego08.essentials.api.Configuration;
import fr.maxlego08.essentials.api.commands.CommandCooldown;
import fr.maxlego08.essentials.api.utils.CompactMaterial;
import fr.maxlego08.essentials.zutils.utils.YamlLoader;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permissible;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainConfiguration extends YamlLoader implements Configuration {

    private final ZEssentialsPlugin plugin;
    private final List<CommandCooldown> commandCooldowns = new ArrayList<>();
    private final List<CompactMaterial> compactMaterials = new ArrayList<>();
    private boolean enableDebug;
    private boolean enableCooldownBypass;
    private int trashSize;

    public MainConfiguration(ZEssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnableDebug() {
        return this.enableDebug;
    }

    @Override
    public boolean isEnableCooldownBypass() {
        return this.enableCooldownBypass;
    }

    @Override
    public List<CommandCooldown> getCommandCooldown() {
        return this.commandCooldowns;
    }

    @Override
    public Optional<Integer> getCooldown(Permissible permissible, String command) {
        return this.commandCooldowns.stream().filter(e -> e.command().equalsIgnoreCase(command)).map(commandCooldown -> commandCooldown.permissions().stream().filter(e -> permissible.hasPermission((String) e.get("permission"))).mapToInt(e -> ((Number) e.get("cooldown")).intValue()).min().orElse(commandCooldown.cooldown())).findFirst();
    }

    @Override
    public void load() {

        this.plugin.reloadConfig();

        YamlConfiguration configuration = (YamlConfiguration) this.plugin.getConfig();
        this.loadYamlConfirmation(configuration);
    }

    @Override
    public int getTrashSize() {
        return this.trashSize;
    }

    @Override
    public List<CompactMaterial> getCompactMaterials() {
        return this.compactMaterials;
    }
}
