package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.home.HomeDisplay;
import fr.maxlego08.essentials.api.home.HomePermission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.ZModule;
import org.bukkit.permissions.Permissible;

import java.util.ArrayList;
import java.util.List;

public class HomeModule extends ZModule {

    private final List<HomePermission> permissions = new ArrayList<>();

    private HomeDisplay homeDisplay;
    private String homeRegex;
    private int homeNameMax;
    private int homeNameMin;

    public HomeModule(ZEssentialsPlugin plugin) {
        super(plugin, "home");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        System.out.println(this.permissions);
    }

    public List<HomePermission> getPermissions() {
        return permissions;
    }

    public Message isValidHomeName(String input) {
        if (!input.matches("[a-zA-Z0-9]+")) {
            return Message.COMMAND_SET_HOME_INVALIDE_NAME;
        }
        if (input.length() > this.homeNameMax) {
            return Message.COMMAND_SET_HOME_TOO_LONG;
        }
        if (input.length() < this.homeNameMin) {
            return Message.COMMAND_SET_HOME_TOO_SHORT;
        }
        return null;
    }

    public int getMaxHome(Permissible permissible) {
        return this.permissions.stream().filter(homePermission -> permissible.hasPermission(homePermission.permission())).mapToInt(HomePermission::amount).max().orElse(0);
    }
}
