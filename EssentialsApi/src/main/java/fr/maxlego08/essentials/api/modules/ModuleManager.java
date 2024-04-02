package fr.maxlego08.essentials.api.modules;

import java.io.File;

public interface ModuleManager {

    void loadModules();

    void loadConfigurations();

    <T extends Module> T getModule(Class<T> module);

    File getFolder();

}
