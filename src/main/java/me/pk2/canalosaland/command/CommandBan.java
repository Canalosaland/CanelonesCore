package me.pk2.canalosaland.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandBan implements CommandExecutor {
    private static void sendHelp(CommandSender sender) {
        sender.sendMessage("&c/ban <ip/user> <player> <sec> <reason>");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("canalosaland.command.ban")) {
            sender.sendMessage("&cNo tienes permiso para ejecutar este comando.");
            return true;
        }

        if(args.length < 4) {
            sendHelp(sender);
            return true;
        }
        return true;
    }
}