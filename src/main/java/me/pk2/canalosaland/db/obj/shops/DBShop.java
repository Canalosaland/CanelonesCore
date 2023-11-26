package me.pk2.canalosaland.db.obj.shops;

import me.pk2.canalosaland.db.obj.shops.items.DBSItem;

import java.io.Serializable;
import java.util.ArrayList;

public class DBShop implements Serializable { // TODO: Maybe change this class.
    private final int id;
    private final String name;
    private String displayName;
    private ArrayList<DBSItem> items;
    public DBShop(int id, String name, String displayName, ArrayList<DBSItem> items) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.items = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }

    public void insertItem(DBSItem item) { items.add(item); }
    public void removeItem(int idx) { items.remove(idx); }
    public void modifyItem(int idx, DBSItem item) { items.set(idx, item); }
    public ArrayList<DBSItem> getItems() { return items; }
}