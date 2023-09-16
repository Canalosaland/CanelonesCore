package me.pk2.canalosaland.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

import static me.pk2.canalosaland.util.Wrapper.*;

public class TPAListener implements Listener {
    public static HashMap<String, Long> users;
    static {
        users = new HashMap<>();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(users.containsKey(_UUID(event.getPlayer()))) {
            users.remove(_UUID(event.getPlayer())); // Cancel /tpa
            event.getPlayer().sendMessage(_SENDER_TRANSLATE(event.getPlayer(), "COMMAND_TPA_CANCEL_MOVE"));
        }
    }
}