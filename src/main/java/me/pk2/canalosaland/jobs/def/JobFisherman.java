package me.pk2.canalosaland.jobs.def;

import me.pk2.canalosaland.jobs.Job;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class JobFisherman extends Job {
    private HashMap<Material, Double> extras;
    public JobFisherman() {
        this.extras = new HashMap<>();
        extras.put(Material.TROPICAL_FISH, .4);
        extras.put(Material.PUFFERFISH, .1);
        extras.put(Material.BOW, .4);
        extras.put(Material.ENCHANTED_BOOK, 3.9);
        extras.put(Material.NAME_TAG, 2.9);
        extras.put(Material.NAUTILUS_SHELL, 2.9);
        extras.put(Material.SADDLE, 3.9);
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
            Material mat = ((Item) m).getItemStack().getType();

        }
    }
}