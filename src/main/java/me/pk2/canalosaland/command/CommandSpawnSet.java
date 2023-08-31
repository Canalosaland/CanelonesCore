package me.pk2.canalosaland.command;

import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandSpawnSet implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(_COLOR("&cEste comando solo puede ser ejecutado por un jugador."));
            return true;
        }

        if(!player.hasPermission("canalosaland.spawn.set")) {
            player.sendMessage(_COLOR("&cNo tienes permisos para ejecutar este comando."));
            return true;
        }

        ConfigMainBuffer.buffer.spawn = player.getLocation();
        ConfigMainBuffer.save();

        player.sendMessage(_COLOR("&aHas establecido el spawn correctamente."));
        return true;
    }
}