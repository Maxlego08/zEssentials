package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.home.HomeDisplay;
import fr.maxlego08.essentials.api.home.HomePermission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import org.apache.logging.log4j.util.Strings;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
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

    public void sendHomes(Player player, User user) {

        if (this.homeDisplay == HomeDisplay.IN_LINE || this.homeDisplay == HomeDisplay.MULTI_LINE) {

            int homeAmount = user.countHomes();
            int maxHome = getMaxHome(player);
            List<Home> homes = user.getHomes();

            if (this.homeDisplay == HomeDisplay.IN_LINE) {
                List<String> homesAsString = homes.stream().map(home -> getMessage(Message.COMMAND_HOME_INFORMATION_IN_LINE_INFO, formatHomeInformation(home, homeAmount, maxHome))).toList();
                message(player, Message.COMMAND_HOME_INFORMATION_IN_LINE, "%homes%", Strings.join(homesAsString, ','), "%count%", homeAmount, "%max%", maxHome);
            } else {
                message(player, Message.COMMAND_HOME_INFORMATION_MULTI_LINE_HEADER, "%count%", homeAmount, "%max%", maxHome);
                homes.forEach(home -> message(player, Message.COMMAND_HOME_INFORMATION_MULTI_LINE_CONTENT, formatHomeInformation(home, homeAmount, maxHome)));
                message(player, Message.COMMAND_HOME_INFORMATION_MULTI_LINE_FOOTER, "%count%", homeAmount, "%max%", maxHome);
            }
        }
    }

    private Object[] formatHomeInformation(Home home, int homeAmount, int maxHome) {
        Location location = home.getLocation();
        World world = location.getWorld();
        return new Object[]{
                "%count%", homeAmount,
                "%max%", maxHome,
                "%name%", home.getName(),
                "%world%", name(world.getName()),
                "%environment%", name(world.getEnvironment().name()),
                "%x%", location.getBlockX(),
                "%y%", location.getBlockY(),
                "%z%", location.getBlockZ()
        };
    }
}
