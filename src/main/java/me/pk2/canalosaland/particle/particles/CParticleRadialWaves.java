package me.pk2.canalosaland.particle.particles;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.db.obj.mb.DBMysteryBoxLocationObj;
import me.pk2.canalosaland.db.obj.mb.action.MBACommand;
import me.pk2.canalosaland.db.obj.mb.action.MBAItem;
import me.pk2.canalosaland.db.obj.mb.action.MysteryBoxAction;
import me.pk2.canalosaland.particle.CParticle;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.util.BukkitSerialization;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Random;

import static me.pk2.canalosaland.util.Wrapper._COLOR;
import static me.pk2.canalosaland.util.Wrapper._PLACEHOLDER;

public class CParticleRadialWaves extends CParticle {
    private User user;
    private MysteryBoxAction act;
    private DBMysteryBoxLocationObj locationObj;
    public CParticleRadialWaves(User user, MysteryBoxAction act, DBMysteryBoxLocationObj locationObj) {
        super("radialwaves");

        this.user = user;
        this.act = act;
        this.locationObj = locationObj;
    }

    @Override
    public void run(Location location, Particle particle, int loops) {
        BukkitRunnable runnable = new BukkitRunnable() {
            Location loc = location.clone();
            double t = 0;
            int yes = 0;

            @Override
            public void run() {
                if(yes == 0) { /* Fix Location */
                    loc.setX(loc.getBlockX() + .5);
                    loc.setY(loc.getBlockY() -  1);
                    loc.setZ(loc.getBlockZ() + .5);
                }
                yes++;

                if(t == 0) {
                    loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                    Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
                        locationObj.setInUse(false);

                        if(user != null && act != null && user.player != null && user.player.isValid()) {
                            if(act instanceof MBACommand) {
                                final MBACommand mbaCommand = (MBACommand) act;
                                Bukkit.getScheduler().runTaskAsynchronously(CanelonesCore.INSTANCE, () -> {
                                    ItemStack item = BukkitSerialization.deserializeItems(mbaCommand.getMaterial())[0];
                                    ItemMeta materialMeta = item.getItemMeta();

                                    Bukkit.getScheduler().runTask(CanelonesCore.INSTANCE, () -> {
                                        if(user == null || user.player == null || !user.player.isValid())
                                            return;
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), _PLACEHOLDER(user.player, mbaCommand.getCommand()));

                                        user.player.sendMessage(user.translateC("MB_COMPLETED").replace("%name%", _COLOR(materialMeta.hasDisplayName()?materialMeta.getDisplayName():item.getType().name())));
                                        loc.getWorld().playSound(loc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1f);
                                    });
                                });

                                return;
                            }

                            if(act instanceof MBAItem) {
                                final MBAItem mbaItem = (MBAItem) act;
                                Bukkit.getScheduler().runTaskAsynchronously(CanelonesCore.INSTANCE, () -> {
                                    ItemStack item = BukkitSerialization.deserializeItems(mbaItem.getItem())[0];
                                    ItemMeta materialMeta = item.getItemMeta();

                                    // YOOOO WHY 9999 TASKS=????
                                    //    "because they are required :>"
                                    //    - PK2_Stimpy
                                    Bukkit.getScheduler().runTask(CanelonesCore.INSTANCE, () -> {
                                        if(user == null || user.player == null || !user.player.isValid())
                                            return;

                                        HashMap<Integer, ItemStack> items = user.player.getInventory().addItem(item);
                                        if(!items.isEmpty())
                                            for(ItemStack iStack : items.values())
                                                loc.getWorld().dropItem(user.player.getLocation(), iStack);

                                        user.player.sendMessage(user.translateC("MB_COMPLETED").replace("%name%", _COLOR(materialMeta.hasDisplayName()?materialMeta.getDisplayName():item.getType().name())));
                                        loc.getWorld().playSound(loc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1f);
                                    });
                                });

                                return;
                            }

                            user.sendLocale("MB_NO_EXIST");
                        }
                    }, 40L);
                }

                t = t + 0.1*Math.PI;
                for (double theta = 0; theta <= 2*Math.PI; theta = theta + Math.PI/32){
                    double x = t*Math.cos(theta);
                    double y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
                    double z = t*Math.sin(theta);
                    loc.add(x,y,z);
                    location.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 1, 0.1, 0.1, 0.1);
                    loc.subtract(x,y,z);

                    theta = theta + Math.PI/64;

                    x = t*Math.cos(theta);
                    y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
                    z = t*Math.sin(theta);
                    loc.add(x,y,z);
                    location.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 1, 0.1, 0.1, 0.1);
                    loc.subtract(x,y,z);

                }
                if (t > 20) {  reference().stop(location); }
            }
        };
        this.instances.put(location, runnable);

        runnable.runTaskTimer(CanelonesCore.INSTANCE, 0, 1);
    }
}