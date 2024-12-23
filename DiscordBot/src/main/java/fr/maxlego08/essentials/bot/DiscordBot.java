package fr.maxlego08.essentials.bot;

import fr.maxlego08.essentials.bot.command.CommandManager;
import fr.maxlego08.essentials.bot.config.Configuration;
import fr.maxlego08.essentials.bot.config.ConfigurationManager;
import fr.maxlego08.essentials.bot.link.LinkManager;
import fr.maxlego08.essentials.bot.listener.CommandListener;
import fr.maxlego08.essentials.bot.storage.StorageManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.Scanner;

public class DiscordBot {

    private final ConfigurationManager configurationManager;
    private final Configuration configuration;
    private final JDA jda;
    private final CommandManager commandManager;
    private final StorageManager storageManager;
    private final LinkManager linkManager;
    private boolean isRunning = true;
    private Scanner scanner;

    private DiscordBot() {

        this.configurationManager = new ConfigurationManager();
        this.configuration = new Configuration();
        this.configuration.loadConfiguration(configurationManager.getConfig());

        var builder = JDABuilder.createDefault(this.configuration.getBotToken())
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.GUILD_EXPRESSIONS)
                .enableIntents(GatewayIntent.DIRECT_MESSAGES)
                .enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .enableIntents(GatewayIntent.GUILD_MESSAGE_TYPING)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new CommandListener(this));
        this.jda = builder.build();

        this.commandManager = new CommandManager(this);

        this.storageManager = new StorageManager();
        this.storageManager.connect(this.configuration);

        this.linkManager = new LinkManager(this);

        this.shutdown();
    }

    public static void main(String[] args) {
        new DiscordBot();
    }

    private void shutdown() {
        // Add shutdown hook to disconnect the bot
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (this.jda != null) {
                this.jda.shutdown();
                System.out.println("Bot disconnected.");
            }
        }));

        // Scanner to listen for "stop" command
        this.scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            if (command.equalsIgnoreCase("stop")) {
                if (this.jda != null) {
                    this.jda.shutdownNow();
                    System.out.println("Bot stopped by user command.");
                }
                break;
            }
        }
    }

    public void forceShutdown() {
        this.isRunning = false;
        this.scanner.close();
        this.jda.shutdown();
        System.out.println("Bot disconnected.");
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public LinkManager getLinkManager() {
        return linkManager;
    }
}