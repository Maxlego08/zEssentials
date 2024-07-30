package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.messages.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author Maxlego08
 */
public class VersionChecker extends ZUtils implements Listener {

    private final String URL_API = "https://groupez.dev/api/v1/resource/version/%s";
    private final String URL_RESOURCE = "https://groupez.dev/resources/%s";
    private final EssentialsPlugin plugin;
    private final int pluginID;
    private boolean useLastVersion = false;

    public VersionChecker(EssentialsPlugin plugin, int pluginID) {
        super();
        this.plugin = plugin;
        this.pluginID = pluginID;
        this.useLastVersion();
    }

    public void useLastVersion() {

        Bukkit.getPluginManager().registerEvents(this, this.plugin);

        String pluginVersion = this.plugin.getDescription().getVersion();
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        this.getVersion(version -> {

            long ver = Long.parseLong(version.replace(".", ""));
            long plVersion = Long.parseLong(pluginVersion.replace(".", ""));
            atomicBoolean.set(this.useLastVersion = plVersion >= ver);
            if (atomicBoolean.get()) {
                this.plugin.getLogger().info("No update available.");
            } else {
                this.plugin.getLogger().severe("");
                this.plugin.getLogger().severe("New update available. Your version: " + pluginVersion + ", latest version: " + version);
                this.plugin.getLogger().severe("Download plugin here: " + String.format(URL_RESOURCE, this.pluginID));
                this.plugin.getLogger().severe("");
            }
        });

    }

    @EventHandler
    public void onConnect(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (!useLastVersion && event.getPlayer().hasPermission("zplugin.notifs")) {
            plugin.getScheduler().runLater(() -> {
                String prefix = Message.PREFIX.getMessageAsString();
                message(player, prefix + "§cYou do not use the latest version of the plugin! Thank you for taking the latest version to avoid any risk of problem!");
                message(player, prefix + "§fDownload plugin here: §a" + String.format(URL_RESOURCE, pluginID));
            }, 20 * 2);
        }
    }

    /**
     * Get version by plugin id
     *
     * @param consumer - Do something after
     */
    public void getVersion(Consumer<String> consumer) {
        this.plugin.getScheduler().runAsync(wrappedTask -> {
            final String apiURL = String.format(URL_API, this.pluginID);
            try {
                URL url = new URL(apiURL);
                URLConnection hc = url.openConnection();
                hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
                Scanner scanner = new Scanner(hc.getInputStream());
                if (scanner.hasNext()) consumer.accept(scanner.next());
                scanner.close();
            } catch (IOException exception) {
                this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
            }
        });
    }

}
