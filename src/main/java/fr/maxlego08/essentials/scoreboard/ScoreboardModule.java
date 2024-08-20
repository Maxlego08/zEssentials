package fr.maxlego08.essentials.scoreboard;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.scoreboard.EssentialsScoreboard;
import fr.maxlego08.essentials.api.scoreboard.JoinCondition;
import fr.maxlego08.essentials.api.scoreboard.PlayerBoard;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardLine;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardManager;
import fr.maxlego08.essentials.api.scoreboard.TaskCondition;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.scoreboard.board.ClassicBoard;
import fr.maxlego08.essentials.scoreboard.board.ComponentBoard;
import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.requirement.Permissible;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ScoreboardModule extends ZModule implements ScoreboardManager {


    private final List<EssentialsScoreboard> essentialsScoreboards = new ArrayList<>();
    private final Map<UUID, PlayerBoard> boards = new HashMap<>();
    private final List<JoinCondition> joinConditions = new ArrayList<>();
    private final List<TaskCondition> taskConditions = new ArrayList<>();
    private boolean enableTaskConditions;
    private int taskConditionsInterval;
    private EssentialsScoreboard defaultScoreboard;
    private WrappedTask wrappedTask;

    public ScoreboardModule(ZEssentialsPlugin plugin) {
        super(plugin, "scoreboard");
        this.isRegisterEvent = false;
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        if (wrappedTask != null) {
            wrappedTask.cancel();
        }

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

        loadJoinConditions(configuration);
        loadTaskConditions(configuration);

        HandlerList.unregisterAll(this);

        if (this.isEnable()) {
            reloadPlayers();
            registerEvents();
        }

        if (this.enableTaskConditions) {
            this.wrappedTask = this.plugin.getScheduler().runTimer(this::updateScoreboards, this.taskConditionsInterval, this.taskConditionsInterval, TimeUnit.SECONDS);
        }
    }

    private void loadJoinConditions(YamlConfiguration configuration) {
        this.joinConditions.clear();

        ButtonManager manager = plugin.getButtonManager();

        List<Map<?, ?>> mapList = configuration.getMapList("join-conditions");
        mapList.forEach(map -> {

            int priority = ((Number) map.get("priority")).intValue();
            String scoreboard = (String) map.get("scoreboard");
            List<Map<String, Object>> mapPermissibles = (List<Map<String, Object>>) map.get("requirements");
            List<Permissible> permissibles = mapPermissibles == null ? new ArrayList<>() : manager.loadPermissible(mapPermissibles, "join-conditions", new File(getFolder(), "config.yml"));

            this.joinConditions.add(new JoinCondition(priority, scoreboard, permissibles));
        });
    }

    private void loadTaskConditions(YamlConfiguration configuration) {
        this.taskConditions.clear();

        ButtonManager manager = plugin.getButtonManager();

        List<Map<?, ?>> mapList = configuration.getMapList("task-conditions");
        mapList.forEach(map -> {

            String scoreboard = (String) map.get("scoreboard");
            List<Map<String, Object>> mapPermissibles = (List<Map<String, Object>>) map.get("requirements");
            List<Permissible> permissibles = mapPermissibles == null ? new ArrayList<>() : manager.loadPermissible(mapPermissibles, "task-conditions", new File(getFolder(), "config.yml"));

            this.taskConditions.add(new TaskCondition(scoreboard, permissibles));
        });
    }

    private void registerEvents() {

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(this, this.plugin);

        List<String> events = essentialsScoreboards.stream().map(EssentialsScoreboard::getLines).flatMap(List::stream).map(ScoreboardLine::getEventName).filter(Objects::nonNull).distinct().toList();
        this.registerEvents(events);
    }

    @Override
    protected void updateEventPlayer(Player player, String eventName) {
        if (this.boards.containsKey(player.getUniqueId())) {
            PlayerBoard board = this.boards.get(player.getUniqueId());
            updatePlayerBoard(board, eventName);
        }
    }

    @Override
    protected void updateEventUniqueId(UUID uniqueId, String eventName) {
        if (this.boards.containsKey(uniqueId)) {
            PlayerBoard board = this.boards.get(uniqueId);
            updatePlayerBoard(board, eventName);
        }
    }

    private void updatePlayerBoard(PlayerBoard board, String eventName) {
        this.plugin.getScheduler().runNextTick(wrappedTask -> {
            EssentialsScoreboard essentialsScoreboard = board.getScoreboard();
            essentialsScoreboard.getLines().stream().filter(scoreboardLine -> scoreboardLine.getEventName() != null && scoreboardLine.getEventName().equals(eventName)).forEach(scoreboardLine -> scoreboardLine.update(board));
        });
    }

    @Override
    public void reloadPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerBoard board = this.boards.remove(player.getUniqueId());
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

        User user = plugin.getUser(event.getPlayer().getUniqueId());
        if (user != null && user.getOption(Option.DISABLE_SCOREBOARD)) return;

        Player player = event.getPlayer();
        EssentialsScoreboard essentialsScoreboard = getJoinScoreboard(player);
        this.createScoreboard(player, essentialsScoreboard);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.deleteBoard(event.getPlayer());
    }

    @Override
    public PlayerBoard createScoreboard(Player player, EssentialsScoreboard essentialsScoreboard) {

        PlayerBoard playerBoard = this.plugin.isPaperVersion() ? new ComponentBoard(player, essentialsScoreboard) : new ClassicBoard(player, essentialsScoreboard);
        this.boards.put(player.getUniqueId(), playerBoard);

        essentialsScoreboard.create(playerBoard);

        return playerBoard;
    }

    @Override
    public void deleteBoard(Player player) {
        PlayerBoard board = this.boards.remove(player.getUniqueId());
        if (board != null) board.delete();
    }

    @Override
    public Optional<PlayerBoard> getBoard(Player player) {
        return Optional.ofNullable(this.boards.get(player.getUniqueId()));
    }

    @Override
    public List<EssentialsScoreboard> getEssentialsScoreboard() {
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

    @Override
    public void toggleScoreboard(Player player, boolean silent) {

        User user = plugin.getUser(player.getUniqueId());

        if (this.boards.containsKey(player.getUniqueId())) {
            if (user != null) user.setOption(Option.DISABLE_SCOREBOARD, true);
            this.deleteBoard(player);
            if (!silent) {
                message(player, Message.SCOREBOARD_DISABLE);
            }
        } else {
            if (user != null) user.setOption(Option.DISABLE_SCOREBOARD, false);
            this.createScoreboard(player, getJoinScoreboard(player));
            if (!silent) {
                message(player, Message.SCOREBOARD_ENABLE);
            }
        }
    }

    @Override
    public EssentialsScoreboard getJoinScoreboard(Player player) {
        return this.joinConditions.stream().sorted(Comparator.comparingInt(JoinCondition::priority).reversed()).filter(joinCondition -> {
            return joinCondition.permissibles().isEmpty() || joinCondition.permissibles().stream().allMatch(permissible -> permissible.hasPermission(player, null, null, new Placeholders()));
        }).map(joinCondition -> getScoreboard(joinCondition.scoreboard())).filter(Optional::isPresent).map(Optional::get).findFirst().orElse(this.defaultScoreboard);
    }

    @Override
    public EssentialsScoreboard getTaskScoreboard(Player player) {
        return this.taskConditions.stream().filter(taskCondition -> {
            return taskCondition.permissibles().isEmpty() || taskCondition.permissibles().stream().allMatch(permissible -> permissible.hasPermission(player, null, null, new Placeholders()));
        }).map(joinCondition -> getScoreboard(joinCondition.scoreboard())).filter(Optional::isPresent).map(Optional::get).findFirst().orElse(this.defaultScoreboard);
    }

    private void updateScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            User user = plugin.getUser(player.getUniqueId());
            if (user != null && user.getOption(Option.DISABLE_SCOREBOARD)) continue;

            getBoard(player).ifPresent(playerBoard -> {

                EssentialsScoreboard essentialsScoreboard = getTaskScoreboard(player);
                if (playerBoard.getScoreboard() != essentialsScoreboard) {

                    deleteBoard(player);
                    createScoreboard(player, essentialsScoreboard);
                }
            });
        }
    }
}
