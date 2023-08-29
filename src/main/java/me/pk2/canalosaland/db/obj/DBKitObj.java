package me.pk2.canalosaland.db.obj;

import org.bukkit.inventory.ItemStack;

public class DBKitObj {
    public final int id;
    public final String name;
    public final ItemStack[] slots;
    public final ItemStack material;

    public DBKitObj(int id, String name, ItemStack[] slots, ItemStack material) {
        this.id = id;
        this.name = name;
        this.slots = slots;
        this.material = material;
    }
}