package me.pk2.canalosaland.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandSoundAll implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("canalosaland.soundall")) {
            sender.sendMessage(_COLOR("&cYou do not have permission to execute this command!"));
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage(_COLOR("&cUsage: /soundall <sound> [volume] [pitch]"));
            return true;
        }

        float volume = 1.0F;
        float pitch = 1.0F;
        if(args.length >= 2) {
            try {
                volume = Float.parseFloat(args[1]);
            } catch (NumberFormatException exception) {
                sender.sendMessage(_COLOR("&cInvalid volume!"));
                return true;
            }
        }

        if(args.length >= 3) {
            try {
                pitch = Float.parseFloat(args[2]);
            } catch (NumberFormatException exception) {
                sender.sendMessage(_COLOR("&cInvalid pitch!"));
                return true;
            }
        }

        float finalVolume = volume;
        float finalPitch = pitch;
        Bukkit.getServer().getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), args[0], finalVolume, finalPitch));

        sender.sendMessage(_COLOR("&aSuccessfully played sound to all players!"));
        return true;
    }
}