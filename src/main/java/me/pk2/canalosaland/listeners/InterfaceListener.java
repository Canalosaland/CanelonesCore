package me.pk2.canalosaland.listeners;

import me.pk2.canalosaland.interfaces.GInterface;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

public class InterfaceListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player))
            return;
        User user = UserManager.get(player);
        if(user == null)
            return;

        for(GInterface gui : user.interfaces.values())
            if(gui.getInstance().equals(event.getClickedInventory())) {
                event.setCancelled(true);
                gui.click(event.getSlot());

                return;
            }
    }

    @EventHandler
    public void onInventoryDrag(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player))
            return;
        User user = UserManager.get(player);
        if(user == null)
            return;

        for(GInterface gui : user.interfaces.values())
            if(gui.getInstance().equals(event.getClickedInventory())) {
                event.setCancelled(true);
                return;
            }
    }
}