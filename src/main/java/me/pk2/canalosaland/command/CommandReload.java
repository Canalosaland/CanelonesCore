package me.pk2.canalosaland.command;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.config.ConfigLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandReload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("canalosaland.reload")) {
            sender.sendMessage(_COLOR("&cYou do not have permission to execute this command!"));
            return true;
        }

        sender.sendMessage(_COLOR("&eReloading CanalosaLand..."));
        ConfigLoader.load();

        sender.sendMessage(_COLOR("&aReloaded CanalosaLand!"));
        return true;
    }
}