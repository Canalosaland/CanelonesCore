package me.pk2.canalosaland.interfaces;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.dependencies.DependencyVault;
import me.pk2.canalosaland.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class GInterfaceATM extends GInterface {
    public GInterfaceATM(User user) {
        super(user, "&eATM", 1);
    }

    @Override
    public void init() {

    }

    @Override
    public void click(int slot) {
        switch(slot) {
            case 3 -> {
                owner.player.closeInventory();
                Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> owner.openI(GInterfaceATMIn.class), 2L);

                _SOUND_CLICK(owner.player);
            }

            case 4 -> _SOUND_CLICK(owner.player);

            case 5 -> {
                owner.player.closeInventory();
                Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> owner.openI(GInterfaceATMOut.class), 2L);

                _SOUND_CLICK(owner.player);
            }

            case 8 -> {
                owner.player.closeInventory();
                _SOUND_CLOSE(owner.player);
            }
        }
    }

    @Override
    public void open() {
        super.open();

        setItem(3, Material.PAPER, 0, owner.translateC("INTERFACE_ATM_DEPOSIT_NAME"), owner.translateC("INTERFACE_ATM_DEPOSIT_LORE1"));
        setItem(4, Material.COMPASS, 0, owner.translateC("INTERFACE_ATM_BALANCE_NAME"), "&e0.00$");
        setItem(5, Material.DIAMOND_ORE, 0, owner.translateC("INTERFACE_ATM_WITHDRAW_NAME"), owner.translateC("INTERFACE_ATM_WITHDRAW_LORE1"));
        setItem(8, Material.BARRIER, 0, owner.translateC("INTERFACE_ATM_EXIT_NAME"), owner.translateC("INTERFACE_ATM_EXIT_LORE1"));

        _SOUND_OPEN(owner.player);
        setItem(4, Material.COMPASS, 0, owner.translateC("INTERFACE_ATM_BALANCE_NAME"), String.format("&e%.2f$", DependencyVault.getBalance(owner.player)));
    }
}