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
    private boolean updateNeeded;
    public DBBSShopData(DBShopData dbShopData) {
        this.dbShopData = dbShopData;
        this.items = new ArrayList<>();
        this.updateNeeded = false;

        for(DBSItem item : dbShopData.getItems())
            if(item instanceof DBSItemCommand)
                items.add(new DBBSShopItemCommand((DBSItemCommand)item));
            else items.add(new DBBSShopItemStack((DBSItemStack)item));
    }

    public DBShopData getDbShopData() { return dbShopData; }

    public void insertItem(DBBSShopItem item) {
        items.add(item);

        updateNeeded = true;
    }
    public void removeItem(int idx) {
        items.remove(idx);

        updateNeeded = true;
    }
    public void modifyItem(int idx, DBBSShopItem item) {
        items.set(idx, item);

        updateNeeded = true;
    }
    public void clearItems() {
        items.clear();

        updateNeeded = true;
    }
    public ArrayList<DBBSShopItem> getItems() { return items; }
    public DBBSShopItem getItem(int idx) { return items.get(idx); }

    public void updateLocal() {
        for(DBBSShopItem item : items)
            item.updateLocal();
    }

    public void updateDBS() {
        ArrayList<DBSItem> dbsItems = new ArrayList<>();
        for(DBBSShopItem item : items) {
            item.updateDBS();
            dbsItems.add(item.asDBS());
        }

        dbShopData.setItems(dbsItems);
        updateNeeded = false;
    }

    public boolean updateNeeded() { return updateNeeded; }
    public void setUpdateNeeded() { updateNeeded = true; }
}