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
    private HashMap<Class<? extends LivingEntity>, Double> extras;
    public JobHunter() {
        super();

        this.extras = new HashMap<>();
        extras.put(Blaze.class, .2);
        extras.put(ElderGuardian.class, .4);
        extras.put(Evoker.class, .3);
        extras.put(Ghast.class, .1);
        extras.put(Guardian.class, .1);
        extras.put(PiglinBrute.class, .3);
        extras.put(Pillager.class, .1);
        extras.put(Ravager.class, .3);
        extras.put(Shulker.class, .1);
        extras.put(Slime.class, .1);
        extras.put(Vex.class, .1);
        extras.put(Vindicator.class, .2);
        extras.put(Zoglin.class, .1);
        extras.put(Wither.class, 799.8);
        extras.put(EnderDragon.class, 399.8);
    }

    @Override public String getName() {
        return "Hunter";
    }
    @Override public Material getMaterial() { return Material.BOW; }

    @EventHandler
    public void entityDeath(EntityDeathEvent e) {
        LivingEntity m = e.getEntity();
        Player p = m.getKiller();
        if(p == null)
            return;
        if(m instanceof Player)
            return;

        double money = .1; // Base money for killing entity.
        if(m instanceof Monster)
            money += .1; // Plus if is a monster, resulting in 0.2$
        money += extras.getOrDefault(m.getClass(), .0);

        DependencyVault.deposit(p, money);
        _ACTION_BAR(p, String.format("&a&l+%.2f$", money));
        _SOUND_EXP2(p);
    }
}