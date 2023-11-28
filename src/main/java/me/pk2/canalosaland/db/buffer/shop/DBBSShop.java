package me.pk2.canalosaland.db.buffer.shop;

import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.obj.shops.DBShop;

import java.sql.Connection;

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

    public int updateDBS() {
        shopData.updateDBS();

        Connection conn = DBApi.connect();

        int exCode = DBApi.API.shops.updateData(conn, dbShop.getId(), shopData.getDbShopData());
        if(exCode != 1) {
            DBApi.disconnect(conn);
            return exCode;
        }

        exCode = DBApi.API.shops.updateName(conn, dbShop.getId(), dbShop.getName());
        if(exCode != 1) {
            DBApi.disconnect(conn);
            return exCode;
        }

        DBApi.disconnect(conn);
        return 1;
    }
}