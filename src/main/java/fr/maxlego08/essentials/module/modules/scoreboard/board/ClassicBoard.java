package fr.maxlego08.essentials.module.modules.scoreboard.board;

import fr.maxlego08.essentials.api.scoreboard.EssentialsScoreboard;
import fr.maxlego08.essentials.api.scoreboard.PlayerBoard;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassicBoard extends FastBoard implements PlayerBoard {


    private final EssentialsScoreboard essentialsScoreboard;
    private final Map<Integer, Integer> linesModifier = new HashMap<>();

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

    @Override
    public Map<Integer, Integer> getLinesModifier() {
        return this.linesModifier;
    }

    @Override
    public int getLineModifier(int line) {
        return this.linesModifier.getOrDefault(line, line);
    }
}
