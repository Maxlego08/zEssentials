package fr.maxlego08.essentials.scoreboard.board;

import fr.maxlego08.essentials.api.scoreboard.EssentialsScoreboard;
import fr.maxlego08.essentials.api.scoreboard.PlayerBoard;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.entity.Player;

import java.util.List;

public class ClassicBoard extends FastBoard implements PlayerBoard {


    private final EssentialsScoreboard essentialsScoreboard;

    public ClassicBoard(Player player, EssentialsScoreboard essentialsScoreboard) {
        super(player);
        this.essentialsScoreboard = essentialsScoreboard;
    }

    @Override
    public void updateLines(List<String> lines) {
        super.updateLines(lines);
    }

    @Override
    public EssentialsScoreboard getScoreboard() {
        return this.essentialsScoreboard;
    }
}
