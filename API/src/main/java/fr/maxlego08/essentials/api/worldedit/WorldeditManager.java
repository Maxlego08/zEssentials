package fr.maxlego08.essentials.api.worldedit;

import fr.maxlego08.essentials.api.user.User;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public interface WorldeditManager {

    Optional<WorldEditItem> getWorldeditItem(String name);

    void give(CommandSender sender, Player player, String itemName);

    List<String> getWorldeditItems();

    List<String> getAllowedMaterials();

    boolean isBlacklist(Material material);

    void setBlocks(User user, Material material);
}
