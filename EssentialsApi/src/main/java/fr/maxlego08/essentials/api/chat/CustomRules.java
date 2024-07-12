package fr.maxlego08.essentials.api.chat;

import fr.maxlego08.essentials.api.modules.Loadable;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public record CustomRules(Pattern pattern, String permission, String message) implements Loadable {

    public boolean isNotValid() {
        return this.pattern == null || this.message == null;
    }

    public boolean match(Player player, String string) {
        return (this.permission == null || player.hasPermission(this.permission)) && (this.pattern != null && this.pattern.matcher(string).find());
    }
}
