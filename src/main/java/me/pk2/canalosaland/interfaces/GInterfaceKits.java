package me.pk2.canalosaland.interfaces;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.db.buffer.DBBufferKits;
import me.pk2.canalosaland.db.obj.DBKitObj;
import me.pk2.canalosaland.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import static me.pk2.canalosaland.util.Wrapper._SOUND_OPEN;

public class GInterfaceKits extends GInterface{
    public int kid;
    public GInterfaceKits(User user) {
        super(user, "&9&lTodos los kits", 3);

        kid = -1;
    }

    private DBKitObj[] kits;
    @Override
    public void open() {
        super.open();
        _SOUND_OPEN(owner.player);

        // RESET
        for(int i = 0; i < 27; i++)
            setItem(i, Material.WHITE_STAINED_GLASS_PANE, 0, "&0", "");

        kits = DBBufferKits.BUFFER.getKits();
        for(int i = 0; i < kits.length; i++) {
            DBKitObj kit = kits[i];
            if(kit == null)
                continue;

            setItem(i, kit.material.getType(), kit.material.getDurability(), 1, kit.name, "&eReclama el kit con &l/iphone", "", "&7Haz clic para ver el contenido", "&8id=" + kit.id);
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void click(int slot) {
        if(kits == null)
            return;
        if(slot >= kits.length)
            return;

        DBKitObj kit = kits[slot];
        if(kit == null)
            return;

        owner.player.closeInventory();

        kid = kit.id;
        Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
            owner.openI(GInterfaceKitsOpen.class);
        }, 2L);
    }
}