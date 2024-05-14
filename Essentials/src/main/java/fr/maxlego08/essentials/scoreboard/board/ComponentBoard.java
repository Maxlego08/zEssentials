package fr.maxlego08.essentials.scoreboard.board;

import fr.maxlego08.essentials.api.scoreboard.EssentialsScoreboard;
import fr.maxlego08.essentials.api.scoreboard.PlayerBoard;
import fr.maxlego08.essentials.zutils.utils.paper.PaperComponent;
import fr.mrmicky.fastboard.adventure.FastBoard;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ComponentBoard extends FastBoard implements PlayerBoard {

    private final EssentialsScoreboard essentialsScoreboard;
    private final PaperComponent paperComponent = new PaperComponent();

    public ComponentBoard(Player player, EssentialsScoreboard essentialsScoreboard) {
        super(player);
        this.essentialsScoreboard = essentialsScoreboard;
    }

    @Override
    public void updateTitle(String title) {
        this.updateTitle(this.color(title));
    }

    @Override
    public void updateLine(int line, String text) {
        this.updateLine(line, this.color(text));
    }

    @Override
    public void updateLines(String... lines) {
        updateLines(Arrays.stream(lines).map(this::color).toList());
    }

    @Override
    public void updateLines(List<String> lines) {
        updateLines(lines.stream().map(this::color).toList());
    }

    private Component color(String text) {
        return this.paperComponent.getComponent(text);
    }

    @Override
    public EssentialsScoreboard getScoreboard() {
        return this.essentialsScoreboard;
    }
}
