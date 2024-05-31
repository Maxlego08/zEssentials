package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.Configuration;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.configuration.ReplacePlaceholder;
import fr.maxlego08.essentials.api.messages.MessageHelper;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.zutils.utils.ZUtils;

import java.util.Arrays;
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

        placeholder.register("center_", (player, args) -> {

            String[] values = args.split("\\|");
            System.out.println(args + " - " + Arrays.asList(values));
            if (values.length != 3) return "The format is invalid! Please try again";

            try {
                String start = PapiHelper.papi(values[0].replaceAll("::", "%"), player);
                String end = PapiHelper.papi(values[1].replaceAll("::", "%"), player);
                int size = Integer.parseInt(values[2]);
                return MessageHelper.getFormattedString(start, end, size);
            } catch (Exception exception) {
                return "The format is invalid! Please try again";
            }
        }, "Transforms two placeholders to add space between them. This allows to create texts that will have the same space between the name of the player and his score for example.", "arguments");
    }
}
