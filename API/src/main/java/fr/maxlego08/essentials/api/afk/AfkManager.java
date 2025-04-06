package fr.maxlego08.essentials.api.afk;

import fr.maxlego08.essentials.api.user.User;
import org.bukkit.permissions.Permissible;

import java.util.Optional;

public interface AfkManager {

    void checkUser(User user);

    Optional<AfkPermission> getPermission(Permissible permissible);

}
