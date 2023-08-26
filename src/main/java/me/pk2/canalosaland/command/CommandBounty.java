package me.pk2.canalosaland.command;

import me.pk2.canalosaland.config.buff.ConfigBountyBuffer;
import me.pk2.canalosaland.dependencies.DependencyVault;
import me.pk2.canalosaland.util.Wrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandBounty implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length <2) {
            sender.sendMessage(_COLOR("&cUsage: /bounty <player> <amount>"));
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if(target == null) {
            sender.sendMessage(_COLOR("&cPlayer not found!"));
            return true;
        }

        if((sender instanceof Player) && target.getUniqueId().toString().equalsIgnoreCase(((Player) sender).getUniqueId().toString())) {
            sender.sendMessage(_COLOR("&cYou can't put a bounty on yourself!"));
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
            amount = Math.round(amount * 100.0) / 100.0;
        } catch (Exception ex) {
            sender.sendMessage(_COLOR("&cInvalid amount!"));
            return true;
        }

        if(amount <= 0) {
            sender.sendMessage(_COLOR("&cInvalid amount!"));
            return true;
        }

        if(!sender.hasPermission("canalosaland.bounty") && sender instanceof Player p) {
            if (DependencyVault.has(p, amount))
                DependencyVault.withdraw(p, amount);
            else {
                sender.sendMessage(_COLOR("&cYou don't have enough money!"));
                return true;
            }
        }

        String senderUUID = (sender instanceof Player) ? ((Player)sender).getUniqueId().toString() : "ffffffff-ffff-ffff-ffff-ffffffffffff";
        String senderName = (sender instanceof Player) ? ((Player)sender).getName() : "CONSOLE";

        if(ConfigBountyBuffer.getBounty(target.getName()) > 0)
            _GLOBAL_MESSAGE("&c" + senderName + " &7has added a &e" + amount + "$ &7bounty on &c" + target.getName() + "&7!");
        else _GLOBAL_MESSAGE("&c" + senderName + " &7has put a &e" + amount + "$ &7bounty on &c" + target.getName() + "&7!");


        ConfigBountyBuffer.addBounty(target.getUniqueId().toString(), senderUUID, amount);
        ConfigBountyBuffer.save();

        Bukkit.getOnlinePlayers().forEach(Wrapper::_SOUND_BOUNTY);
        return true;
    }
}