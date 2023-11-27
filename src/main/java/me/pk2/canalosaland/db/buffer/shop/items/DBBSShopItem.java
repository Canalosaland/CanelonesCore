package me.pk2.canalosaland.db.buffer.shop.items;

import me.pk2.canalosaland.db.obj.shops.items.DBSItem;
import me.pk2.canalosaland.util.BukkitSerialization;
import org.bukkit.inventory.ItemStack;

public abstract class DBBSShopItem {
    private final DBSItem dbsItem;
    private double price;
    private ItemStack material;
    public DBBSShopItem(DBSItem dbsItem) {
        this.dbsItem = dbsItem;
        this.price = dbsItem.getPrice();
        this.material = BukkitSerialization.deserializeItems(dbsItem.getMaterial())[0];
    }

    public void setPrice(double price) { this.price = price; }
    public double getPrice() { return price; }

    public void setMaterial(ItemStack material) { this.material = material; }
    public ItemStack getMaterial() { return material; }

    public void updateLocal() {
        this.price = dbsItem.getPrice();
        this.material = BukkitSerialization.deserializeItems(dbsItem.getMaterial())[0];
    }

    public void updateDBS() {
        dbsItem.setPrice(price);
        dbsItem.setMaterial(BukkitSerialization.serializeItems(material));
    }
}