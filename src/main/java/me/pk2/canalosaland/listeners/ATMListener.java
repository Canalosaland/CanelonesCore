package me.pk2.canalosaland.listeners;

import me.pk2.canalosaland.config.buff.ConfigAtmBuffer;
import me.pk2.canalosaland.config.buff.atm.ATMObj;
import me.pk2.canalosaland.interfaces.GInterfaceATM;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ATMListener implements Listener {
    @EventHandler
    public void onInteractWithBlock(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if(block == null)
            return;

        User user = UserManager.get(event.getPlayer());
        if(user == null)
            return;

        Location location = block.getLocation();
        ATMObj atm = ConfigAtmBuffer.get(location);
        if(atm == null)
            return;

        event.setCancelled(true);
        user.interfaces.get(GInterfaceATM.class).open();
    }
}