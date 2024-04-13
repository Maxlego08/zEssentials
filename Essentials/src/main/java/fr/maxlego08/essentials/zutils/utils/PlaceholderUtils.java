package fr.maxlego08.essentials.zutils.utils;

import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.stream.Collectors;

public abstract class PlaceholderUtils extends LocationUtils{

    protected String papi(String placeHolder, OfflinePlayer player) {
        return placeHolder;
    }

    protected List<String> papi(List<String> placeHolders, OfflinePlayer player) {
        if (player == null) return placeHolders;
        return placeHolders.stream().map(placeHolder -> papi(placeHolder, player)).collect(Collectors.toList());
    }

}
