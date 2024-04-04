package fr.maxlego08.essentials;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.impl.ServerImplementation;
import fr.maxlego08.essentials.api.Configuration;
import fr.maxlego08.essentials.api.ConfigurationFile;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandManager;
import fr.maxlego08.essentials.api.database.MigrationManager;
import fr.maxlego08.essentials.api.economy.EconomyProvider;
import fr.maxlego08.essentials.api.modules.ModuleManager;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.api.storage.Persist;
import fr.maxlego08.essentials.api.storage.StorageManager;
import fr.maxlego08.essentials.api.storage.adapter.LocationAdapter;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.buttons.ButtonTeleportationConfirm;
import fr.maxlego08.essentials.commands.CommandLoader;
import fr.maxlego08.essentials.commands.ZCommandManager;
import fr.maxlego08.essentials.commands.commands.essentials.CommandEssentials;
import fr.maxlego08.essentials.database.ZMigrationManager;
import fr.maxlego08.essentials.economy.EconomyManager;
import fr.maxlego08.essentials.hooks.VaultEconomy;
import fr.maxlego08.essentials.listener.PlayerListener;
import fr.maxlego08.essentials.messages.MessageLoader;
import fr.maxlego08.essentials.module.ZModuleManager;
import fr.maxlego08.essentials.placeholders.DistantPlaceholder;
import fr.maxlego08.essentials.placeholders.LocalPlaceholder;
import fr.maxlego08.essentials.storage.ZStorageManager;
import fr.maxlego08.essentials.storage.adapter.UserTypeAdapter;
import fr.maxlego08.essentials.user.UserPlaceholders;
import fr.maxlego08.essentials.user.ZUser;
import fr.maxlego08.essentials.zutils.ZPlugin;
import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.pattern.PatternManager;
import fr.maxlego08.menu.button.loader.NoneLoader;
import org.bukkit.Location;

import java.lang.reflect.Modifier;
import java.util.List;

public final class ZEssentialsPlugin extends ZPlugin implements EssentialsPlugin {

    private InventoryManager inventoryManager;
    private ButtonManager buttonManager;
    private PatternManager patternManager;

    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        FoliaLib foliaLib = new FoliaLib(this);
        this.serverImplementation = foliaLib.getImpl();

        this.migrationManager = new ZMigrationManager(this);
        this.migrationManager.registerMigration();

        this.placeholder = new LocalPlaceholder(this);
        DistantPlaceholder distantPlaceholder = new DistantPlaceholder(this, this.placeholder);
        distantPlaceholder.register();

        this.economyProvider = new EconomyManager(this);

        this.inventoryManager = this.getProvider(InventoryManager.class);
        this.buttonManager = this.getProvider(ButtonManager.class);
        this.patternManager = this.getProvider(PatternManager.class);
        this.registerButtons();

        this.moduleManager = new ZModuleManager(this);

        this.gson = getGsonBuilder().create();
        this.persist = new Persist(this);

        // Configurations files
        this.registerConfiguration(new MessageLoader(this));
        this.registerConfiguration(this.configuration = new MainConfiguration(this));

        // Load configuration files
        this.configurationFiles.forEach(ConfigurationFile::load);

        // Commands
        this.commandManager = new ZCommandManager(this);
        this.registerCommand("zessentials", new CommandEssentials(this), "ess");

        CommandLoader commandLoader = new CommandLoader(this);
        commandLoader.loadCommands(this.commandManager);

        this.getLogger().info("Create " + this.commandManager.countCommands() + " commands.");

        // Storage
        this.storageManager = new ZStorageManager(this);
        this.registerListener(this.storageManager);
        this.storageManager.onEnable();

        this.moduleManager.loadModules();

        this.registerListener(new PlayerListener(this));
        this.registerPlaceholder(UserPlaceholders.class);
    }

    @Override
    public void onLoad() {

        try {
            Class.forName("net.milkbowl.vault.economy.Economy");
            new VaultEconomy(this);
            getLogger().info("Register Vault Economy.");
        } catch (final ClassNotFoundException ignored) {
            ignored.printStackTrace();
        }

    }

    @Override
    public void onDisable() {

        // Storage
        if (this.storageManager != null) this.storageManager.onDisable();
    }

    private void registerButtons() {

        this.buttonManager.register(new NoneLoader(this, ButtonTeleportationConfirm.class, "essentials_teleportation_confirm"));

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
    public ServerImplementation getScheduler() {
        return this.serverImplementation;
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
        return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE)
                .registerTypeAdapter(Location.class, new LocationAdapter(this))
                .registerTypeAdapter(User.class, new UserTypeAdapter(this))
                .registerTypeAdapter(ZUser.class, new UserTypeAdapter(this));
    }

    private void registerPlaceholder(Class<? extends PlaceholderRegister> placeholderClass) {
        try {
            PlaceholderRegister placeholderRegister = placeholderClass.getConstructor().newInstance();
            placeholderRegister.register(this.placeholder, this);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }

    @Override
    public MigrationManager getMigrationManager() {
        return this.migrationManager;
    }

    @Override
    public boolean isEconomyEnable() {
        return this.economyProvider.isEnable();
    }

    @Override
    public EconomyProvider getEconomyProvider() {
        return this.economyProvider;
    }
}
