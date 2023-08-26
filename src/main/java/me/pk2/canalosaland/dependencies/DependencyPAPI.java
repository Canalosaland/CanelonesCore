package me.pk2.canalosaland.dependencies;

import me.clip.placeholderapi.PlaceholderAPI;
import me.pk2.canalosaland.CanelonesCore;
import org.bukkit.entity.Player;

public class DependencyPAPI {
    public static boolean registered = false;
    public static void register() {
        registered = true;

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