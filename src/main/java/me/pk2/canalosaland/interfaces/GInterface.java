package me.pk2.canalosaland.interfaces;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public abstract class GInterface {
    private final Inventory inv;
    protected final User owner;

    public GInterface(User user, String name, int cols) {
        inv = Bukkit.createInventory(null, cols * 9, _COLOR(name));
        owner = user;

        init();
    }

    public abstract void init();
    public abstract void click(int slot);

    public void open() {
        owner.player.openInventory(inv);
    }

    public Inventory getInstance() {
        return inv;
    }

    protected ItemStack _item(final Material material, final short damage, final String name, final String... lore) {
        for(int i = 0; i < lore.length; i++)
            lore[i] = _COLOR(lore[i]);

        final ItemStack item = new ItemStack(material, 1, damage);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(_COLOR(name));
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    protected ItemStack _head(final Player player, final String name, final String... lore) {
        for(int i = 0; i < lore.length; i++)
            lore[i] = _COLOR(lore[i]);

        final ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        final SkullMeta meta = (SkullMeta)item.getItemMeta();

        meta.setDisplayName(_COLOR(name));
        meta.setLore(Arrays.asList(lore));
        meta.setOwningPlayer(player);

        item.setItemMeta(meta);

        return item;
    }

    protected void setItem(int idx, Material mat, int dam, String name, String... lore) {
        inv.setItem(idx, _item(mat, (short)dam, name, lore));
    }

    protected void setHead(int idx, Player player, String name, String... lore) {
        inv.setItem(idx, _head(player, name, lore));
    }
}