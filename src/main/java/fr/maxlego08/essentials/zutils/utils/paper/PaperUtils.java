package fr.maxlego08.essentials.zutils.utils.paper;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.PrivateMessage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.BaseServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class PaperUtils extends BaseServer {

    public PaperUtils(EssentialsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void kick(Player player, Message message, Object... objects) {
        PaperComponent paperComponent = (PaperComponent) this.componentMessage;
        this.plugin.getScheduler().runAtLocation(player.getLocation(), wrappedTask -> player.kick(paperComponent.getComponentMessage(message, objects)));
    }

    @Override
    public void disallow(PlayerLoginEvent event, PlayerLoginEvent.Result result, Message message, Object... objects) {
        PaperComponent paperComponent = (PaperComponent) this.componentMessage;
        event.disallow(result, paperComponent.getComponentMessage(message, objects));
    }

    @Override
    public void sendPrivateMessage(User user, PrivateMessage privateMessage, Message message, String content) {
        PaperComponent paperComponent = (PaperComponent) this.componentMessage;
        Component component = paperComponent.getComponentMessage(message.getMessageAsString(), TagResolver.resolver("message", Tag.inserting(Component.text(content))), "%target%", privateMessage.username());
        user.getPlayer().sendMessage(component);
    }
}
