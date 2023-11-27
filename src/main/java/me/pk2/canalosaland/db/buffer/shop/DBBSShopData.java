package me.pk2.canalosaland.db.buffer.shop;

import me.pk2.canalosaland.db.buffer.shop.items.DBBSShopItem;
import me.pk2.canalosaland.db.buffer.shop.items.DBBSShopItemCommand;
import me.pk2.canalosaland.db.buffer.shop.items.DBBSShopItemStack;
import me.pk2.canalosaland.db.obj.shops.DBShopData;
import me.pk2.canalosaland.db.obj.shops.items.DBSItem;
import me.pk2.canalosaland.db.obj.shops.items.DBSItemCommand;
import me.pk2.canalosaland.db.obj.shops.items.DBSItemStack;

import java.util.ArrayList;

public class DBBSShopData {
    private final DBShopData dbShopData;
    private ArrayList<DBBSShopItem> items;
    public DBBSShopData(DBShopData dbShopData) {
        this.dbShopData = dbShopData;
        this.items = new ArrayList<>();

        for(DBSItem item : dbShopData.getItems())
            if(item instanceof DBSItemCommand)
                items.add(new DBBSShopItemCommand((DBSItemCommand)item));
            else items.add(new DBBSShopItemStack((DBSItemStack)item));
    }

    public DBShopData getDbShopData() { return dbShopData; }

    public void insertItem(DBBSShopItem item) { items.add(item); }
    public void removeItem(int idx) { items.remove(idx); }
    public void modifyItem(int idx, DBBSShopItem item) { items.set(idx, item); }
    public ArrayList<DBBSShopItem> getItems() { return items; }

    public void updateLocal() {
        //
    }
}