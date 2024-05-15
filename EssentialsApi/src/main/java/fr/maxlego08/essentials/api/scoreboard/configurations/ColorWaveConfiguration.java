package fr.maxlego08.essentials.api.scoreboard.configurations;

import fr.maxlego08.essentials.api.scoreboard.AnimationConfiguration;

public record ColorWaveConfiguration(String fromColor, String toColor, int length, int delayBetween, int animationSpeed) implements AnimationConfiguration {
}
