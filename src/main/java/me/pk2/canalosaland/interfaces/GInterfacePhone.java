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
                "%s &e%name%",
                "%s &e%price%$",
                "",
                "%s &e%name%"
            }, {
                "%s"
            }
    };
    public GInterfacePhone(User user) {
        super(user, "&9Phone - Main", 1);
    }

    private String[][] translateLore() {
        String[][] lores = new String[GInterfacePhone.lores.length][];
        for(int i = 0; i < GInterfacePhone.lores.length; i++)
            lores[i] = Arrays.copyOf(GInterfacePhone.lores[i], GInterfacePhone.lores[i].length);
        lores[0][1] = lores[0][1].replace("%s", owner.translateC("INTERFACE_PHONE_LORE1_NAME"));
        lores[0][2] = lores[0][2].replace("%s", owner.translateC("INTERFACE_PHONE_LORE1_MESSAGES"));
        lores[0][4] = lores[0][4].replace("%s", owner.translateC("INTERFACE_PHONE_LORE1_UNSUBSCRIBE"));
        lores[1][0] = lores[1][0].replace("%s", owner.translateC("INTERFACE_PHONE_LORE2_SUBSCRIBE"));
        return lores;
    }

    @Override
    public void init() {
        String[][] lores = translateLore();
    }

    @Override
    public void click(int slot) {
        _SOUND_CLICK(owner.player);

        switch (slot) {
            case 0 -> {
                owner.player.closeInventory();
                Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
                    owner.openI(GInterfacePhoneKits.class);
                }, 2L);
            }
            case 2 -> {
                Carrier carrier = DependencyTCom.getCarrierByPlayer(owner.player);
                if (carrier == null) {
                    owner.player.closeInventory();
                    Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
                        owner.openI(GInterfacePhoneProviders.class);
                    }, 2L);
                    return;
                }

                if (carrier.getOwner().contentEquals(owner.player.getName())) {
                    owner.player.closeInventory();
                    owner.player.sendMessage(owner.translateC("INTERFACE_PHONE_PROVIDER_CANNOT_UNSUBSCRIBE_OWNER"));

                    _SOUND_CLOSE(owner.player);
                    return;
                }

                carrier.unsubscribe(owner.player.getName());
                _SOUND_EXP(owner.player);

                Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
                    owner.openI(GInterfacePhone.class);
                }, 2L);
            }
            case 3 -> {
                owner.player.closeInventory();
                Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
                    owner.openI(GInterfacePhoneProviders.class);
                }, 2L);
            }
            case 5 -> {
                owner.player.closeInventory();
                Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
                    owner.openI(GInterfacePhoneBizum.class);
                }, 2L);
            }
            case 8 -> {
                owner.player.closeInventory();

                _SOUND_CLOSE(owner.player);
            }
            default -> {
            }
        }
    }

    @Override
    public void open() {
        super.open();
        setItem(0, Material.RED_SHULKER_BOX, 0, "&9&lKits", owner.translateC("INTERFACE_PHONE_KITS_LORE1"));
        setItem(2, Material.REDSTONE_TORCH, 0, owner.translateC("INTERFACE_PHONE_PROVIDER_NAME"), lores[1]);
        setItem(3, Material.BOOK, 0, owner.translateC("INTERFACE_PHONE_PROVIDERS_NAME"), owner.translateC("INTERFACE_PHONE_PROVIDERS_LORE1"));
        setItem(5, Material.GOLD_INGOT, 0, owner.translateC("INTERFACE_ATM_BALANCE_NAME"), "&e0.00$", "", owner.translateC("INTERFACE_PHONE_BALANCE_LORE1"));
        setItem(8, Material.BARRIER, 0, owner.translateC("INTERFACE_ATM_EXIT_NAME"), owner.translateC("INTERFACE_ATM_EXIT_LORE1"));
        _SOUND_OPEN(owner.player);

        String[][] lores = translateLore();
        Carrier carrier = DependencyTCom.getCarrierByPlayer(owner.player);
        if(carrier == null) {
            setItem(2, Material.REDSTONE_TORCH, 0, owner.translateC("INTERFACE_PHONE_PROVIDER_NAME"), lores[1]);
        } else setItem(2, Material.REDSTONE_TORCH, 0, owner.translateC("INTERFACE_PHONE_PROVIDER_NAME"), DependencyPAPI.parse(owner.player, String.join("\n", lores[0])
                .replace("%name%", carrier.getName())
                .replace("%price%", String.format("%.2f", carrier.getPricePerText())))
                        .split("\n")
            );
        setItem(5, Material.GOLD_INGOT, 0, owner.translateC("INTERFACE_ATM_BALANCE_NAME"), String.format("&e%.2f$", DependencyVault.getBalance(owner.player)), "", owner.translateC("INTERFACE_PHONE_BALANCE_LORE1"));
    }
}