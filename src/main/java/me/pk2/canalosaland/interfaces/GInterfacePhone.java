package me.pk2.canalosaland.interfaces;

import static me.pk2.canalosaland.util.Wrapper.*;

import com.dbteku.telecom.models.Carrier;
import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.dependencies.DependencyPAPI;
import me.pk2.canalosaland.dependencies.DependencyTCom;
import me.pk2.canalosaland.dependencies.DependencyVault;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.util.Wrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Arrays;

public class GInterfacePhone extends GInterface {
    private static final String[][] lores = {
            {
                "%telecom_phone_signal%",
                "&6Nombre: &e%name%",
                "&6Mensajes: &e%price%$",
                "",
                "&c&lClic para darte de baja en &e%name%"
            }, {
                "&a&lClic para darte de alta."
            }
    };
    public GInterfacePhone(User user) {
        super(user, "&9Phone - Main", 1);
    }

    @Override
    public void init() {
        setItem(3, Material.REDSTONE_TORCH, 0, "&e&lProveedor", lores[1]);
        setItem(4, Material.BOOK, 0, "&a&lProveedores", "&7Clic para ver los proveedores.");
        setItem(5, Material.GOLD_INGOT, 0, "&6&lSaldo", "&e0.00$", "", "&7Clic para abrir menu de Bizum.");
        setItem(8, Material.BARRIER, 0, "&c&lSalir", "&7Clic para salir.");
    }

    @Override
    public void click(int slot) {
        _SOUND_CLICK(owner.player);

        switch(slot) {
            case 3: {
                Carrier carrier = DependencyTCom.getCarrierByPlayer(owner.player);
                if(carrier == null) {
                    owner.player.closeInventory();
                    Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
                        owner.openI(GInterfacePhoneProviders.class);
                    }, 2L);
                    return;
                }

                if(carrier.getOwner().contentEquals(owner.player.getName())) {
                    owner.player.closeInventory();
                    owner.player.sendMessage(_COLOR("&cNo puedes darte de baja de tu propio proveedor."));

                    _SOUND_CLOSE(owner.player);
                    return;
                }

                carrier.unsubscribe(owner.player.getName());
                _SOUND_EXP(owner.player);

                Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
                    owner.openI(GInterfacePhone.class);
                }, 2L);
            } break;
            case 4: {
                owner.player.closeInventory();
                Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
                    owner.openI(GInterfacePhoneProviders.class);
                }, 2L);
            } break;
            case 5: {
                owner.player.closeInventory();
                Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
                    owner.openI(GInterfacePhoneBizum.class);
                }, 2L);
            } break;
            case 8: {
                owner.player.closeInventory();

                _SOUND_CLOSE(owner.player);
            } break;
            default: break;
        }
    }

    @Override
    public void open() {
        super.open();
        _SOUND_OPEN(owner.player);

        Carrier carrier = DependencyTCom.getCarrierByPlayer(owner.player);
        if(carrier == null) {
            setItem(3, Material.REDSTONE_TORCH, 0, "&e&lProveedor", lores[1]);
        } else setItem(3, Material.REDSTONE_TORCH, 0, "&e&lProveedor", DependencyPAPI.parse(owner.player, String.join("\n", lores[0])
                .replace("%name%", carrier.getName())
                .replace("%price%", String.format("%.2f", carrier.getPricePerText())))
                        .split("\n")
            );
        setItem(5, Material.GOLD_INGOT, 0, "&6&lSaldo", String.format("&e%.2f$", DependencyVault.getBalance(owner.player)), "", "&7Clic para abrir menu de Bizum.");
    }
}