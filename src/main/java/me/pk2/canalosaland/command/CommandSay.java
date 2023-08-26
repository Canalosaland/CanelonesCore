package me.pk2.canalosaland.command;

import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandSay implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("canalosaland.say")) {
            sender.sendMessage(_COLOR("&cYou do not have permission to execute this command!"));
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage(_COLOR("&cUsage: /say <message>"));
            return true;
        }

        StringBuilder builder = new StringBuilder();
        for(String arg : args)
            builder.append(arg).append(" ");

        Bukkit.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(_COLOR(ConfigMainBuffer.buffer.server_prefix + builder.toString().trim())));
        return true;
    }
}