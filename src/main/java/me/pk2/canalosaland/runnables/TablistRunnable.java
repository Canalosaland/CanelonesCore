package me.pk2.canalosaland.runnables;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.clip.placeholderapi.PlaceholderAPI;
import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import me.pk2.canalosaland.dependencies.DependencyPAPI;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TablistRunnable extends BukkitRunnable {
    @Override
    public void run() {
        for(Player player : new ArrayList<>(Bukkit.getOnlinePlayers())) {
            if(player == null || !player.isValid())
                continue;
            String header = DependencyPAPI.parse(player, _COLOR(String.join("\n", ConfigMainBuffer.buffer.tablist.header)));
            String footer = DependencyPAPI.parse(player, _COLOR(String.join("\n", ConfigMainBuffer.buffer.tablist.footer)));

            User user = UserManager.get(player);
            if(user != null)
                user.updateTeam();
            player.setPlayerListHeaderFooter(header, footer);
        }
    }
}