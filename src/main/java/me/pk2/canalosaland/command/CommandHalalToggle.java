package me.pk2.canalosaland.command;

import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandHalalToggle implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            sender.sendMessage(_COLOR("&eHalal Mode is currently " + (ConfigMainBuffer.buffer.halal_mode.enabled ? "&aenabled" : "&cdisabled")));
            return true;
        }

        if(!sender.hasPermission("canalosaland.halal")) {
            sender.sendMessage(_COLOR("&cYou do not have permission to execute this command!"));
            return true;
        }

        if(args[0].equalsIgnoreCase("on")) {
            ConfigMainBuffer.buffer.halal_mode.enabled = true;
            sender.sendMessage(_COLOR("&aHalal Mode has been enabled!"));
        } else if(args[0].equalsIgnoreCase("off")) {
            ConfigMainBuffer.buffer.halal_mode.enabled = false;
            sender.sendMessage(_COLOR("&cHalal Mode has been disabled!"));
        } else {
            sender.sendMessage(_COLOR("&cUsage: /halal <on/off>"));
            return true;
        }

        ConfigMainBuffer.save();
        return true;
    }
}