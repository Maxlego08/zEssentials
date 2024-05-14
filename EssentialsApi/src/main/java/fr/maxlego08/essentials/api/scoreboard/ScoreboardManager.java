package fr.maxlego08.essentials.api.scoreboard;

import fr.maxlego08.essentials.api.modules.Module;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public interface ScoreboardManager extends Module {

    List<EssentialsScoreboard> getEssentialsScoreboards();

    EssentialsScoreboard getDefaultScoreboard();

    PlayerBoard createScoreboard(Player player, EssentialsScoreboard essentialsScoreboard);

    void deleteBoard(Player player);

    Optional<PlayerBoard> getBoard(Player player);

}
