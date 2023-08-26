package me.pk2.canalosaland.interfaces;

import static me.pk2.canalosaland.util.Wrapper.*;

import com.dbteku.telecom.models.Carrier;
import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.dependencies.DependencyTCom;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.util.Wrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.List;

public class GInterfacePhoneProviders extends GInterface {
    public GInterfacePhoneProviders(User user) {
        super(user, "&9Phone - Providers", 3);
    }

    @Override
    public void init() {
        setItem(26, Material.BARRIER, 0, "&c&lSalir", "&7Clic para ir atras.");
    }

    @Override
    public void click(int slot) {
        _SOUND_CLICK(owner.player);
        switch(slot) {
            case 26: {
                owner.player.closeInventory();
                Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
                    owner.interfaces.get(GInterfacePhone.class).open();
                }, 2L);
            } break;
        }

        Carrier current = DependencyTCom.getCarrierByPlayer(owner.player);
        if(current != null)
            return;

        List<Carrier> carriers = DependencyTCom.API.getAllCarriers();
        for(int i = 0; i < carriers.size(); i++)
            if(i == slot) {
                _SOUND_EXP(owner.player);

                carriers.get(i).subscribe(owner.player.getName());
                owner.player.closeInventory();

                Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
                    owner.interfaces.get(GInterfacePhone.class).open();
                }, 2L);
            }
    }

    @Override
    public void open() {
        super.open();
        _SOUND_OPEN(owner.player);

        Carrier current = DependencyTCom.getCarrierByPlayer(owner.player);

        int maxCarrier = DependencyTCom.API.getAllCarriers().size();
        for(int i = 0; i < 26; i++) {
            if(i >= maxCarrier) {
                setItem(i, Material.RED_STAINED_GLASS_PANE, 0, "&c&lNo hay mas proveedores.", "&7No hay mas proveedores.");
                continue;
            }

            Carrier carrier = DependencyTCom.API.getAllCarriers().get(i);
            setItem(i, Material.PAPER, 0, "&e&l" + carrier.getName(),
                    "&6Mensajes: &e" + carrier.getPricePerText() + "$",
                    "&6Personas: &e" + carrier.getSubscribers().size(),
                    "",
                    current==null?
                            "&a&lClic para darte de alta.":
                            "&c&lYa estas suscrito a un proveedor.");
        }
    }
}