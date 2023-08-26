package me.pk2.canalosaland.listeners;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.event.EventHandler;
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