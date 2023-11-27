package me.pk2.canalosaland.db.buffer;

import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.obj.shops.DBShop;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DBBufferShops {
    public static final DBBufferShops BUFFER = new DBBufferShops();
    private DBShop[] shops;

    public void updateShops() {
        Connection conn = DBApi.connect();

        DBShop[] shops = DBApi.API.shops.getShops(conn);

        DBApi.disconnect(conn);

        this.shops = shops;
    }


}