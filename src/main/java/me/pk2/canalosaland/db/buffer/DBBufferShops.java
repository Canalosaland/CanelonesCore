package me.pk2.canalosaland.db.buffer;

import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.buffer.shop.DBBSShop;
import me.pk2.canalosaland.db.obj.shops.DBShop;

import java.sql.Connection;
import java.util.ArrayList;

public class DBBufferShops {
    public static final DBBufferShops BUFFER = new DBBufferShops();
    private ArrayList<DBBSShop> shops;

    public void updateShops() {
        Connection conn = DBApi.connect();

        DBShop[] shops = DBApi.API.shops.getShops(conn);

        DBApi.disconnect(conn);

        this.shops = new ArrayList<>();
        for (DBShop shop : shops)
            this.shops.add(new DBBSShop(shop));
    }

    public DBBSShop[] getShops() { return shops.toArray(DBBSShop[]::new); }
    public DBBSShop getShop(int id) {
        for(DBBSShop shop : shops)
            if(shop.getDbShop().getId() == id)
                return shop;
        return null;
    }
    public DBBSShop getShop(String name) {
        for(DBBSShop shop : shops)
            if(shop.getDbShop().getName().equals(name))
                return shop;
        return null;
    }

    public void delShop(int id) {
        for(int i = 0; i < shops.size(); i++)
            if (shops.get(i).getDbShop().getId() == id) {
                shops.remove(i);
                break;
            }
    }

    public void newShop(DBBSShop shop) {
        DBBSShop exists = getShop(shop.getDbShop().getId());
        if(exists != null)
            return;

        shops.add(shop);
    }
}