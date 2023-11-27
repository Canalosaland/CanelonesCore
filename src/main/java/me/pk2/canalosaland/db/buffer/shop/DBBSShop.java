package me.pk2.canalosaland.db.buffer.shop;

import me.pk2.canalosaland.db.obj.shops.DBShop;

public class DBBSShop {
    private final DBShop dbShop;
    private final DBBSShopData shopData;

    public DBBSShop(DBShop dbShop) {
        this.dbShop = dbShop;
        this.shopData = new DBBSShopData(dbShop.getData());
    }

    public DBShop getDbShop() { return dbShop; }
    public DBBSShopData getShopData() { return shopData; }

    public void updateLocal() {
        shopData.updateLocal();
    }

    public void updateDBS() {
        shopData.updateDBS();
    }
}