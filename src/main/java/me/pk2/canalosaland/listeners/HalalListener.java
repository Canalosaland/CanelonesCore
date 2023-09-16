package me.pk2.canalosaland.listeners;

import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import static me.pk2.canalosaland.util.Wrapper.*;

public class HalalListener implements Listener {
    public static void _EXECUTE_ACTION(Player player, String actLocale) {
        for(String action : ConfigMainBuffer.buffer.halal_mode.action) {
            if(action.contentEquals("notify-everyone")) {
                for(Player p : Bukkit.getOnlinePlayers())
                    p.sendMessage(_COLOR(ConfigMainBuffer.buffer.server_prefix) +
                            _SENDER_TRANSLATE(p, "LISTENER_HALAL_NOTIFICATION")
                                .replace("%user%", player.getName())
                                .replace("%action%", _SENDER_TRANSLATE(p, actLocale)));
                continue;
            }
            action = action.replace("%player%", player.getName());
            action = action.replace("%action%", _SENDER_TRANSLATE(player, actLocale));

            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), action);
        }
    }

    public static boolean _IS_FOOD(Material material) {
        return switch (material) {
            case    APPLE,
                    MUSHROOM_STEW,
                    BREAD,
                    PORKCHOP,
                    COOKED_PORKCHOP,
                    GOLDEN_APPLE,
                    ENCHANTED_GOLDEN_APPLE,
                    COD,
                    SALMON,
                    TROPICAL_FISH,
                    PUFFERFISH,
                    COOKED_COD,
                    COOKED_SALMON,
                    CAKE,
                    COOKIE,
                    MELON_SLICE,
                    DRIED_KELP,
                    BEEF,
                    COOKED_BEEF,
                    CHICKEN,
                    COOKED_CHICKEN,
                    ROTTEN_FLESH,
                    SPIDER_EYE,
                    CARROT,
                    POTATO,
                    BAKED_POTATO,
                    POISONOUS_POTATO,
                    PUMPKIN_PIE,
                    RABBIT,
                    COOKED_RABBIT,
                    RABBIT_STEW,
                    MUTTON,
                    COOKED_MUTTON,
                    BEETROOT,
                    BEETROOT_SOUP,
                    SWEET_BERRIES,
                    HONEY_BOTTLE // If ever updated to newer versions, add new food items here
                    -> true;
            default -> false;
        };
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        if(!ConfigMainBuffer.buffer.halal_mode.enabled)
            return;

        if((event.getItem().getType() == org.bukkit.Material.PORKCHOP || event.getItem().getType() == org.bukkit.Material.COOKED_PORKCHOP)
                && ConfigMainBuffer.buffer.halal_mode.activate.pork)
            _EXECUTE_ACTION(event.getPlayer(), "LISTENER_HALAL_ACTION_PORK"); // Suffer the consequences
        else if(_IS_FOOD(event.getItem().getType())
                && ConfigMainBuffer.buffer.halal_mode.activate.day_food
                && event.getPlayer().getWorld().getTime()<13000)
            _EXECUTE_ACTION(event.getPlayer(), "LISTENER_HALAL_ACTION_DAYTIME"); // Suffer the consequences
    }
}