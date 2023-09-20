package me.pk2.canalosaland.listeners;

import me.pk2.canalosaland.db.buffer.DBBufferMB;
import me.pk2.canalosaland.db.obj.mb.DBMysteryBoxLocationObj;
import me.pk2.canalosaland.interfaces.GInterfaceMB;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class MBListener implements Listener {
    @EventHandler
    public void onInteractWithBlock(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null)
            return;

        User user = UserManager.get(event.getPlayer());
        if(user == null)
            return;

        Location location = block.getLocation();
        DBMysteryBoxLocationObj obj = DBBufferMB.BUFFER.getAt(location);
        if(obj == null)
            return;

        event.setCancelled(true);

        user.setLastMB(obj);
        user.openI(GInterfaceMB.class);
    }
}