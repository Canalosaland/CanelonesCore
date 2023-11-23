package me.pk2.canalosaland.jobs.def;

import me.pk2.canalosaland.dependencies.DependencyVault;
import me.pk2.canalosaland.jobs.Job;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

import static me.pk2.canalosaland.util.Wrapper._ACTION_BAR;
import static me.pk2.canalosaland.util.Wrapper._SOUND_EXP2;

public class JobFisherman extends Job {
    private HashMap<Material, Double> extras;
    public JobFisherman() {
        this.extras = new HashMap<>();
        extras.put(Material.TROPICAL_FISH, .4);
        extras.put(Material.PUFFERFISH, .1);
        extras.put(Material.BOW, .2);
        extras.put(Material.ENCHANTED_BOOK, 3.9);
        extras.put(Material.NAME_TAG, 2.9);
        extras.put(Material.NAUTILUS_SHELL, 2.9);
        extras.put(Material.SADDLE, 3.9);
        extras.put(Material.FISHING_ROD, .2);
    }

    @Override public String getName() { return "Fisherman"; }
    @Override public Material getMaterial() { return Material.FISHING_ROD; }

    @EventHandler
    public void pFish(PlayerFishEvent e) {
        Player p = e.getPlayer();
        if (!is(p) || e.getCaught() == null)
            return;

        Entity m = e.getCaught();
        if (m instanceof Item) {
            double money = .1;
            money += extras.getOrDefault(((Item) m).getItemStack().getType(), .0);

            ItemStack iStack = ((Item)m).getItemStack();
            if(!iStack.getEnchantments().isEmpty()) {
                money += .2 * iStack.getEnchantments().size();
                for(Integer level : iStack.getEnchantments().values())
                    if(level > 1)
                        money += .1*level;
            }

            DependencyVault.deposit(p, money);
            _ACTION_BAR(p, String.format("&a&l+%.2f$", money));
            _SOUND_EXP2(p);
        }
    }
}