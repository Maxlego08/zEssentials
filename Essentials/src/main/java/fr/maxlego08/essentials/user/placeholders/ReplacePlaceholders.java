package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.Configuration;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.configuration.ReplacePlaceholder;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.zutils.utils.ZUtils;

import java.util.Optional;

public class ReplacePlaceholders extends ZUtils implements PlaceholderRegister {

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {
        Configuration configuration = plugin.getConfiguration();

        placeholder.register("replace_", (player, placeholderValue) -> {

            Optional<ReplacePlaceholder> optional = configuration.getReplacePlaceholder(placeholderValue);
            if (optional.isEmpty()) return placeholderValue + " was not found in config.yml";

            ReplacePlaceholder replacePlaceholder = optional.get();
            String result = papi("%" + placeholderValue + "%", player);
            return replacePlaceholder.replace(result);
        }, "Replace the value of one placeholder with another", "placeholder");
    }
}
