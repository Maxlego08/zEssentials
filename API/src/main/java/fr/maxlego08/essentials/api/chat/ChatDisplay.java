package fr.maxlego08.essentials.api.chat;

import fr.maxlego08.essentials.api.utils.component.AdventureComponent;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

public interface ChatDisplay {

    String display(AdventureComponent adventureComponent, TagResolver.Builder builder, Player sender, Player receiver, String message);

    boolean hasPermission(Permissible permissible);
}
