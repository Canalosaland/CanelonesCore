package me.pk2.canalosaland.listeners;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.config.buff.ConfigBountyBuffer;
import me.pk2.canalosaland.dependencies.DependencyVault;
import me.pk2.canalosaland.util.Wrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static me.pk2.canalosaland.util.Wrapper.*;

public class BountyListener implements Listener {
    @EventHandler
    public void onDie(PlayerDeathEvent event) {
        Player target = event.getEntity();
        Player killer = target.getKiller();
        if(killer == null) return;

        double bounty = ConfigBountyBuffer.getBounty(target.getUniqueId().toString());
        if(bounty > 0) {
            ConfigBountyBuffer.removeBounty(target.getUniqueId().toString());
            ConfigBountyBuffer.save();
            DependencyVault.deposit(killer, bounty);

            String killerName = killer.getName();
            String targetName = target.getName();

            Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
                _GLOBAL_MESSAGE("&a" + killerName + " &7has claimed the &e" + bounty + "$ &7bounty on &c" + targetName + "&7!");
                Bukkit.getOnlinePlayers().forEach(Wrapper::_SOUND_BOUNTY);
            }, 20L);
        }
    }
}