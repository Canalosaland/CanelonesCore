package me.pk2.canalosaland.dependencies;

import me.clip.placeholderapi.PlaceholderAPI;
import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.dependencies.papi.PAPIExpansion;
import org.bukkit.entity.Player;

public class DependencyPAPI {
    public static final PAPIExpansion extension = new PAPIExpansion();
    public static boolean registered = false;
    public static void register() {
        registered = true;
        extension.register();

        CanelonesCore.INSTANCE.getLogger().info("PlaceholdersAPI registered!");
    }

    public static String parse(Player player, String text) {
        if(!registered)
            return text;
        return PlaceholderAPI.setPlaceholders(player, text);
    }
    public static String parse(String text) {
        return parse(null, text);
    }
}