package me.pk2.canalosaland.interfaces;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.buffer.DBBufferMB;
import me.pk2.canalosaland.db.obj.DBUserMBObj;
import me.pk2.canalosaland.db.obj.mb.DBMysteryBoxLocationObj;
import me.pk2.canalosaland.db.obj.mb.DBMysteryBoxObj;
import me.pk2.canalosaland.db.obj.mb.action.MysteryBoxAction;
import me.pk2.canalosaland.particle.particles.CParticleRadialWaves;
import me.pk2.canalosaland.particle.particles.CParticleTest;
import me.pk2.canalosaland.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static me.pk2.canalosaland.util.Wrapper.*;

public class GInterfaceMB extends GInterface {
    public GInterfaceMB(User user) {
        super(user, "&5MysteryBox", 3);

        this.random = new Random();
    }

    private Random random;
    private MysteryBoxAction randomAction(DBMysteryBoxObj obj) {
        return obj.getSlots()[random.nextInt(obj.getSlots().length)];
    }

    @Override
    public void init() {

    }

    @Override
    public void open() {
        if(owner.getLastMB().isInUse()) {
            owner.sendLocale("MB_IN_USE");
            _SOUND_ERROR(owner.player);
            return;
        }

        for(int i = 0; i < 27; i++)
            setItem(i, new ItemStack(Material.AIR));

        super.open();
        _SOUND_OPEN(owner.player);

        final DBUserMBObj[] userMBArr = owner.getBoxes();
        if(userMBArr.length == 0) {
            for(int i = 0; i < 27; i++)
                setItem(i, Material.RED_STAINED_GLASS_PANE, 0, owner.translateC("MB_NO_BOXES"));
            return;
        }

        for(int i = 0; i < userMBArr.length; i++) {
            DBUserMBObj userMB = userMBArr[i];
            DBMysteryBoxObj mysteryBox = DBBufferMB.BUFFER.getById(userMB.mid);

            List<String> lore = new ArrayList<>();
            lore.add(owner.translateC("MB_REMAINING_BOXES").replace("%uses%", String.valueOf(userMB.amount)));
            lore.add(_COLOR("&r "));
            lore.add(owner.translateC("MB_CLICK_CLAIM"));

            ItemStack item = new ItemStack(mysteryBox.getMaterial());
            ItemMeta meta = item.getItemMeta();

            item.setAmount(Math.min(userMB.amount, 64));
            meta.setDisplayName(_COLOR(mysteryBox.getName()));
            meta.setLore(lore);

            item.setItemMeta(meta);

            setItem(i, item);
        }
    }

    @Override
    public void click(int slot) {
        if(owner.getLastMB() == null || owner.getLastMB().isInUse()) {
            owner.sendLocale("MB_IN_USE");
            _SOUND_ERROR(owner.player);
            return;
        }

        final DBMysteryBoxLocationObj location = owner.getLastMB();
        final DBUserMBObj[] userMBArr = owner.getBoxes();
        if(slot >= userMBArr.length)
            return;

        final DBMysteryBoxObj obj = DBBufferMB.BUFFER.getById(userMBArr[slot].mid);
        if(obj == null) {
            owner.sendLocale("MB_NO_EXIST");
            _SOUND_ERROR(owner.player);
            return;
        }

        final int userId = owner.getUserId();
        final MysteryBoxAction action = randomAction(obj);

        owner.sendLocale("MB_OPENING");

        location.setInUse(true);
        owner.player.getInventory().close();
        DBApi.enqueue(() -> {
            Connection conn = DBApi.connect();

            int exCode = DBApi.API.users_mb.remove(conn, userId, obj.getId(), 1);
            if(exCode != 1) {
                location.setInUse(false);

                owner.sendLocale("MB_ERROR");
                _SOUND_ERROR(owner.player);
                return;
            }

            owner.fetchData();

            DBApi.disconnect(conn);

            Bukkit.getScheduler().runTask(CanelonesCore.INSTANCE, () -> new CParticleTest(owner, action, location).run(location.getLocation(), Particle.FLAME, 6));
        });
    }
}