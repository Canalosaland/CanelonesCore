// FROM MY OLD PLUGIN SHAFTY NETWORK
package me.pk2.canalosaland.particle;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public abstract class CParticle {
    private final String name;
    public HashMap<Location, BukkitRunnable> instances;

    public CParticle(String name) {
        this.name = name;

        this.instances = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    protected CParticle reference() {
        return this;
    }

    public abstract void run(Location location, Particle particle, int loops);

    public void stop(Location location) {
        if (instances.containsKey(location))
            instances.get(location).cancel();
        instances.remove(location);
    }
}