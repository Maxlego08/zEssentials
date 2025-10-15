package fr.maxlego08.essentials;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.impl.FoliaImplementation;
import com.tcoded.folialib.impl.PlatformScheduler;
import fr.maxlego08.essentials.api.Configuration;
import fr.maxlego08.essentials.api.ConfigurationFile;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.afk.AfkManager;
import fr.maxlego08.essentials.api.block.BlockTracker;
import fr.maxlego08.essentials.api.chat.InteractiveChat;
import fr.maxlego08.essentials.api.commands.CommandManager;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.discord.DiscordManager;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.api.enchantment.Enchantments;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.home.HomeManager;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.modules.ModuleManager;
import fr.maxlego08.essentials.api.permission.PermissionChecker;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.api.sanction.SanctionManager;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardManager;
import fr.maxlego08.essentials.api.server.EssentialsServer;
import fr.maxlego08.essentials.api.steps.StepManager;
import fr.maxlego08.essentials.api.storage.Persist;
import fr.maxlego08.essentials.api.storage.ServerStorage;
import fr.maxlego08.essentials.api.storage.StorageManager;
import fr.maxlego08.essentials.api.storage.adapter.LocationAdapter;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.EssentialsUtils;
import fr.maxlego08.essentials.api.utils.RandomWord;
import fr.maxlego08.essentials.api.utils.SafeLocation;
import fr.maxlego08.essentials.api.utils.Warp;
import fr.maxlego08.essentials.api.utils.component.ComponentMessage;
import fr.maxlego08.essentials.api.vault.VaultManager;
import fr.maxlego08.essentials.api.vote.VoteManager;
import fr.maxlego08.essentials.api.waypoint.WayPointHelper;
import fr.maxlego08.essentials.api.worldedit.WorldeditManager;
import fr.maxlego08.essentials.buttons.ButtonHomes;
import fr.maxlego08.essentials.buttons.ButtonPayConfirm;
import fr.maxlego08.essentials.buttons.ButtonTeleportationConfirm;
import fr.maxlego08.essentials.buttons.ButtonTeleportationConfirmHere;
import fr.maxlego08.essentials.buttons.kit.ButtonKitPreview;
import fr.maxlego08.essentials.buttons.mail.ButtonMailBox;
import fr.maxlego08.essentials.buttons.mail.ButtonMailBoxAdmin;
import fr.maxlego08.essentials.buttons.sanction.ButtonSanctionInformation;
import fr.maxlego08.essentials.buttons.sanction.ButtonSanctions;
import fr.maxlego08.essentials.buttons.vault.ButtonVaultIcon;
import fr.maxlego08.essentials.buttons.vault.ButtonVaultRename;
import fr.maxlego08.essentials.buttons.vault.ButtonVaultSlotDisable;
import fr.maxlego08.essentials.buttons.vault.ButtonVaultSlotItems;
import fr.maxlego08.essentials.commands.CommandLoader;
import fr.maxlego08.essentials.commands.ZCommandManager;
import fr.maxlego08.essentials.commands.commands.essentials.CommandEssentials;
import fr.maxlego08.essentials.enchantments.ZEnchantments;
import fr.maxlego08.essentials.listener.InvseeListener;
import fr.maxlego08.essentials.listener.PlayerListener;
import fr.maxlego08.essentials.loader.ButtonKitCooldownLoader;
import fr.maxlego08.essentials.loader.ButtonKitGetLoader;
import fr.maxlego08.essentials.loader.ButtonOptionLoader;
import fr.maxlego08.essentials.loader.ButtonSanctionLoader;
import fr.maxlego08.essentials.loader.ButtonVaultNoPermissionLoader;
import fr.maxlego08.essentials.loader.ButtonVaultNoPermissionAdminLoader;
import fr.maxlego08.essentials.loader.ButtonVaultOpenLoader;
import fr.maxlego08.essentials.loader.ButtonVaultOpenAdminLoader;
import fr.maxlego08.essentials.loader.ButtonWarpLoader;
import fr.maxlego08.essentials.messages.MessageLoader;
import fr.maxlego08.essentials.module.ZModuleManager;
import fr.maxlego08.essentials.module.modules.HomeModule;
import fr.maxlego08.essentials.module.modules.MailBoxModule;
import fr.maxlego08.essentials.module.modules.SanctionModule;
import fr.maxlego08.essentials.module.modules.StepModule;
import fr.maxlego08.essentials.module.modules.VoteModule;
import fr.maxlego08.essentials.module.modules.afk.AFKModule;
import fr.maxlego08.essentials.module.modules.chat.interactive.InteractiveChatHelper;
import fr.maxlego08.essentials.module.modules.chat.interactive.InteractiveChatPaperListener;
import fr.maxlego08.essentials.module.modules.chat.interactive.InteractiveChatSpigotListener;
import fr.maxlego08.essentials.module.modules.discord.DiscordModule;
import fr.maxlego08.essentials.module.modules.economy.EconomyModule;
import fr.maxlego08.essentials.module.modules.hologram.HologramModule;
import fr.maxlego08.essentials.module.modules.kit.KitModule;
import fr.maxlego08.essentials.module.modules.scoreboard.ScoreboardModule;
import fr.maxlego08.essentials.module.modules.vault.VaultModule;
import fr.maxlego08.essentials.module.modules.worldedit.WorldeditModule;
import fr.maxlego08.essentials.placeholders.DistantPlaceholder;
import fr.maxlego08.essentials.placeholders.LocalPlaceholder;
import fr.maxlego08.essentials.server.PaperServer;
import fr.maxlego08.essentials.server.SpigotServer;
import fr.maxlego08.essentials.storage.ConfigStorage;
import fr.maxlego08.essentials.storage.ZStorageManager;
import fr.maxlego08.essentials.storage.adapter.UserTypeAdapter;
import fr.maxlego08.essentials.task.FlyTask;
import fr.maxlego08.essentials.user.ZUser;
import fr.maxlego08.essentials.user.placeholders.ArmorPlaceholders;
import fr.maxlego08.essentials.user.placeholders.EconomyBaltopPlaceholders;
import fr.maxlego08.essentials.user.placeholders.RandomWordPlaceholders;
import fr.maxlego08.essentials.user.placeholders.ReplacePlaceholders;
import fr.maxlego08.essentials.user.placeholders.ServerPlaceholders;
import fr.maxlego08.essentials.user.placeholders.UserHomePlaceholders;
import fr.maxlego08.essentials.user.placeholders.UserItems1_21Placeholders;
import fr.maxlego08.essentials.user.placeholders.UserItemsPlaceholders;
import fr.maxlego08.essentials.user.placeholders.UserKitPlaceholders;
import fr.maxlego08.essentials.user.placeholders.UserPlaceholders;
import fr.maxlego08.essentials.user.placeholders.UserPlayTimePlaceholders;
import fr.maxlego08.essentials.user.placeholders.VotePlaceholders;
import fr.maxlego08.essentials.user.placeholders.WorldEditPlaceholders;
import fr.maxlego08.essentials.zutils.Metrics;
import fr.maxlego08.essentials.zutils.ZPlugin;
import fr.maxlego08.essentials.zutils.utils.ComponentMessageHelper;
import fr.maxlego08.essentials.zutils.utils.DefaultBlockTracker;
import fr.maxlego08.essentials.zutils.utils.PlaceholderUtils;
import fr.maxlego08.essentials.zutils.utils.VersionChecker;
import fr.maxlego08.essentials.zutils.utils.ZServerStorage;
import fr.maxlego08.essentials.zutils.utils.documentation.CommandMarkdownGenerator;
import fr.maxlego08.essentials.zutils.utils.documentation.PermissionInfo;
import fr.maxlego08.essentials.zutils.utils.documentation.PermissionMarkdownGenerator;
import fr.maxlego08.essentials.zutils.utils.documentation.PlaceholderMarkdownGenerator;
import fr.maxlego08.essentials.zutils.utils.paper.PaperUtils;
import fr.maxlego08.essentials.zutils.utils.spigot.SpigotUtils;
import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.loader.NoneLoader;
import fr.maxlego08.menu.api.pattern.PatternManager;
import fr.maxlego08.menu.zcore.utils.nms.NmsVersion;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.ServicePriority;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public final class ZEssentialsPlugin extends ZPlugin implements EssentialsPlugin {

    private final UUID consoleUniqueId = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private final List<Material> materials = Arrays.stream(Material.values()).filter(e -> !e.name().startsWith("LEGACY_")).toList();
    private final Enchantments enchantments = new ZEnchantments();
    private final List<PermissionChecker> permissionCheckers = new ArrayList<>();
    private EssentialsUtils essentialsUtils;
    private ServerStorage serverStorage = new ZServerStorage(this);
    private InventoryManager inventoryManager;
    private ButtonManager buttonManager;
    private PatternManager patternManager;
    private EssentialsServer essentialsServer;
    private ScoreboardManager scoreboardManager;
    private HologramManager hologramManager;
    private InteractiveChatHelper interactiveChatHelper;
    private RandomWord randomWord;
    private long serverStartUptime;
    private BlockTracker blockTracker = new DefaultBlockTracker();
    private WayPointHelper wayPointHelper;
    private String lastFirstJoinPlayerName;

    @Override
    public void onEnable() {

        this.serverStartUptime = System.currentTimeMillis();
        this.saveDefaultConfig();
        this.saveOrUpdateConfiguration("config.yml", true);
        this.enchantments.register();

        FoliaLib foliaLib = new FoliaLib(this);
        this.platformScheduler = foliaLib.getScheduler();

        var isPaper = isPaperVersion();
        this.essentialsUtils = isPaper ? new PaperUtils(this) : new SpigotUtils(this);
        this.essentialsServer = isPaper ? new PaperServer(this) : new SpigotServer(this);
        this.interactiveChatHelper = isPaper ? new InteractiveChatPaperListener() : new InteractiveChatSpigotListener();
        this.registerListener(this.interactiveChatHelper);

        this.placeholder = new LocalPlaceholder(this);
        DistantPlaceholder distantPlaceholder = new DistantPlaceholder(this, this.placeholder);
        distantPlaceholder.register();

        this.economyManager = new EconomyModule(this);
        this.scoreboardManager = new ScoreboardModule(this);
        this.hologramManager = new HologramModule(this);

        this.inventoryManager = this.getProvider(InventoryManager.class);
        this.buttonManager = this.getProvider(ButtonManager.class);
        this.patternManager = this.getProvider(PatternManager.class);
        this.registerButtons();

        this.moduleManager = new ZModuleManager(this);

        this.gson = getGsonBuilder().create();
        this.persist = new Persist(this);

        // Configurations files
        this.registerConfiguration(this.configuration = new MainConfiguration(this));
        this.registerConfiguration(new MessageLoader(this));

        // Load configuration files
        this.configurationFiles.forEach(ConfigurationFile::load);
        ConfigStorage.getInstance().load(getPersist());

        // Commands
        this.registerListener(this.commandManager = new ZCommandManager(this));
        this.registerCommand("zessentials", new CommandEssentials(this), "ess");

        CommandLoader commandLoader = new CommandLoader(this);
        commandLoader.loadCommands(this.commandManager);

        this.getLogger().info("Create " + this.commandManager.countCommands() + " commands.");

        // Essentials Server
        /*if (this.configuration.getServerType() == ServerType.REDIS) {
            this.essentialsServer = new RedisServer(this);
            this.getLogger().info("Using Redis server.");
        }*/

        this.essentialsServer.onEnable();

        // Storage
        this.storageManager = new ZStorageManager(this);
        this.registerListener(this.storageManager);
        this.storageManager.onEnable();

        this.moduleManager.loadModules();
        this.getVoteManager().startResetTask();
        getVaultManager().loadVaults();

        this.registerListener(new PlayerListener(this));
        this.registerPlaceholder(UserPlaceholders.class);
        this.registerPlaceholder(UserItemsPlaceholders.class);
        if (NmsVersion.getCurrentVersion().getVersion() >= NmsVersion.V_1_21.getVersion()) {
            this.registerPlaceholder(UserItems1_21Placeholders.class);
        }
        this.registerPlaceholder(UserHomePlaceholders.class);
        this.registerPlaceholder(UserPlayTimePlaceholders.class);
        this.registerPlaceholder(UserKitPlaceholders.class);
        this.registerPlaceholder(ReplacePlaceholders.class);
        this.registerPlaceholder(EconomyBaltopPlaceholders.class);
        this.registerPlaceholder(VotePlaceholders.class);
        this.registerPlaceholder(WorldEditPlaceholders.class);
        this.registerPlaceholder(ServerPlaceholders.class);
        this.registerPlaceholder(ArmorPlaceholders.class);
        this.randomWord = this.registerPlaceholder(RandomWordPlaceholders.class);

        new Metrics(this, 21703);
        new VersionChecker(this, 325);

        // Load ProtocolLib
        /*if (this.moduleManager.getModuleConfiguration("chat").getBoolean("command-placeholder.enable-replace-all-message") && getServer().getPluginManager().isPluginEnabled("ProtocolLib") && this.isPaperVersion()) {
            PacketListener packetListener = new PacketListener();
            packetListener.registerPackets(this);
        }*/

        this.loadHooks();

        this.getServer().getServicesManager().register(EssentialsPlugin.class, this, this, ServicePriority.Normal);

        this.registerListener(new InvseeListener());

        this.generateDocs();

        if (this.configuration.isTempFlyTask()) {
            new FlyTask(this);
        }
    }

    @Override
    public void onLoad() {
        try {

            File file = new File(this.getDataFolder(), "modules/economy/config.yml");
            if (!file.exists()) {
                this.saveResource("modules/economy/config.yml", false);
            }
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            if (!configuration.getBoolean("enable", false)) return;

            Class.forName("net.milkbowl.vault.economy.Economy");
            var optional = createInstance("VaultEconomy");
            if (optional.isPresent()) {
                getLogger().info("Register Vault Economy.");
            } else {
                getLogger().severe("Impossible to register Vault Economy.");
            }
        } catch (final ClassNotFoundException ignored) {
        }
    }

    @Override
    public void onDisable() {

        // Storage
        if (this.storageManager != null) this.storageManager.onDisable();
        if (this.persist != null) ConfigStorage.getInstance().save(this.persist);

        this.essentialsServer.onDisable();

    }

    private void registerButtons() {

        this.buttonManager.register(new NoneLoader(this, ButtonTeleportationConfirmHere.class, "ZESSENTIALS_TELEPORTATION_CONFIRM_HERE"));
        this.buttonManager.register(new NoneLoader(this, ButtonTeleportationConfirm.class, "ZESSENTIALS_TELEPORTATION_CONFIRM"));
        this.buttonManager.register(new NoneLoader(this, ButtonPayConfirm.class, "ZESSENTIALS_PAY_CONFIRM"));
        this.buttonManager.register(new NoneLoader(this, ButtonHomes.class, "ZESSENTIALS_HOMES"));
        this.buttonManager.register(new NoneLoader(this, ButtonSanctionInformation.class, "ZESSENTIALS_SANCTION_INFORMATION"));
        this.buttonManager.register(new NoneLoader(this, ButtonSanctions.class, "ZESSENTIALS_SANCTIONS"));
        this.buttonManager.register(new NoneLoader(this, ButtonKitPreview.class, "ZESSENTIALS_KIT_PREVIEW"));
        this.buttonManager.register(new NoneLoader(this, ButtonMailBox.class, "ZESSENTIALS_MAILBOX"));
        this.buttonManager.register(new NoneLoader(this, ButtonMailBoxAdmin.class, "ZESSENTIALS_MAILBOX_ADMIN"));
        this.buttonManager.register(new NoneLoader(this, ButtonVaultSlotDisable.class, "ZESSENTIALS_VAULT_SLOTS_DISABLE"));
        this.buttonManager.register(new NoneLoader(this, ButtonVaultSlotItems.class, "ZESSENTIALS_VAULT_SLOTS_ITEMS"));
        this.buttonManager.register(new NoneLoader(this, ButtonVaultIcon.class, "ZESSENTIALS_VAULT_CHANGE_ICON"));
        this.buttonManager.register(new NoneLoader(this, ButtonVaultRename.class, "ZESSENTIALS_VAULT_CHANGE_NAME"));
        this.buttonManager.register(new ButtonWarpLoader(this));
        this.buttonManager.register(new ButtonSanctionLoader(this));
        this.buttonManager.register(new ButtonKitCooldownLoader(this));
        this.buttonManager.register(new ButtonKitGetLoader(this));
        this.buttonManager.register(new ButtonVaultOpenLoader(this));
        this.buttonManager.register(new ButtonVaultNoPermissionLoader(this));
        this.buttonManager.register(new ButtonVaultOpenAdminLoader(this));
        this.buttonManager.register(new ButtonVaultNoPermissionAdminLoader(this));
        this.buttonManager.register(new ButtonOptionLoader(this));

    }

    @Override
    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public List<ConfigurationFile> getConfigurationFiles() {
        return this.configurationFiles;
    }

    @Override
    public Gson getGson() {
        return this.gson;
    }

    @Override
    public Persist getPersist() {
        return this.persist;
    }

    @Override
    public PlatformScheduler getScheduler() {
        return this.platformScheduler;
    }

    @Override
    public ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    @Override
    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    @Override
    public ButtonManager getButtonManager() {
        return this.buttonManager;
    }

    @Override
    public PatternManager getPatternManager() {
        return this.patternManager;
    }

    @Override
    public Placeholder getPlaceholder() {
        return this.placeholder;
    }

    @Override
    public StorageManager getStorageManager() {
        return this.storageManager;
    }

    private GsonBuilder getGsonBuilder() {
        return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE).registerTypeAdapter(SafeLocation.class, new LocationAdapter(this)).registerTypeAdapter(User.class, new UserTypeAdapter(this)).registerTypeAdapter(ZUser.class, new UserTypeAdapter(this));
    }

    private <T extends PlaceholderRegister> T registerPlaceholder(Class<T> placeholderClass) {
        try {
            PlaceholderRegister placeholderRegister = placeholderClass.getConstructor().newInstance();
            placeholderRegister.register(this.placeholder, this);
            return (T) placeholderRegister;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }

    @Override
    public boolean isEconomyEnable() {
        return this.economyManager.isEnable();
    }

    @Override
    public EconomyManager getEconomyManager() {
        return this.economyManager;
    }

    @Override
    public UUID getConsoleUniqueId() {
        return this.consoleUniqueId;
    }

    private void generateDocs() {
        CommandMarkdownGenerator commandMarkdownGenerator = new CommandMarkdownGenerator();
        PlaceholderMarkdownGenerator placeholderMarkdownGenerator = new PlaceholderMarkdownGenerator();
        PermissionMarkdownGenerator permissionMarkdownGenerator = new PermissionMarkdownGenerator();

        File folder = new File(getDataFolder(), "docs");
        if (!folder.exists()) folder.mkdirs();

        File fileCommand = new File(folder, "commands.md");
        File filePlaceholder = new File(folder, "placeholders.md");
        File filePermissions = new File(folder, "permissions.md");

        try {
            commandMarkdownGenerator.generateMarkdownFile(this.commandManager.getSortCommands(), fileCommand.toPath());
            getLogger().info("Markdown 'commands.md' file successfully generated!");
        } catch (IOException exception) {
            getLogger().severe("Error while writing the file commands: " + exception.getMessage());
            exception.printStackTrace();
        }

        try {
            placeholderMarkdownGenerator.generateMarkdownFile(((LocalPlaceholder) this.placeholder).getAutoPlaceholders(), filePlaceholder.toPath());
            getLogger().info("Markdown 'placeholders.md' file successfully generated!");
        } catch (IOException exception) {
            getLogger().severe("Error while writing the file placeholders: " + exception.getMessage());
            exception.printStackTrace();
        }

        try {

            List<PermissionInfo> permissions = new ArrayList<>();

            var commands = commandManager.getCommands();
            for (Permission permission : Permission.values()) {

                var optional = commands.stream().filter(essentialsCommand -> essentialsCommand.getPermission() != null && essentialsCommand.getPermission().equals(permission.asPermission())).findFirst();
                String description = permission.getDescription();
                if (optional.isPresent()) {
                    var command = optional.get();
                    if (command.getDescription() != null) {
                        description = command.getDescription();
                    }
                }

                permissions.add(new PermissionInfo(permission.toPermission(), description));
            }

            permissionMarkdownGenerator.generateMarkdownFile(permissions, filePermissions.toPath());
            getLogger().info("Markdown 'permissions.md' file successfully generated!");
        } catch (IOException exception) {
            getLogger().severe("Error while writing the file permissions: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    @Override
    public ServerStorage getServerStorage() {
        return serverStorage;
    }

    @Override
    public void setServerStorage(ServerStorage serverStorage) {
        this.serverStorage = serverStorage;
    }

    @Override
    public String getLastFirstJoinPlayerName() {
        return this.lastFirstJoinPlayerName;
    }

    @Override
    public void setLastFirstJoinPlayerName(String playerName) {
        this.lastFirstJoinPlayerName = playerName;
    }

    @Override
    public boolean isFolia() {
        return this.platformScheduler instanceof FoliaImplementation;
    }

    @Override
    public List<Warp> getWarps() {
        return ConfigStorage.warps;
    }

    @Override
    public Optional<Warp> getWarp(String name) {
        return getWarps().stream().filter(warp -> warp.name().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public int getMaxHome(Permissible permissible) {
        return this.moduleManager.getModule(HomeModule.class).getMaxHome(permissible);
    }

    @Override
    public User getUser(UUID uniqueId) {
        return this.storageManager.getStorage().getUser(uniqueId);
    }

    @Override
    public EssentialsServer getEssentialsServer() {
        return this.essentialsServer;
    }

    @Override
    public EssentialsUtils getUtils() {
        return this.essentialsUtils;
    }

    @Override
    public void debug(String string) {
        if (this.configuration != null && this.configuration.isEnableDebug()) {
            this.getLogger().info(string);
        }
    }

    @Override
    public void openInventory(Player player, String inventoryName) {
        this.inventoryManager.getInventory(this, inventoryName).ifPresent(inventory -> {
            this.platformScheduler.runAtLocation(player.getLocation(), wrappedTask -> {
                this.inventoryManager.getCurrentPlayerInventory(player).ifPresentOrElse(oldInventory -> {
                    this.inventoryManager.openInventory(player, inventory, 1, oldInventory);
                }, () -> this.inventoryManager.openInventory(player, inventory));
            });
        });
    }

    @Override
    public void saveOrUpdateConfiguration(String resourcePath, boolean deep) {
        this.saveOrUpdateConfiguration(resourcePath, resourcePath, deep);
    }

    @Override
    public void saveOrUpdateConfiguration(String resourcePath, String toPath, boolean deep) {

        File file = new File(getDataFolder(), toPath);
        if (!file.exists()) {
            saveResource(resourcePath, toPath, false);
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        try {

            InputStream inputStream = this.getResource(resourcePath);

            if (inputStream == null) {
                this.getLogger().severe("Cannot find file " + resourcePath);
                return;
            }

            Reader defConfigStream = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);

            Set<String> defaultKeys = defConfig.getKeys(deep);

            boolean configUpdated = false;
            for (String key : defaultKeys) {
                if (!config.contains(key)) {
                    debug("I can’t find " + key + " in the file " + file.getName());
                    configUpdated = true;
                }
            }

            config.setDefaults(defConfig);
            config.options().copyDefaults(true);

            if (configUpdated) {
                this.getLogger().info("Update file " + toPath);
                config.save(file);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Optional<Kit> getKit(String kitName) {
        return this.moduleManager.getModule(KitModule.class).getKit(kitName);
    }

    @Override
    public void giveKit(User user, Kit kit, boolean bypassCooldown) {
        this.moduleManager.getModule(KitModule.class).giveKit(user, kit, bypassCooldown);
    }

    @Override
    public List<Material> getMaterials() {
        return this.materials;
    }

    @Override
    public ScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }

    @Override
    public void give(OfflinePlayer offlinePlayer, ItemStack itemStack) {
        if (offlinePlayer.isOnline()) {
            give(Objects.requireNonNull(offlinePlayer.getPlayer()), itemStack);
        } else {
            MailBoxModule mailBoxModule = this.moduleManager.getModule(MailBoxModule.class);
            if (!mailBoxModule.isEnable()) {
                var location = offlinePlayer.getLocation();
                if (location == null) return;
                location.getWorld().dropItemNaturally(location, itemStack);
                return;
            }

            mailBoxModule.addItemAndFix(offlinePlayer.getUniqueId(), itemStack);
        }
    }

    @Override
    public void give(Player player, ItemStack itemStack) {

        PlayerInventory inventory = player.getInventory();

        Map<Integer, ItemStack> result = inventory.addItem(itemStack);
        if (result.isEmpty()) return;

        MailBoxModule mailBoxModule = this.moduleManager.getModule(MailBoxModule.class);
        if (!mailBoxModule.isEnable()) {
            result.values().forEach(item -> player.getWorld().dropItemNaturally(player.getLocation(), item));
            return;
        }

        result.values().forEach(item -> mailBoxModule.addItemAndFix(player.getUniqueId(), item));
    }

    @Override
    public HologramManager getHologramManager() {
        return this.hologramManager;
    }

    @Override
    public ComponentMessage getComponentMessage() {
        return ComponentMessageHelper.componentMessage;
    }

    @Override
    public String papi(Player player, String string) {
        return PlaceholderUtils.PapiHelper.papi(string, player);
    }

    @Override
    public <T> Optional<T> createInstance(String className) {
        try {
            Class<?> clazz = Class.forName("fr.maxlego08.essentials.hooks." + className);

            try {
                Constructor<?> constructor = clazz.getConstructor(EssentialsPlugin.class);
                // noinspection unchecked
                return Optional.of((T) constructor.newInstance(this));
            } catch (NoSuchMethodException error) {
                // noinspection unchecked
                return Optional.of((T) clazz.getConstructor().newInstance());
            }
        } catch (Exception exception) {
            this.getLogger().severe("Cannot create a new instance for the class " + className);
            this.getLogger().severe(exception.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public VoteManager getVoteManager() {
        return this.moduleManager.getModule(VoteModule.class);
    }

    @Override
    public VaultManager getVaultManager() {
        return this.moduleManager.getModule(VaultModule.class);
    }

    @Override
    public WorldeditManager getWorldeditManager() {
        return this.moduleManager.getModule(WorldeditModule.class);
    }

    @Override
    public InteractiveChat startInteractiveChat(Player player, Consumer<String> consumer, long expiredAt) {
        InteractiveChat interactiveChat = new InteractiveChat(consumer, expiredAt);
        this.interactiveChatHelper.register(player, interactiveChat);
        return interactiveChat;
    }

    @Override
    public Enchantments getEnchantments() {
        return this.enchantments;
    }

    @Override
    public long getServerStartupTime() {
        return serverStartUptime;
    }

    @Override
    public RandomWord getRandomWord() {
        return randomWord;
    }

    @Override
    public BlockTracker getBlockTracker() {
        return this.blockTracker;
    }

    @Override
    public void setBlockTracker(BlockTracker blockTracker) {
        this.blockTracker = blockTracker;
    }

    @Override
    public List<PermissionChecker> getPermissions() {
        return this.permissionCheckers;
    }

    @Override
    public void addMailBoxItem(UUID uuid, ItemStack itemStack) {
        getModuleManager().getModule(MailBoxModule.class).addItem(uuid, itemStack);
    }

    @Override
    public StepManager getStepManager() {
        return getModuleManager().getModule(StepModule.class);
    }

    @Override
    public AfkManager getAfkManager() {
        return getModuleManager().getModule(AFKModule.class);
    }

    @Override
    public DiscordManager getDiscordManager() {
        return getModuleManager().getModule(DiscordModule.class);
    }

    @Override
    public HomeManager getHomeManager() {
        return getModuleManager().getModule(HomeModule.class);
    }

    @Override
    public SanctionManager getSanctionManager() {
        return getModuleManager().getModule(SanctionModule.class);
    }

    @Override
    public WayPointHelper getWayPointHelper() {
        if (this.wayPointHelper == null) {
            String version = NmsVersion.getCurrentVersion().name().replace("V_", "v");
            String className = String.format("fr.maxlego08.essentials.nms.%s.WayPointPacket", version);

            try {
                Class<?> clazz = Class.forName(className);
                this.wayPointHelper = (WayPointHelper) clazz.getConstructor().newInstance();
            } catch (Exception exception) {
                this.getLogger().severe("Cannot create a new instance for the class " + className);
                this.getLogger().severe(exception.getMessage());
            }
        }
        return this.wayPointHelper;
    }

    private void loadHooks() {
        if (getServer().getPluginManager().isPluginEnabled("BlockTracker")) {
            createInstance("BlockTrackerHook").ifPresent(object -> {
                this.blockTracker = (BlockTracker) object;
                this.getLogger().info("Register BlockTracker.");
            });
        }

        if (getServer().getPluginManager().isPluginEnabled("SuperiorSkyBlock2")) {
            createInstance("SuperiorSkyBlockPermission").ifPresent(object -> {
                this.permissionCheckers.add((PermissionChecker) object);
                this.getLogger().info("Register SuperiorSkyBlock Permission Checker.");
            });
        }

        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            createInstance("WorldGuardBlockPermission").ifPresent(object -> {
                this.permissionCheckers.add((PermissionChecker) object);
                this.getLogger().info("Register WorldGuard Permission Checker.");
            });
        }

        if (getServer().getPluginManager().isPluginEnabled("Votifier")) {
            createInstance("NuVotifierHook").ifPresent(object -> this.getLogger().info("Register NuVotifierHook."));
        }

        if (getServer().getPluginManager().isPluginEnabled("NChat")) {
            createInstance("NChatHook").ifPresent(object -> this.getLogger().info("Register NChatHook."));
        }
    }
}
