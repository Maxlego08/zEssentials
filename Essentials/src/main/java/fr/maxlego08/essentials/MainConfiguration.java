package fr.maxlego08.essentials;

import fr.maxlego08.essentials.api.Configuration;
import fr.maxlego08.essentials.api.commands.CommandCooldown;
import fr.maxlego08.essentials.api.configuration.ReplacePlaceholder;
import fr.maxlego08.essentials.api.configuration.ReplacePlaceholderElement;
import fr.maxlego08.essentials.api.configuration.ReplacePlaceholderType;
import fr.maxlego08.essentials.api.configuration.placeholders.NumberPlaceholder;
import fr.maxlego08.essentials.api.configuration.placeholders.StringPlaceholder;
import fr.maxlego08.essentials.api.server.RedisConfiguration;
import fr.maxlego08.essentials.api.server.ServerType;
import fr.maxlego08.essentials.api.storage.StorageType;
import fr.maxlego08.essentials.api.utils.ChatCooldown;
import fr.maxlego08.essentials.api.utils.MessageColor;
import fr.maxlego08.essentials.api.utils.NearDistance;
import fr.maxlego08.essentials.api.utils.TransformMaterial;
import fr.maxlego08.essentials.zutils.utils.YamlLoader;
import fr.maxlego08.sarah.DatabaseConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permissible;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.LongStream;

public class MainConfiguration extends YamlLoader implements Configuration {

    private final ZEssentialsPlugin plugin;
    private final List<CommandCooldown> commandCooldowns = new ArrayList<>();
    private final List<TransformMaterial> compactMaterials = new ArrayList<>();
    private final List<TransformMaterial> smeltableMaterials = new ArrayList<>();
    private final StorageType storageType = StorageType.JSON;
    private final List<MessageColor> messageColors = new ArrayList<>();
    private final List<ChatCooldown> cooldowns = new ArrayList<>();
    private final List<NearDistance> nearPermissions = new ArrayList<>();
    private long[] cooldownCommands;
    private boolean enableDebug;
    private boolean enableCooldownBypass;
    private boolean enableCommandLog;
    private int trashSize;
    private String globalDateFormat;
    private DatabaseConfiguration databaseConfiguration;
    private ServerType serverType;
    private RedisConfiguration redisConfiguration;
    private double nearDistance;
    private SimpleDateFormat simpleDateFormat;
    private List<ReplacePlaceholder> replacePlaceholders = new ArrayList<>();

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

        this.cooldownCommands = this.cooldowns.stream().flatMapToLong(cooldown -> LongStream.of(cooldown.cooldown(), cooldown.messages())).toArray();
        this.simpleDateFormat = new SimpleDateFormat(this.globalDateFormat);
        loadReplacePlaceholders();
    }

    private void loadReplacePlaceholders() {

        YamlConfiguration configuration = (YamlConfiguration) this.plugin.getConfig();
        this.replacePlaceholders = new ArrayList<>();

        List<Map<?, ?>> placeholdersList = configuration.getMapList("replace-placeholders");

        for (Map<?, ?> placeholderMap : placeholdersList) {

            if (!placeholderMap.containsKey("placeholder") || !placeholderMap.containsKey("default")) {
                this.plugin.getLogger().severe("Your replace-placeholders is wrong ! Please check your configuration.");
                continue;
            }

            String placeholder = (String) placeholderMap.get("placeholder");
            String defaultValue = (String) placeholderMap.get("default");

            List<ReplacePlaceholderElement> elements = new ArrayList<>();
            List<Map<?, ?>> replacesList = (List<Map<?, ?>>) placeholderMap.get("replaces");

            for (Map<?, ?> replaceMap : replacesList) {

                if (!replaceMap.containsKey("type") || !replaceMap.containsKey("value")) {
                    this.plugin.getLogger().severe("Your replace-placeholders is wrong ! Please check your configuration.");
                    continue;
                }

                ReplacePlaceholderType type = ReplacePlaceholderType.valueOf((String) replaceMap.get("type"));
                String value = (String) replaceMap.get("value");

                if (type == ReplacePlaceholderType.NUMBER) {
                    int max = (int) replaceMap.get("max");
                    elements.add(new NumberPlaceholder(value, max));
                } else if (type == ReplacePlaceholderType.STRING) {
                    String string = String.valueOf(replaceMap.get("equalsTo"));
                    elements.add(new StringPlaceholder(value, string));
                }
            }

            this.replacePlaceholders.add(new ReplacePlaceholder(placeholder, defaultValue, elements));
        };
    }

    @Override
    public int getTrashSize() {
        return this.trashSize;
    }

    @Override
    public List<TransformMaterial> getCompactMaterials() {
        return this.compactMaterials;
    }

    @Override
    public StorageType getStorageType() {
        return this.storageType;
    }

    @Override
    public DatabaseConfiguration getDatabaseConfiguration() {
        return this.databaseConfiguration;
    }

    @Override
    public ServerType getServerType() {
        return serverType;
    }

    @Override
    public RedisConfiguration getRedisConfiguration() {
        return redisConfiguration;
    }

    @Override
    public List<MessageColor> getMessageColors() {
        return messageColors;
    }

    @Override
    public List<ChatCooldown> getCooldowns() {
        return this.cooldowns;
    }

    @Override
    public long[] getCooldownCommands() {
        return cooldownCommands;
    }

    @Override
    public List<TransformMaterial> getSmeltableMaterials() {
        return this.smeltableMaterials;
    }

    @Override
    public List<NearDistance> getNearPermissions() {
        return nearPermissions;
    }

    @Override
    public double getDefaultNearDistance() {
        return nearDistance;
    }

    @Override
    public double getNearDistance(Permissible permissible) {
        return this.nearPermissions.stream().filter(nearDistance -> permissible.hasPermission(nearDistance.permission())).map(NearDistance::distance).findFirst().orElse(this.nearDistance);
    }

    @Override
    public boolean isEnableCommandLog() {
        return this.enableCommandLog;
    }

    @Override
    public SimpleDateFormat getGlobalDateFormat() {
        return this.simpleDateFormat;
    }

    @Override
    public List<ReplacePlaceholder> getReplacePlaceholders() {
        return this.replacePlaceholders;
    }

    @Override
    public Optional<ReplacePlaceholder> getReplacePlaceholder(String placeholder) {
        return this.replacePlaceholders.stream().filter(replacePlaceholder -> replacePlaceholder.placeholder().equalsIgnoreCase(placeholder)).findFirst();
    }
}
