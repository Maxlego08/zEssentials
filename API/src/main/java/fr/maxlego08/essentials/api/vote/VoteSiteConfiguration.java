package fr.maxlego08.essentials.api.vote;

import fr.maxlego08.essentials.api.modules.Loadable;

public record VoteSiteConfiguration(String name, int seconds) implements Loadable {
}
