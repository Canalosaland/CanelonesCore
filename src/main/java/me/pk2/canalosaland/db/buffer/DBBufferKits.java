package me.pk2.canalosaland.db.buffer;

import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.obj.DBKitObj;

import java.sql.Connection;

public class DBBufferKits {
    public static DBBufferKits BUFFER = new DBBufferKits();
    private DBKitObj[] kits;

    public void updateKits() {
        Connection conn = DBApi.connect();
        DBKitObj[] kits = DBApi.API.kits.getKits(conn);
        DBApi.disconnect(conn);

        this.kits = kits;
    }

    public DBKitObj[] getKits() { return kits; }
    public DBKitObj getKit(int id) {
        for(DBKitObj kit : kits)
            if(kit.id == id)
                return kit;
        return null;
    }
}