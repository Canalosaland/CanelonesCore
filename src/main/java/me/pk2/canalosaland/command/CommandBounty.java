package me.pk2.canalosaland.command;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.config.buff.ConfigBountyBuffer;
import me.pk2.canalosaland.dependencies.DependencyVault;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import me.pk2.canalosaland.util.Wrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.pk2.canalosaland.util.Wrapper.*;
import static me.pk2.canalosaland.config.buff.ConfigLangBuffer.translateC;

public class CommandBounty implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length <2) {
            sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_USAGE") + "/bounty <player> <amount>");
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if(target == null) {
            sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_BOUNTY_PLAYER_NOT_FOUND"));
            return true;
        }

        if((sender instanceof Player) && target.getUniqueId().toString().equalsIgnoreCase(((Player) sender).getUniqueId().toString())) {
            sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_BOUNTY_CANT_BOUNTY_SELF"));
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
            amount = Math.round(amount * 100.0) / 100.0;
        } catch (Exception ex) {
            sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_BOUNTY_INVALID_AMOUNT"));
            return true;
        }

        if(amount <= 0) {
            sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_BOUNTY_INVALID_AMOUNT"));
            return true;
        }

        if(!sender.hasPermission("canalosaland.bounty") && sender instanceof Player p) {
            if (ConfigBountyBuffer.getBountiesPlayerTarget(_UUID(p), _UUID(target)).length >= 2) {
                sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_BOUNTY_MAX_BOUNTIES"));
                return true;
            }

            if (DependencyVault.has(p, amount))
                DependencyVault.withdraw(p, amount);
            else {
                sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_BOUNTY_NOT_ENOUGH_MONEY"));
                return true;
            }
        }

        String senderUUID = (sender instanceof Player) ? _UUID((Player)sender) : "ffffffff-ffff-ffff-ffff-ffffffffffff";
        String senderName = (sender instanceof Player) ? sender.getName() : "CONSOLE";

        if(ConfigBountyBuffer.getBounty(_UUID(target)) > 0)
            _GLOBAL_MESSAGE_LOCALE("COMMAND_BOUNTY_BOUNTY_INCREASED", senderName, target.getName(), amount);
        else _GLOBAL_MESSAGE_LOCALE("COMMAND_BOUNTY_BOUNTY_SET", senderName, amount, target.getName());

        String targetUUID = _UUID(target);
        double finalAmount = amount;
        Bukkit.getScheduler().runTaskAsynchronously(CanelonesCore.INSTANCE, () -> {
            ConfigBountyBuffer.addBounty(targetUUID, senderUUID, finalAmount);
            ConfigBountyBuffer.save();
        });

        Bukkit.getOnlinePlayers().forEach(Wrapper::_SOUND_BOUNTY);
        return true;
    }
}