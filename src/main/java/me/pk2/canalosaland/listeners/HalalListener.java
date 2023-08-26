package me.pk2.canalosaland.listeners;

import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class HalalListener implements Listener {
    public static void _EXECUTE_ACTION(Player player) {
        for(String action : ConfigMainBuffer.buffer.halal_mode.action) {
            action = action.replace("%player%", player.getName());
            action = action.replace("%action%", "eaten pork");

            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), action);
        }
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        if((event.getItem().getType() == org.bukkit.Material.PORKCHOP || event.getItem().getType() == org.bukkit.Material.COOKED_PORKCHOP)
                && ConfigMainBuffer.buffer.halal_mode.enabled
                && ConfigMainBuffer.buffer.halal_mode.activate.pork)
            _EXECUTE_ACTION(event.getPlayer()); // Suffer the consequences of eating pork while halal mode is enabled
    }
}