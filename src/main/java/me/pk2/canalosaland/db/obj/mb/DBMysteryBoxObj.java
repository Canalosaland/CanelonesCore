package me.pk2.canalosaland.db.obj.mb;

import me.pk2.canalosaland.db.obj.mb.action.MysteryBoxAction;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class DBMysteryBoxObj {
    private final int id;
    private String name;
    private MysteryBoxAction[] slots;
    private ItemStack material;
    public DBMysteryBoxObj(int id, String name, MysteryBoxAction[] slots, ItemStack material) {
        this.id = id;
        this.name = name;
        this.slots = slots;
        this.material = material;
    }

    public void setName(String name) { this.name = name; }
    public void setSlots(MysteryBoxAction... slots) { this.slots = slots; }
    public void setMaterial(ItemStack material) { this.material = material; }
    public void addSlot(MysteryBoxAction act) {
        MysteryBoxAction[] arr = new MysteryBoxAction[slots.length+1];
        System.arraycopy(slots, 0, arr, 0, slots.length);

        arr[arr.length-1] = act;
        slots = arr;
    }
    public void remSlot(int idx) {
        if(slots == null || idx < 0 || idx >= slots.length)
            return;

        MysteryBoxAction[] slots = new MysteryBoxAction[this.slots.length -1];
        for (int i = 0, k = 0; i < this.slots.length; i++) {
            if(i == idx)
                continue;
            slots[k++] = this.slots[i];
        }

        this.slots = slots;
    }

    public String getName() { return this.name; }
    public MysteryBoxAction[] getSlots() { return this.slots; }
    public ItemStack getMaterial() { return this.material; }
    public int getId() { return id; }
}