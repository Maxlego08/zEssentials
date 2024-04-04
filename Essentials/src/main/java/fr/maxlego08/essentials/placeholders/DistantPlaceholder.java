package fr.maxlego08.essentials.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class DistantPlaceholder extends PlaceholderExpansion {

    private final EssentialsPlugin plugin;
    private final Placeholder placeholder;

    public DistantPlaceholder(EssentialsPlugin plugin, Placeholder placeholder) {
        this.plugin = plugin;
        this.placeholder = placeholder;
    }

    @Override
    public String getAuthor() {
        return this.plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public String getIdentifier() {
        return this.placeholder.getPrefix();
    }

    @Override
    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        return this.placeholder.onRequest(player, params);
    }

}
