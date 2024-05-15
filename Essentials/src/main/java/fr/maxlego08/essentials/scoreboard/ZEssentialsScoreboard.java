package fr.maxlego08.essentials.scoreboard;

import fr.maxlego08.essentials.api.scoreboard.EssentialsScoreboard;
import fr.maxlego08.essentials.api.scoreboard.PlayerBoard;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardAnimationType;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardLine;
import fr.maxlego08.essentials.api.scoreboard.configurations.ColorWaveConfiguration;
import fr.maxlego08.essentials.api.scoreboard.configurations.NoneConfiguration;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

            TypedMapAccessor accessor = new TypedMapAccessor((Map<String, Object>) currentLine);

            int line = accessor.getInt("line") - 1;
            String eventName = currentLine.containsKey("event") ? accessor.getString("event") : null;
            String text = accessor.getString("text", "text not found for line " + line);
            ScoreboardAnimationType animationType = ScoreboardAnimationType.valueOf(accessor.getString("animation", ScoreboardAnimationType.NONE.name()));

            switch (animationType) {
                case COLOR_WAVE -> {
                    String fromColor = accessor.getString("fromColor");
                    String toColor = accessor.getString("toColor");
                    int length = accessor.getInt("length", text.length());
                    int delayBetween = accessor.getInt("delayBetween", 5000);
                    int animationSpeed = accessor.getInt("animationSpeed", 25);
                    this.lines.add(new ZScoreboardLine(line, text, animationType, new ColorWaveConfiguration(fromColor, toColor, length, delayBetween, animationSpeed)));
                }
                case NONE -> {
                    int update = accessor.getInt("update", 0);
                    if (update <= 0) {
                        this.lines.add(new ZScoreboardLine(line, text, eventName));
                    } else {
                        this.lines.add(new ZScoreboardLine(line, text, animationType, new NoneConfiguration(update)));
                    }
                }
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
