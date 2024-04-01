package fr.maxlego08.essentials.messages;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.ConfigurationFile;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.messages.MessageType;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class MessageLoader implements ConfigurationFile {

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
        if (!file.exists()) {

            // ToDo, detect the server country

            this.plugin.saveResource("messages/messages.yml", "messages.yml", false);
        }
    }

    private void loadMessages(YamlConfiguration configuration) {

        this.loadedMessages.clear();

        for (String key : configuration.getKeys(false)) {

            String messageKey = key.replace("-", "_").toUpperCase();
            try {

                Message message = Message.valueOf(messageKey);
                if (configuration.contains(key + ".type")) {

                    MessageType messageType = MessageType.valueOf(configuration.getString(key + ".type", "TCHAT").toUpperCase());
                    switch (messageType) {
                        case ACTION -> {
                            message.setMessage(configuration.getString(key + ".message"));
                        }
                        case CENTER, TCHAT -> {
                            List<String> messages = configuration.getStringList(key + ".messages");
                            if (messages.isEmpty()) {
                                message.setMessage(configuration.getString(key + "message"));
                            } else message.setMessages(messages);
                        }
                    }

                } else {

                    List<String> messages = configuration.getStringList(key);
                    if (messages.isEmpty()) {
                        message.setMessage(configuration.getString(key));
                    } else message.setMessages(messages);
                }

                this.loadedMessages.add(message);

            } catch (Exception exception) {
                exception.printStackTrace();
                this.plugin.getLogger().log(Level.SEVERE, messageKey + " key was not found !");
            }
        }
    }
}
