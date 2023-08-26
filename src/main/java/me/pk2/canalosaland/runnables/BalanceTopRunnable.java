package me.pk2.canalosaland.runnables;

import me.pk2.canalosaland.dependencies.DependencyVault;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Map;

public class BalanceTopRunnable extends BukkitRunnable {
    public class BalanceTopEntry implements Map.Entry<OfflinePlayer, Double> {
        private OfflinePlayer player;
        private Double balance;

        public BalanceTopEntry(OfflinePlayer player, Double balance) {
            this.player = player;
            this.balance = balance;
        }

        @Override
        public OfflinePlayer getKey() {
            return player;
        }

        @Override
        public Double getValue() {
            return balance;
        }

        @Override
        public Double setValue(Double value) {
            return balance = value;
        }
    }

    public static ArrayList<Map.Entry<OfflinePlayer, Double>> balances = new ArrayList<>();
    public static BalanceTopEntry[] top10 = new BalanceTopEntry[10];

    @Override
    public void run() {
        if(DependencyVault.economy == null)
            return;

        ArrayList<Map.Entry<OfflinePlayer, Double>> balances = new ArrayList<>();
        OfflinePlayer[] players = Bukkit.getOfflinePlayers();
        for (OfflinePlayer player : players) {
            if (DependencyVault.economy.hasAccount(player))
                balances.add(Map.entry(player, DependencyVault.economy.getBalance(player)));
        }

        BalanceTopEntry[] top10 = new BalanceTopEntry[10];

        balances.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        for(int i = 0; i < 10; i++) {
            if(i >= balances.size())
                break;
            top10[i] = new BalanceTopEntry(balances.get(i).getKey(), balances.get(i).getValue());
        }

        BalanceTopRunnable.balances = balances;
        BalanceTopRunnable.top10 = top10;
    }
}