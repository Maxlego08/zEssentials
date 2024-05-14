package fr.maxlego08.essentials.api.scoreboard;

public interface ScoreboardLine {

    int getLine();

    String getText();

    ScoreboardAnimation getAnimation();

    void startAnimation(PlayerBoard playerBoard);
}
