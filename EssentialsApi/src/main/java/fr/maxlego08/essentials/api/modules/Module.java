package fr.maxlego08.essentials.api.modules;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public interface Module {

    String getName();

    void loadConfiguration();

    File getFolder();

    YamlConfiguration getConfiguration();

    boolean isEnable();


}
