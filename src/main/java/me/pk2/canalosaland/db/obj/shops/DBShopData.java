package me.pk2.canalosaland.db.obj.shops;

import me.pk2.canalosaland.db.obj.shops.items.DBSItem;

import java.io.Serializable;
import java.util.ArrayList;

public class DBShopData implements Serializable {
    private String displayName;
    private ArrayList<DBSItem> items;
    public DBShopData(String displayName, ArrayList<DBSItem> items) {
        this.displayName = displayName;
        this.items = new ArrayList<>();
    }

    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }

    public void insertItem(DBSItem item) { items.add(item); }
    public void removeItem(int idx) { items.remove(idx); }
    public void modifyItem(int idx, DBSItem item) { items.set(idx, item); }
    public ArrayList<DBSItem> getItems() { return items; }
    public void setItems(ArrayList<DBSItem> dbsItems) { items = new ArrayList<>(dbsItems); }
}