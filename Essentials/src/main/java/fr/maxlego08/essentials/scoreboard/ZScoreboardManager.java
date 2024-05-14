package fr.maxlego08.essentials.scoreboard;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.scoreboard.EssentialsScoreboard;
import fr.maxlego08.essentials.api.scoreboard.PlayerBoard;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardManager;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.scoreboard.board.ClassicBoard;
import fr.maxlego08.essentials.scoreboard.board.ComponentBoard;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
}
