package fr.maxlego08.essentials.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.messages.MessageUtils;
import fr.maxlego08.essentials.api.packet.PacketRegister;
import fr.maxlego08.essentials.api.utils.component.AdventureComponent;
import fr.maxlego08.essentials.protocollib.component.AbstractComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PacketChatListener extends PacketAdapter implements PacketRegister {

    private final EssentialsPlugin plugin;
    private final String result;
    private final Pattern pattern = Pattern.compile("\\./(.*?)(\\.|$)");

    public PacketChatListener(EssentialsPlugin plugin, String result) {
        super(PacketAdapter.params().plugin(plugin).listenerPriority(ListenerPriority.HIGHEST).types(PacketType.Play.Server.SYSTEM_CHAT));
        this.plugin = plugin;
        this.result = result;
    }

    @Override
    public void onPacketSending(PacketEvent event) {

        var packet = event.getPacket();

        // Check is message is an action bar
        var isActionBar = packet.getModifier().read(1);
        if (isActionBar instanceof Boolean asBoolean && asBoolean) return;

        var wrappedChatComponent = packet.getChatComponents().readSafely(0);
        if (wrappedChatComponent == null) return; // Do nothing

        String json = wrappedChatComponent.getJson();

        String serializedMessage = AbstractComponent.parse(json).toMiniMessage();

        Matcher matcher = PacketChatListener.this.pattern.matcher(serializedMessage);
        if (matcher.find()) {
            var newComponent = fixClickEvent(parseComponent(serializedMessage));
            packet.getChatComponents().write(0, WrappedChatComponent.fromJson(GsonComponentSerializer.gson().serialize(newComponent)));
        }
    }

    @Override
    public void addPacketListener() {

        if (this.result == null) {
            this.plugin.getLogger().severe("Cannot use ProtocolLib, your configuration command-placeholder.result is invalid !");
            return;
        }

        this.plugin.getLogger().info("Start listening packet System Chat");
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    private Component parseComponent(String message) {

        AdventureComponent adventureComponent = (AdventureComponent) this.plugin.getComponentMessage();
        TagResolver.Builder builder = TagResolver.builder();

        Matcher matcher = this.pattern.matcher(message);
        StringBuilder formattedMessage = new StringBuilder();

        while (matcher.find()) {
            String command = matcher.group(1); // Get the content between ./ and . or end of string

            String placeholderTag = MessageUtils.removeNonAlphanumeric("cmd_" + command.replace(" ", "_"));
            builder.resolver(Placeholder.component(placeholderTag, adventureComponent.getComponent(
                    result.replace("%command%", command).replace("%fixed_command%", command.replace("'", "\\'"))
            )));
            matcher.appendReplacement(formattedMessage, "<" + placeholderTag + ">");
        }

        matcher.appendTail(formattedMessage);

        return adventureComponent.getComponent(formattedMessage.toString(), builder.build());
    }

    /**
     * Fixes the click event of a component by removing a specific prefix from the event value.
     *
     * <p>This method processes the given component and its children recursively to ensure that
     * if a click event's value starts with "&f", this prefix is removed.</p>
     *
     * @param component The component to be processed.
     * @return A new component with the corrected click event values.
     */
    private Component fixClickEvent(final Component component) {
        final ClickEvent event = component.clickEvent();
        Component copied = component;

        if (event != null && event.value().startsWith("&f")) {
            copied = component.clickEvent(ClickEvent.clickEvent(event.action(), event.value().substring(2)));
        }

        copied = copied.children(copied.children().stream().map(this::fixClickEvent).collect(Collectors.toList()));
        return copied;
    }
}
