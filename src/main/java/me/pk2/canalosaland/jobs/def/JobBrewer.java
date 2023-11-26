package me.pk2.canalosaland.jobs.def;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.jobs.Job;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class JobBrewer extends Job {
    public @Override String getName() { return "Brewer"; }
    public @Override Material getMaterial() { return Material.BREWING_STAND; }

    @EventHandler
    public void onBrew(BrewEvent e) {
        BrewerInventory inventory = e.getContents();
        Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
            for(HumanEntity human : inventory.getViewers())
                if(human instanceof Player) {
                    Player player = (Player)human;
                    if(!is(player))
                        continue;

                    double money = .2;
                    for (ItemStack iStack : inventory.getContents()) {
                        if(iStack == null || iStack.getType() != Material.POTION)
                            continue;
                        money += .2;

                        PotionMeta meta = (PotionMeta)iStack.getItemMeta();
                        PotionType type = meta.getBasePotionData().getType();
                        if(type == PotionType.AWKWARD || type == PotionType.MUNDANE || type == PotionType.THICK) {
                            money += .05;
                            continue;
                        }

                        money += .36;
                        if(type == PotionType.TURTLE_MASTER)
                            money += .17;
                    }

                    sendMoney(player, money);
                }
        }, 1L);
    }
}