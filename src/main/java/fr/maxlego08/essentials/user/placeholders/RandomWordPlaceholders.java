package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.api.utils.RandomWord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class RandomWordPlaceholders implements RandomWord, PlaceholderRegister {

    private final Map<UUID, String> words = new HashMap<>();
    private final List<String> alreadyGivenWords = new ArrayList<>();

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {
        placeholder.register("random_word", (player) -> {

            var words = new ArrayList<>(plugin.getConfiguration().getRandomWords());
            Collections.shuffle(words);

            String word = null;
            for (String w : words) {
                if (!this.alreadyGivenWords.contains(w)) {
                    word = w;
                    break;
                }
            }

            if (word == null) return "No available random word found.";

            this.alreadyGivenWords.add(word);

            return word;

        }, "Generate a random word for the player, a word can only be generated once, you must delete the list of words generated with the command/ess clear-random-word");
    }

    @Override
    public void clear() {
        this.words.clear();
        this.alreadyGivenWords.clear();
    }

    @Override
    public Optional<String> get(UUID uuid) {
        return Optional.ofNullable(this.words.get(uuid));
    }

    @Override
    public void set(UUID uuid, String word) {
        this.words.put(uuid, word);
    }

}
