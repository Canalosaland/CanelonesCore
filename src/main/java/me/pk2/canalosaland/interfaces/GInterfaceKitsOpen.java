package me.pk2.canalosaland.interfaces;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.db.buffer.DBBufferKits;
import me.pk2.canalosaland.db.obj.DBKitObj;
import me.pk2.canalosaland.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static me.pk2.canalosaland.util.Wrapper.*;

public class GInterfaceKitsOpen extends GInterface {
    public GInterfaceKitsOpen(User user) {
        super(user, "&9&lKit", 4);
    }

    @Override
    public void open() {
        super.open();
        setItem(35, Material.BARRIER, 0, owner.translateC("INTERFACE_ATM_EXIT_NAME"), owner.translateC("INTERFACE_ATM_EXIT_LORE1"));
        _SOUND_OPEN(owner.player);

        // RESET
        for(int i = 0; i < 27; i++)
            setItem(i, Material.WHITE_STAINED_GLASS_PANE, 0, "&0", "");

        int kid = ((GInterfaceKits) owner.interfaces.get(GInterfaceKits.class)).kid;
        if(kid == -1) {
            owner.player.closeInventory();
            _SOUND_ERROR(owner.player);

            return;
        }

        DBKitObj kit = DBBufferKits.BUFFER.getKit(kid);
        if(kit == null) {
            owner.player.closeInventory();
            _SOUND_ERROR(owner.player);

            return;
        }

        int idx = 0;
        for(ItemStack item : kit.slots) {
            if(item == null)
                continue;

            setItem(idx, item);
            idx++;
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void click(int slot) {
        if(slot == 35) {
            owner.player.closeInventory();
            Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
                owner.openI(GInterfaceKits.class);
            }, 2L);

            return;
        }

        _SOUND_CLICK(owner.player);
    }
}