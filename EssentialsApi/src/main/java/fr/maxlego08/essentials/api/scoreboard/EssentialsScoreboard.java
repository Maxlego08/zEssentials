package fr.maxlego08.essentials.api.scoreboard;

import java.util.List;

public interface EssentialsScoreboard {

    String getName();

    boolean isDefault();

    String getTitle();

    List<ScoreboardLine> getLines();

    void create(PlayerBoard playerBoard);
}
