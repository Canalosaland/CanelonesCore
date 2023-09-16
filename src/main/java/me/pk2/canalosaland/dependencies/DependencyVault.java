package me.pk2.canalosaland.dependencies;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.CanelonesCore;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class DependencyVault {
    public static Economy economy;
    public static void register(RegisteredServiceProvider<Economy> rsp) {
        if(rsp != null) {
            economy = rsp.getProvider();
            _LOG("Vault registered!");
        } else CanelonesCore.INSTANCE.getLogger().warning("Vault is not installed, economy will not work!");
    }

    public static double getBalance(Player player) {
        if(economy == null)
            return 0.00d;
        return economy.getBalance(player);
    }

    public static boolean has(Player player, double amount) {
        if(economy == null)
            return false;
        return economy.has(player, amount);
    }

    public static void withdraw(Player player, double amount) {
        if(economy == null)
            return;
        economy.withdrawPlayer(player, amount);
    }

    public static void deposit(Player player, double amount) {
        if(economy == null)
            return;
        economy.depositPlayer(player, amount);
    }
}