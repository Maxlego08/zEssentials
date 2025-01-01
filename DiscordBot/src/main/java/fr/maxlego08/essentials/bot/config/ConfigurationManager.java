package fr.maxlego08.essentials.bot.config;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ConfigurationManager {

    private final Path configPath;
    private final Yaml yaml;
    private Map<String, Object> config;

    public ConfigurationManager() {
        this.configPath = Path.of("config.yml");
        this.yaml = new Yaml();
        loadOrCreateConfig();
    }

    public void loadOrCreateConfig() {
        if (!Files.exists(configPath)) {
            createDefaultConfig();
        }
        loadConfig();
    }

    private void createDefaultConfig() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.yml")) {
            if (inputStream == null) {
                throw new IllegalStateException("Default config.yml file not found in resources!");
            }

            // Copy the content of the config.yml file from the JAR to the disk
            Files.copy(inputStream, configPath);
            System.out.println("Default config.yml file copied from the JAR.");
        } catch (IOException e) {
            throw new RuntimeException("Error while copying the default config.yml file.", e);
        }
    }

    private void loadConfig() {
        try (InputStream inputStream = Files.newInputStream(configPath)) {
            this.config = yaml.load(inputStream);
            if (this.config == null) {
                throw new IllegalArgumentException("The config.yml file is empty or invalid!");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading the config.yml file", e);
        }
    }

    public Map<String, Object> getConfig() {
        return config;
    }
}
