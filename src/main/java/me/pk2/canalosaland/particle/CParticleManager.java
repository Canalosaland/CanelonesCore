// FROM MY OLD PLUGIN SHAFTY NETWORK
package me.pk2.canalosaland.particle;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Map;

public class CParticleManager {
    private ArrayList<CParticle> particles;

    public CParticleManager() {
        this.particles = new ArrayList<CParticle>();
        /* Add Particles */
    }

    public CParticle[] getParticlesArr() { return particles.toArray(CParticle[]::new); }
    public ArrayList<CParticle> getParticles() { return particles; }
    public CParticle getParticle(String name) {
        for(CParticle particle : particles)
            if(particle.getName().equalsIgnoreCase(name))
                return particle;
        return null;
    }

    public void clearParticles() {
        for(CParticle particle : particles)
            for(Map.Entry<Location, BukkitRunnable> entry : particle.instances.entrySet()) {
                entry.getValue().cancel();
                particle.instances.remove(entry.getKey());
            }
    }
}