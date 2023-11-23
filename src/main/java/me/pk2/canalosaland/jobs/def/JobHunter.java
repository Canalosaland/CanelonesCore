package me.pk2.canalosaland.jobs.def;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.dependencies.DependencyVault;
import me.pk2.canalosaland.jobs.Job;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;

public class JobHunter extends Job {
    private HashMap<String, Double> extras;
    public JobHunter() {
        super();

        this.extras = new HashMap<>();
        extras.put("CraftBlaze", .2);
        extras.put("CraftElderGuardian", .4);
        extras.put("CraftEvoker", .3);
        extras.put("CraftGhast", .2);
        extras.put("CraftGuardian", .1);
        extras.put("CraftPiglinBrute", .3);
        extras.put("CraftPillager", .1);
        extras.put("CraftRavager", .3);
        extras.put("CraftShulker", .1);
        extras.put("CraftSlime", .1);
        extras.put("CraftVex", .1);
        extras.put("CraftVindicator", .2);
        extras.put("CraftZoglin", .1);
        extras.put("CraftWither", 799.8);
        extras.put("CraftEnderDragon", 399.8);
    }

    @Override public String getName() {
        return "Hunter";
    }
    @Override public Material getMaterial() { return Material.BOW; }

    @EventHandler
    public void entityDeath(EntityDeathEvent e) {
        LivingEntity m = e.getEntity();
        Player p = m.getKiller();
        if(p == null || !is(p))
            return;
        if(m instanceof Player)
            return;

        double money = .1; // Base money for killing entity.
        if(m instanceof Monster)
            money += .1; // Plus if is a monster, resulting in 0.2$
        money += extras.getOrDefault(m.getClass().getSimpleName(), .0);

        DependencyVault.deposit(p, money);
        _ACTION_BAR(p, String.format("&a&l+%.2f$", money));
        _SOUND_EXP2(p);
    }
}