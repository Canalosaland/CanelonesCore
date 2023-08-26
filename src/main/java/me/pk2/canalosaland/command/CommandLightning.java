package me.pk2.canalosaland.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandLightning implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("canalosaland.lightning")) {
            sender.sendMessage(_COLOR("&cYou do not have permission to use this command!"));
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage(_COLOR("&cUsage: /lightning <player>"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            sender.sendMessage(_COLOR("&cPlayer not found!"));
            return true;
        }

        target.getWorld().strikeLightning(target.getLocation());
        sender.sendMessage(_COLOR("&6You have struck " + target.getName() + " with lightning!"));
        return true;
    }
}