package me.pk2.canalosaland.jobs.def;

import me.pk2.canalosaland.dependencies.DependencyVault;
import me.pk2.canalosaland.jobs.Job;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.util.Map;

import static me.pk2.canalosaland.util.Wrapper._ACTION_BAR;
import static me.pk2.canalosaland.util.Wrapper._SOUND_EXP2;

public class JobEnchanter extends Job {
    public @Override String getName() { return "Enchanter"; }
    public @Override Material getMaterial() { return Material.ENCHANTED_BOOK; }

    @EventHandler
    public void enchantEvent(EnchantItemEvent e) {
        Player p = e.getEnchanter();
        if(!is(p))
            return;

        Map<Enchantment, Integer> enchantments = e.getEnchantsToAdd();
        if(enchantments.isEmpty())
            return;
        double money = .4;

        money += e.getExpLevelCost()*.05;
        for(Integer level : enchantments.values()) {
            if (level > 3)
                money += .2 * level;
            else if(level > 1) money += .1 * level;
        }

        sendMoney(p, money);
    }
}