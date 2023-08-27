package me.pk2.canalosaland.listeners;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.config.buff.ConfigBountyBuffer;
import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UserManager.add(event.getPlayer());
        UserManager.get(event.getPlayer()).handleJoin();

        if(ConfigMainBuffer.buffer.messages.join.enabled)
            event.setJoinMessage(_COLOR(_PLACEHOLDER(event.getPlayer(), ConfigMainBuffer.buffer.messages.join.message)));

        String playerName = event.getPlayer().getName();
        Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
            Player player = Bukkit.getPlayer(playerName);
            if(player == null)
                return;

            double bounty = ConfigBountyBuffer.getBounty(player.getUniqueId().toString());
            if(bounty > 0)
                Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(_COLOR("&7" + player.getName() + " &8has a &7" + bounty + "$ &8bounty.")));
        }, 100L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(ConfigMainBuffer.buffer.messages.leave.enabled)
            event.setQuitMessage(_COLOR(_PLACEHOLDER(event.getPlayer(), ConfigMainBuffer.buffer.messages.leave.message)));

        User user = UserManager.get(event.getPlayer());
        if(user != null)
            user.handleQuit();
        UserManager.remove(event.getPlayer());
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        if(ConfigMainBuffer.buffer.messages.chat.enabled)
            event.setFormat(_COLOR(_PLACEHOLDER(event.getPlayer(), ConfigMainBuffer.buffer.messages.chat.message
                    .replace("%player%", "%s")
                    .replace("%message%", "%s"))));
    }
}