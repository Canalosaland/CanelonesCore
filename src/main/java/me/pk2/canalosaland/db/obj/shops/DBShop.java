package me.pk2.canalosaland.db.obj.shops;

public class DBShop {
    private final int id;
    private String name;
    private DBShopData data;
    public DBShop(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public void setData(DBShopData data) { this.data = data; }
    public DBShopData getData() { return data; }
}