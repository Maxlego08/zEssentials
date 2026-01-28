package fr.maxlego08.essentials.api.config.models;

public record NearDirectionReplacements(
        String forwardReplacement,
        String forwardRightReplacement,
        String rightReplacement,
        String backRightReplacement,
        String backReplacement,
        String backLeftReplacement,
        String leftReplacement,
        String forwardLeftReplacement
) {
    public static final NearDirectionReplacements DEFAULT = new NearDirectionReplacements(
            "↑",
            "↗",
            "→",
            "↘",
            "↓",
            "↙",
            "←",
            "↖"
    );
}
