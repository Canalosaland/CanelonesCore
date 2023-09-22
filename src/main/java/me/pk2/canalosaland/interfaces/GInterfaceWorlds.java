package me.pk2.canalosaland.interfaces;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import me.pk2.canalosaland.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import static me.pk2.canalosaland.util.Wrapper.*;

public class GInterfaceWorlds extends GInterface {
    public GInterfaceWorlds(User user) {
        super(user, "&2&lTeleport", 3);
    }

    @Override
    public void init() {

    }

    @Override
    public void click(int slot) {
        if(slot < 12 || slot > 14)
            return;

        owner.player.getInventory().close();
        owner.player.teleport(slot == 12 ? ConfigMainBuffer.buffer.spawn : (slot == 13 ? ConfigMainBuffer.buffer.nether : ConfigMainBuffer.buffer.end));
        Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> _SOUND_TELEPORT(owner.player), 2L);
    }

    @Override
    public void open() {
        super.open();

        setItem(12, Material.GRASS_BLOCK, 0, "&a&lOverworld", "", owner.translateC("INTERFACE_WORLDS_CLICK") + "Overworld");
        setItem(13, Material.NETHERRACK, 0, "&c&lNether", "", owner.translateC("INTERFACE_WORLDS_CLICK") + "Nether");
        setItem(14, Material.END_STONE, 0, "&5&lEnd", "", owner.translateC("INTERFACE_WORLDS_CLICK") + "End");
        _SOUND_OPEN(owner.player);
    }
}