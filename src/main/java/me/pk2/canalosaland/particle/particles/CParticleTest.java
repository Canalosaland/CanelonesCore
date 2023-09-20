package me.pk2.canalosaland.particle.particles;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.db.obj.mb.DBMysteryBoxLocationObj;
import me.pk2.canalosaland.db.obj.mb.action.MysteryBoxAction;
import me.pk2.canalosaland.particle.CParticle;
import me.pk2.canalosaland.user.User;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class CParticleTest extends CParticle {
    private User user;
    private MysteryBoxAction act;
    private DBMysteryBoxLocationObj locationObj;
    public CParticleTest(User user, MysteryBoxAction act, DBMysteryBoxLocationObj locationObj) {
        super("test");

        this.user = user;
        this.act = act;
        this.locationObj = locationObj;
    }

    public Entity getEntityByUUID(World world, UUID uuid) {
        for(Entity entity : world.getEntities())
            if(entity.getUniqueId() == uuid)
                return entity;

        return null;
    }

    @Override
    public void run(Location location, Particle particle, int loops) {
        BukkitRunnable runnable = new BukkitRunnable() {
            Location loc = location.clone();
            double t = 0;
            double r = 1;
            double offset = Math.PI/16;
            int yes = 0;

            @Override
            public void run() {
                if(yes == 0) { /* Fix Location */
                    loc.setX(loc.getBlockX() + .5);
                    loc.setZ(loc.getBlockZ() + .5);
                }

                float pitch = 0.0f;
                if(yes ==  0) pitch = 0.5f;
                if(yes == 16) pitch = 0.6f;
                if(yes == 32) pitch = 0.7f;
                if(yes == 48) pitch = 0.8f;
                if(yes == 64) pitch = 0.9f;
                //if(yes == 80) pitch = 1.0f;

                if(pitch != 0.0f)
                    location.getWorld().playSound(location, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, pitch);
                yes++;

                t+=offset;
                double x = r*Math.cos(t);
                double y = .25*t;
                double z = r*Math.sin(t);
                loc.add(x, y, z);
                //location.getWorld().spawnParticle(particle, loc.getX(), loc.getX(), loc.getX(), 1, 0, 0, 0);
                location.getWorld().spawnParticle(particle, loc, 1, 1, 0.1, 0.1);

                loc.subtract(x, y, z);

                if(t > Math.PI*loops) {
                    //Shafty.shafty.shaftyCustom.cPacket.useC3Packet("C3ParticleAnimation", "radialwaves", location, Particle.ITEM_CRACK, 1);
                    reference().stop(location);
                    new CParticleRadialWaves(user, act, locationObj).run(location, Particle.ITEM_CRACK, 1);
                }
            }
        };
        this.instances.put(location, runnable);

        runnable.runTaskTimer(CanelonesCore.INSTANCE, 0, 1);
    }
}