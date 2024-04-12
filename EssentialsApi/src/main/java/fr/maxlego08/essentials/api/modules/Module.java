package fr.maxlego08.essentials.api.modules;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.io.File;

public interface Module extends Listener {

    String getName();

    void loadConfiguration();

    File getFolder();

    YamlConfiguration getConfiguration();

    boolean isEnable();

    boolean isRegisterEvent();


}
