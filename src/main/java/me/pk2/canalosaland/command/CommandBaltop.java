package me.pk2.canalosaland.command;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import me.pk2.canalosaland.dependencies.DependencyVault;
import me.pk2.canalosaland.runnables.BalanceTopRunnable;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandBaltop implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) { // No need to translate.
        Economy economy = DependencyVault.economy;
        if(economy == null) {
            sender.sendMessage(_COLOR("&cNo economy plugin found!"));
            return true;
        }

        ArrayList<OfflinePlayer> players = new ArrayList<>();
        for(OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if(economy.hasAccount(player)) players.add(player);
        }

        BalanceTopRunnable.BalanceTopEntry[] top10 = BalanceTopRunnable.top10;

        sender.sendMessage(_COLOR("&7&m--------------------------------"));
        sender.sendMessage(_COLOR("&6&lTop 10 balances(updated every " + ConfigMainBuffer.buffer.baltop.update + " minutes)"));
        sender.sendMessage(_COLOR(""));
        for(int i = 0; i < 10; i++) {
            if(i >= top10.length || top10[i] == null)
                break;
            sender.sendMessage(_COLOR("&e" + (i + 1) + ". &f" + top10[i].getKey().getName() + " &7- &f" + economy.format(top10[i].getValue()) + "$"));
        }
        sender.sendMessage(_COLOR("&7&m--------------------------------"));
        return true;
    }
}