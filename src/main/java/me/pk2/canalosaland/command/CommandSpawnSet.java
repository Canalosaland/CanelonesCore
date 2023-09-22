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

        if(args.length < 1) {
            player.sendMessage(_COLOR("&c/spawn-set <over/nether/end>"));
            return true;
        }

        if(args[0].equalsIgnoreCase("over"))
            ConfigMainBuffer.buffer.spawn = player.getLocation();
        else if(args[0].equalsIgnoreCase("nether"))
            ConfigMainBuffer.buffer.nether = player.getLocation();
        else if(args[0].equalsIgnoreCase("end"))
            ConfigMainBuffer.buffer.end = player.getLocation();
        else {
            player.sendMessage(_COLOR("&c/spawn-set <over/nether/end>"));
            return true;
        }

        ConfigMainBuffer.save();

        player.sendMessage(_COLOR("&aHas establecido el spawn correctamente."));
        return true;
    }
}