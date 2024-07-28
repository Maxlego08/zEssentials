package fr.maxlego08.essentials.api.vote;

public record VoteResetConfiguration(
        int day,
        int hour,
        int minute,
        int second
) {
}
