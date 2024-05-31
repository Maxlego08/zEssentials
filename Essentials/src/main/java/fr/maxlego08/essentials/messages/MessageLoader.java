package fr.maxlego08.essentials.messages;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.ConfigurationFile;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.messages.MessageType;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

public class MessageLoader implements ConfigurationFile {

    private final Locale locale = Locale.getDefault();
    private final ZEssentialsPlugin plugin;
    private final List<Message> loadedMessages = new ArrayList<>();

    public MessageLoader(ZEssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {

        File file = new File(plugin.getDataFolder(), "messages.yml");
        this.copyFile(file);

        this.loadMessages(YamlConfiguration.loadConfiguration(file));

        if (this.loadedMessages.size() != Message.values().length) {
            this.plugin.getLogger().log(Level.SEVERE, "Messages were not loaded correctly.");
            for (Message value : Message.values()) {
                if (!this.loadedMessages.contains(value)) {
                    this.plugin.getLogger().log(Level.SEVERE, value + " was not loaded.");
                }
            }
        }
    }

    private void copyFile(File file) {

        String messageFileName = "messages";
        String localMessageName = "messages_" + locale.getLanguage();
        if (this.plugin.resourceExist("messages/" + localMessageName + ".yml")) {
            messageFileName = localMessageName;
        }

        this.plugin.saveOrUpdateConfiguration("messages/" + messageFileName + ".yml", "messages.yml");
    }

    private void loadMessages(YamlConfiguration configuration) {

        this.loadedMessages.clear();

        for (String key : configuration.getKeys(false)) {

            String messageKey = key.replace("-", "_").toUpperCase();
            try {

                Message message = Message.valueOf(messageKey);

                if (configuration.contains(key + ".type")) {

                    MessageType messageType = MessageType.valueOf(configuration.getString(key + ".type", "TCHAT").toUpperCase());
                    message.setMessageType(messageType);
                    switch (messageType) {
                        case ACTION, TCHAT_AND_ACTION -> {
                            message.setMessage(replaceMessagesColors(configuration.getString(key + ".message")));
                        }
                        case CENTER, TCHAT, WITHOUT_PREFIX -> {
                            List<String> messages = replaceMessagesColors(configuration.getStringList(key + ".messages"));
                            if (messages.isEmpty()) {
                                message.setMessage(replaceMessagesColors(configuration.getString(key + ".message")));
                            } else message.setMessages(replaceMessagesColors(messages));
                        }
                    }

                } else {
                    message.setMessageType(MessageType.TCHAT);
                    List<String> messages = configuration.getStringList(key);
                    if (messages.isEmpty()) {
                        message.setMessage(replaceMessagesColors(configuration.getString(key)));
                    } else message.setMessages(replaceMessagesColors(messages));
                }

                this.loadedMessages.add(message);

            } catch (Exception exception) {
                exception.printStackTrace();
                this.plugin.getLogger().log(Level.SEVERE, messageKey + " key was not found !");
            }
        }
    }

    private String replaceMessagesColors(String message) {
        return this.plugin.getConfiguration().getMessageColors().stream().reduce(message, (msg, color) -> msg.replace(color.key(), color.color()), (msg1, msg2) -> msg1);
    }

    private List<String> replaceMessagesColors(List<String> messages) {
        return messages.stream().map(this::replaceMessagesColors).toList();
    }


}
