package me.pk2.canalosaland.interfaces;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.buffer.DBBufferKits;
import me.pk2.canalosaland.db.obj.DBKitObj;
import me.pk2.canalosaland.db.obj.DBUserKitObj;
import me.pk2.canalosaland.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Map;

import static me.pk2.canalosaland.util.Wrapper.*;

public class GInterfacePhoneKits extends GInterface {
    private boolean claiming;
    public GInterfacePhoneKits(User user) {
        super(user, "&9&lTus kits", 3);

        claiming = false;
    }

    @Override
    public void init() {

    }

    DBUserKitObj[] bufferUserKits;

    @Override
    public void open() {
        super.open();
        _SOUND_OPEN(owner.player);
        if(owner.getKits() == null) {
            owner.player.sendMessage(_COLOR("&cTu cuenta esta aun siendo cargada en la base de datos. Por favor, espera unos segundos y vuelve a intentarlo."));
            owner.player.closeInventory();
            _SOUND_ERROR(owner.player);
            return;
        }

        if(claiming) {
            owner.player.closeInventory();
            owner.player.sendMessage(_COLOR("&c&lYa estas reclamando un kit!"));
            _SOUND_ERROR(owner.player);
            return;
        }

        // RESET
        for(int i = 0; i < 27; i++)
            setItem(i, Material.BLACK_STAINED_GLASS_PANE, 0, "&0", "");

        bufferUserKits = owner.getKits();
        for(int i = 0; i < bufferUserKits.length; i++) {
            DBUserKitObj ukit = bufferUserKits[i];
            if(ukit == null)
                continue;

            DBKitObj kit = DBBufferKits.BUFFER.getKit(ukit.kid);
            setItem(i, kit.material.getType(), kit.material.getDurability(), Math.min(ukit.amount, 64), _COLOR(kit.name), "&d" + ukit.amount + " uso" + ((ukit.amount>1)?"s":"") + " restante" + ((ukit.amount>1)?"s.":"."), "", "&7Haz clic para usarlo");
        }
    }

    @Override
    public void click(int slot) {
        if(claiming || owner.getKits() == null)
            return;
        if(slot >= bufferUserKits.length)
            return;

        DBUserKitObj ukit = bufferUserKits[slot];
        if(ukit == null)
            return;

        DBKitObj kit = DBBufferKits.BUFFER.getKit(ukit.kid);
        if(kit == null)
            return;

        if(ukit.amount <= 0)
            return;

        claiming = true;
        owner.player.closeInventory();
        owner.player.sendMessage(_COLOR("&aReclamando kit " + kit.name + "&a..."));
        _SOUND_PAGE(owner.player);

        DBApi.enqueue(() -> {
            try {
                Connection conn = DBApi.connect();
                int exCode = DBApi.API.users_kits.remove(conn, owner.getUserId(), kit.id, 1);
                owner.fetchData();
                DBApi.disconnect(conn);

                if(exCode != 1)
                    throw new Exception("Error while claiming kit " + kit.name + " for " + owner.player.getName() + ": " + exCode + " code. {kit=[id=" + kit.id + ",name=" + kit.name + "],ukit=[id=" + ukit.id + ",uid=" + ukit.uid + ",kid=" + ukit.kid + "]}");

                ItemStack[] slots = Arrays.stream(kit.slots).filter(item -> item != null && item.getType() != Material.AIR).toArray(ItemStack[]::new);
                Bukkit.getScheduler().runTask(CanelonesCore.INSTANCE, () -> {
                    Map<Integer, ItemStack> left = owner.player.getInventory().addItem(slots);
                    if(!left.isEmpty())
                        for(ItemStack item : left.values())
                            owner.player.getWorld().dropItem(owner.player.getLocation(), item);
                });

                owner.player.sendMessage(_COLOR("&aKit " + kit.name + " &areclamado!"));
                _SOUND_EXP(owner.player);
            } catch (Exception ex) {
                _LOG("GInterfacePhoneKits", "Error while claiming kit " + kit.name + " for " + owner.player.getName() + ": " + ex.getMessage());
                owner.player.sendMessage(_COLOR("&cError al reclamar kit " + kit.name + "&c[" + kit.id + "," + ukit.id + "," + ukit.uid + "," + ukit.kid + "]! Contacta con un administrador."));
                _SOUND_ERROR(owner.player);
            }

            claiming = false;
        });
    }
}