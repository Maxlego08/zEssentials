package fr.maxlego08.essentials.scoreboard;

import fr.maxlego08.essentials.api.scoreboard.EssentialsScoreboard;
import fr.maxlego08.essentials.api.scoreboard.PlayerBoard;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardAnimation;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardLine;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ZEssentialsScoreboard extends ZUtils implements EssentialsScoreboard {
    private final String name;
    private final boolean isDefault;
    private final String title;
    private final List<ScoreboardLine> lines = new ArrayList<>();

    public ZEssentialsScoreboard(String name, boolean isDefault, String title, List<ScoreboardLine> lines) {
        this.name = name;
        this.isDefault = isDefault;
        this.title = title;
    }

    public ZEssentialsScoreboard(String scoreboardName, ConfigurationSection configurationSection) {
        this.name = scoreboardName;
        this.isDefault = configurationSection.getBoolean("default", false);
        this.title = configurationSection.getString("title", "");
        configurationSection.getMapList("lines").forEach(currentLine -> {

            int line = ((Number) currentLine.get("line")).intValue() - 1;
            String text = (String) currentLine.get("text");
            ScoreboardAnimation animationValue = currentLine.containsKey("animation") ? ScoreboardAnimation.valueOf((String) currentLine.get("animation")) : ScoreboardAnimation.NONE;

            switch (animationValue) {
                case COLOR -> {
                    String fromColor = (String) currentLine.get("fromColor");
                    String toColor = (String) currentLine.get("toColor");
                    this.lines.add(new ZScoreboardLine(line, text, fromColor, toColor));
                }
                case NONE -> this.lines.add(new ZScoreboardLine(line, text));
            }
        });
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isDefault() {
        return this.isDefault;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public List<ScoreboardLine> getLines() {
        return new ArrayList<>(this.lines);
    }

    @Override
    public void create(PlayerBoard playerBoard) {
        Player player = playerBoard.getPlayer();

        playerBoard.updateTitle(papi(this.title, player));

        List<String> lines = this.lines.stream().map(ScoreboardLine::getText).map(line -> papi(line, player)).toList();
        playerBoard.updateLines(lines);

        this.lines.forEach(line -> line.startAnimation(playerBoard));
    }
}
