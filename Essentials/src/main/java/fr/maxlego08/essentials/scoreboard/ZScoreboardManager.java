package fr.maxlego08.essentials.scoreboard;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.scoreboard.EssentialsScoreboard;
import fr.maxlego08.essentials.api.scoreboard.PlayerBoard;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardLine;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardManager;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.scoreboard.board.ClassicBoard;
import fr.maxlego08.essentials.scoreboard.board.ComponentBoard;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ZScoreboardManager extends ZModule implements ScoreboardManager {


    private final List<EssentialsScoreboard> essentialsScoreboards = new ArrayList<>();
    private final Map<Player, PlayerBoard> boards = new HashMap<>();
    private EssentialsScoreboard defaultScoreboard;

    public ZScoreboardManager(ZEssentialsPlugin plugin) {
        super(plugin, "scoreboard");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        YamlConfiguration configuration = getConfiguration();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("scoreboards");
        if (configurationSection == null) return;


        configurationSection.getKeys(false).forEach(scoreboardName -> {

            ConfigurationSection section = configurationSection.getConfigurationSection(scoreboardName);
            if (section == null) return;

            EssentialsScoreboard essentialsScoreboard = new ZEssentialsScoreboard(scoreboardName, section);
            if (essentialsScoreboard.isDefault()) {
                defaultScoreboard = essentialsScoreboard;
            }
            essentialsScoreboards.add(essentialsScoreboard);
        });


        HandlerList.unregisterAll(this);

        if (this.isEnable()) {
            reloadPlayers();
            registerEvents();
        }
    }

    private void registerEvents() {

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(this, this.plugin);

        List<String> events = essentialsScoreboards.stream().map(EssentialsScoreboard::getLines).flatMap(List::stream).map(ScoreboardLine::getEventName).filter(Objects::nonNull).distinct().toList();

        events.forEach(eventName -> {

            try {
                Class<?> eventClass = Class.forName(eventName);

                if (!org.bukkit.event.Event.class.isAssignableFrom(eventClass)) {
                    this.plugin.getLogger().severe("Class " + eventName + " is not an event !");
                    return;
                }

                pluginManager.registerEvent(eventClass.asSubclass(Event.class), this, EventPriority.NORMAL, (listener, event) -> updateLineWithEvent(eventName), this.plugin);
            } catch (ClassNotFoundException exception) {
                this.plugin.getLogger().severe("Class " + eventName + " was not found !");
                exception.printStackTrace();
            }
        });
    }

    private void updateLineWithEvent(String eventName) {
        System.out.println("Mise à jour de " + eventName);
        for (PlayerBoard board : this.boards.values()) {
            EssentialsScoreboard essentialsScoreboard = board.getScoreboard();
            this.plugin.getScheduler().runAsync(wrappedTask -> {
                essentialsScoreboard.getLines().stream().filter(scoreboardLine -> scoreboardLine.getEventName() != null && scoreboardLine.getEventName().equals(eventName)).forEach(scoreboardLine -> scoreboardLine.update(board));
            });
        }
    }


    @Override
    public void reloadPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerBoard board = this.boards.remove(player);
            if (board != null) {
                board.delete();
                Optional<EssentialsScoreboard> optional = this.getScoreboard(board.getScoreboard().getName());
                optional.ifPresent(essentialsScoreboard -> this.createScoreboard(player, essentialsScoreboard));
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (this.defaultScoreboard == null) return;

        this.createScoreboard(event.getPlayer(), this.defaultScoreboard);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.deleteBoard(event.getPlayer());
    }

    @Override
    public PlayerBoard createScoreboard(Player player, EssentialsScoreboard essentialsScoreboard) {

        PlayerBoard playerBoard = this.plugin.isPaperVersion() ? new ComponentBoard(player, essentialsScoreboard) : new ClassicBoard(player, essentialsScoreboard);
        this.boards.put(player.getPlayer(), playerBoard);

        essentialsScoreboard.create(playerBoard);

        return playerBoard;
    }

    @Override
    public void deleteBoard(Player player) {
        PlayerBoard board = this.boards.remove(player);
        if (board != null) board.delete();
    }

    @Override
    public Optional<PlayerBoard> getBoard(Player player) {
        return Optional.ofNullable(this.boards.get(player));
    }

    @Override
    public List<EssentialsScoreboard> getEssentialsScoreboards() {
        return this.essentialsScoreboards;
    }

    @Override
    public EssentialsScoreboard getDefaultScoreboard() {
        return this.defaultScoreboard;
    }

    @Override
    public Optional<EssentialsScoreboard> getScoreboard(String name) {
        return this.essentialsScoreboards.stream().filter(essentialsScoreboard -> essentialsScoreboard.getName().equalsIgnoreCase(name)).findFirst();
    }
}
