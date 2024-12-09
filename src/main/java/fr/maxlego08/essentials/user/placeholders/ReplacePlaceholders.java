package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.Configuration;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.configuration.ReplacePlaceholder;
import fr.maxlego08.essentials.api.messages.MessageHelper;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.zutils.utils.ZUtils;

import java.util.ArrayList;
import java.util.List;
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

            List<String> values = splitIgnoringBraces(args).stream().map(e -> e.replace("{", "%").replace("}", "%")).toList();
            if (values.size() != 3) return "The format is invalid! Please try again";

            try {
                String start = PapiHelper.papi(values.get(0), player);
                String end = PapiHelper.papi(values.get(1), player);
                int size = Integer.parseInt(values.get(2));
                return MessageHelper.getFormattedString(start, end, size);
            } catch (Exception exception) {
                return "The format is invalid! Please try again";
            }
        }, "Transforms two placeholders to add space between them. This allows to create texts that will have the same space between the name of the player and his score for example.", "first text", "second text", "text length");

        placeholder.register("progressbar_", (player, args) -> {

            List<String> values = splitIgnoringBraces(args).stream().map(e -> e.replace("{", "%").replace("}", "%")).toList();
            if (values.size() != 6) return "The format is invalid! Please try again";

            try {
                long start = Long.parseLong(PapiHelper.papi(values.get(0), player));
                long end = Long.parseLong(PapiHelper.papi(values.get(1), player));
                int totalBar = Integer.parseInt(PapiHelper.papi(values.get(2), player));
                char symbol = values.get(3).charAt(0);
                String completedColor = PapiHelper.papi(values.get(4), player);
                String notCompletedColor = PapiHelper.papi(values.get(5), player);
                return getProgressBar(start, end, totalBar, symbol, completedColor, notCompletedColor);
            } catch (Exception exception) {
                return "The format is invalid! Please try again";
            }
        }, "Allows to transform two numbers into a progressbar, you can use placeholders", "current", "max", "size", "symbol", "completedColor", "notCompletedColor");
    }

    public List<String> splitIgnoringBraces(String input) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideBraces = false;

        for (char c : input.toCharArray()) {
            if (c == '{') {
                insideBraces = true;
            } else if (c == '}') {
                insideBraces = false;
            }

            if (c == '_' && !insideBraces) {
                if (!current.isEmpty()) {
                    result.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }

        if (!current.isEmpty()) {
            result.add(current.toString());
        }

        return result;
    }

}
