package fr.maxlego08.essentials.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.messages.MessageUtils;
import fr.maxlego08.essentials.api.utils.component.AdventureComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageOut {

    private final EssentialsPlugin plugin;
    private final String result;
    private final Pattern pattern = Pattern.compile("\\./(.*?)(\\.|$)");

    public MessageOut(EssentialsPlugin plugin, String result) {
        this.plugin = plugin;
        this.result = result;
    }

    public void addPacketListener() {

        if (this.result == null){
        this.plugin.getLogger().severe("Cannot use ProtocolLib, your configuration command-placeholder.result is invalid !");
            return;
        }

        this.plugin.getLogger().info("Start listening packet System Chat");
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(PacketAdapter.params().plugin(this.plugin).listenerPriority(ListenerPriority.HIGHEST).types(PacketType.Play.Server.SYSTEM_CHAT)) {

            @Override
            public void onPacketSending(PacketEvent event) {

                var packet = event.getPacket();
                var isActionBar = packet.getModifier().read(1);
                if (isActionBar instanceof Boolean asBoolean && asBoolean) return;

                var message = packet.getChatComponents().read(0);
                String json = message.getJson();

                Component component = JSONComponentSerializer.json().deserialize(json);
                String serializedMessage = LegacyComponentSerializer.legacyAmpersand().serialize(component).toLowerCase();

                Matcher matcher = MessageOut.this.pattern.matcher(serializedMessage);
                if (matcher.find()) {
                    event.setCancelled(true);
                    sendNewMessage(event.getPlayer(), colorReverse(serializedMessage));
                }
            }
        });
    }

    private void sendNewMessage(Player player, String message) {

        AdventureComponent adventureComponent = (AdventureComponent) this.plugin.getComponentMessage();
        TagResolver.Builder builder = TagResolver.builder();

        Matcher matcher = this.pattern.matcher(message);
        StringBuilder formattedMessage = new StringBuilder();

        while (matcher.find()) {
            String command = matcher.group(1); // Get the content between ./ and . or end of string

            String placeholderTag = MessageUtils.removeNonAlphanumeric("cmd_" + command.replace(" ", "_"));
            builder.resolver(Placeholder.component(placeholderTag, adventureComponent.getComponent(result.replace("%command%", command))));
            matcher.appendReplacement(formattedMessage, "<" + placeholderTag + ">");
        }

        matcher.appendTail(formattedMessage);

        Component component = adventureComponent.getComponent(formattedMessage.toString(), builder.build());
        player.sendMessage(component);
    }

    protected String colorReverse(String message) {

        if (message == null) return null;

        Pattern pattern = Pattern.compile("&x[a-fA-F0-9-&]{12}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            String colorReplace = color.replace("&x", "#");
            colorReplace = colorReplace.replace("&", "");
            message = message.replace(color, colorReplace);
            matcher = pattern.matcher(message);
        }

        return message;
    }
}
