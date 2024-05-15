package fr.maxlego08.essentials.api.scoreboard;

public interface ScoreboardLine {

    int getLine();

    String getText();

    ScoreboardAnimationType getAnimation();

    void startAnimation(PlayerBoard playerBoard);

    String getEventName();

    void update(PlayerBoard board);
}
