package me.pk2.canalosaland.db.buffer.shop.items;

import me.pk2.canalosaland.db.obj.shops.items.DBSItemStack;
import me.pk2.canalosaland.util.BukkitSerialization;
import org.bukkit.inventory.ItemStack;

public class DBBSShopItemStack extends DBBSShopItem {
    private final DBSItemStack dbsItemStack;
    private ItemStack itemStack;
    public DBBSShopItemStack(DBSItemStack dbsItemStack) {
        super(dbsItemStack);

        this.dbsItemStack = dbsItemStack;
        this.itemStack = BukkitSerialization.deserializeItems(dbsItemStack.getItemStack())[0];
    }

    public DBSItemStack getDbsItemStack() { return dbsItemStack; }

    public void setItemStack(ItemStack itemStack) { this.itemStack = itemStack; }
    public ItemStack getItemStack() { return itemStack; }

    @Override
    public void updateLocal() {
        setPrice(dbsItemStack.getPrice());
        setMaterial(BukkitSerialization.deserializeItems(dbsItemStack.getMaterial())[0]);

        this.itemStack = BukkitSerialization.deserializeItems(dbsItemStack.getItemStack())[0];
    }

    @Override
    public void updateDBS() {
        dbsItemStack.setPrice(getPrice());
        dbsItemStack.setMaterial(BukkitSerialization.serializeItems(getMaterial()));
        dbsItemStack.setItemStack(BukkitSerialization.serializeItems(itemStack));
    }
}