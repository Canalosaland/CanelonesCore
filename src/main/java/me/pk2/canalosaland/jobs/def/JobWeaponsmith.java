package me.pk2.canalosaland.jobs.def;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.jobs.Job;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.HashMap;

public class JobWeaponsmith extends Job {
    private HashMap<Material, Double> crafts;
    public JobWeaponsmith() {
        super();
        this.crafts = new HashMap<>();
        crafts.put(Material.WOODEN_SWORD, .1);
        crafts.put(Material.STONE_SWORD, .2);
        crafts.put(Material.IRON_SWORD, .4);
        crafts.put(Material.GOLDEN_SWORD, .5);
        crafts.put(Material.DIAMOND_SWORD, 10d);
        crafts.put(Material.NETHERITE_SWORD, 40d);

        crafts.put(Material.WOODEN_AXE, .1);
        crafts.put(Material.STONE_AXE, .2);
        crafts.put(Material.IRON_AXE, .4);
        crafts.put(Material.GOLDEN_AXE, .5);
        crafts.put(Material.DIAMOND_AXE, 10d);
        crafts.put(Material.NETHERITE_AXE, 40d);

        crafts.put(Material.LEATHER_HELMET, .1);
        crafts.put(Material.CHAINMAIL_HELMET, .3);
        crafts.put(Material.IRON_HELMET, .4);
        crafts.put(Material.GOLDEN_HELMET, .5);
        crafts.put(Material.DIAMOND_HELMET, 10d);
        crafts.put(Material.NETHERITE_HELMET, 40d);
        crafts.put(Material.TURTLE_HELMET, 60d);

        crafts.put(Material.LEATHER_CHESTPLATE, .2);
        crafts.put(Material.CHAINMAIL_CHESTPLATE, .6);
        crafts.put(Material.IRON_CHESTPLATE, .8);
        crafts.put(Material.GOLDEN_CHESTPLATE, 1d);
        crafts.put(Material.DIAMOND_CHESTPLATE, 20d);
        crafts.put(Material.NETHERITE_CHESTPLATE, 80d);

        crafts.put(Material.LEATHER_LEGGINGS, .2);
        crafts.put(Material.CHAINMAIL_LEGGINGS, .6);
        crafts.put(Material.IRON_LEGGINGS, .8);
        crafts.put(Material.GOLDEN_LEGGINGS, 1d);
        crafts.put(Material.DIAMOND_LEGGINGS, 20d);
        crafts.put(Material.NETHERITE_LEGGINGS, 80d);

        crafts.put(Material.LEATHER_BOOTS, .1);
        crafts.put(Material.CHAINMAIL_BOOTS, .3);
        crafts.put(Material.IRON_BOOTS, .4);
        crafts.put(Material.GOLDEN_BOOTS, .5);
        crafts.put(Material.DIAMOND_BOOTS, 10d);
        crafts.put(Material.NETHERITE_BOOTS, 40d);

        crafts.put(Material.BOW, .1);
        crafts.put(Material.CROSSBOW, .1);
        crafts.put(Material.ARROW, .05);
        crafts.put(Material.TIPPED_ARROW, .11);
        crafts.put(Material.SHIELD, .3);
        crafts.put(Material.TRIDENT, 10d);
    }

    public @Override String getName() { return "Weaponsmith"; }
    public @Override Material getMaterial() { return Material.DIAMOND_SWORD; }

    // Will make in a future
}