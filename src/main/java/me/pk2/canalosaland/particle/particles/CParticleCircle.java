package me.pk2.canalosaland.particle.particles;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.particle.CParticle;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

public class CParticleCircle extends CParticle {
    public CParticleCircle() {
        super("circle");
    }

    @Override
    public void run(Location location, Particle particle, int loops) {
        BukkitRunnable runnable = new BukkitRunnable() {
            Location loc = location.clone();
            double t = 0;
            double r = 2;

            @Override
            public void run() {
                t+=Math.PI/16;
                double x = r*Math.cos(t);
                double y = r*Math.sin(t);
                double z = r*Math.sin(t);
                loc.add(x, y, z);
                location.getWorld().spawnParticle(particle, loc.getX(), loc.getX(), loc.getX(), 1, 0, 0, 0);
                // effect.display(0, 0, 0, 0, 1, loc, 32);
                loc.subtract(x, y, z);

                if(t > Math.PI*loops) reference().stop(location);
            }
        };
        this.instances.put(location, runnable);

        runnable.runTaskTimer(CanelonesCore.INSTANCE, 0, 1);
    }
}