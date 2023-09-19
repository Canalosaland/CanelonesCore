package me.pk2.canalosaland.db.buffer;

import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.obj.mb.DBMysteryBoxLocationObj;
import me.pk2.canalosaland.db.obj.mb.DBMysteryBoxObj;
import org.bukkit.Location;

import java.sql.Connection;

public class DBBufferMB {
    public static final DBBufferMB BUFFER = new DBBufferMB();
    private DBMysteryBoxObj[] mbTypes;
    private DBMysteryBoxLocationObj[] mbLocations;

    public void updateMysteryBoxes() {
        Connection conn = DBApi.connect();

        DBMysteryBoxObj[] mbTypes = DBApi.API.mystery_boxes.getMysteryBoxes(conn);
        DBMysteryBoxLocationObj[] mbLocations = DBApi.API.mystery_boxes_location.getLocations(conn);

        DBApi.disconnect(conn);

        this.mbTypes = mbTypes;
        this.mbLocations = mbLocations;
    }

    public DBMysteryBoxObj[] getTypes() { return mbTypes; }
    public DBMysteryBoxLocationObj[] getLocations() { return mbLocations; }
    public DBMysteryBoxLocationObj getAt(Location location) {
        for(DBMysteryBoxLocationObj box : mbLocations)
            if(location.getBlockX() == box.getLocation().getBlockX()
            && location.getBlockY() == box.getLocation().getBlockY()
            && location.getBlockZ() == box.getLocation().getBlockZ()
            && location.getWorld().getName().contentEquals(box.getLocation().getWorld().getName()))
                return box;
        return null;
    }
    public DBMysteryBoxObj getById(int id) {
        for(final DBMysteryBoxObj box : mbTypes)
            if(box.getId() == id)
                return box;
        return null;
    }
    public DBMysteryBoxLocationObj getLocationById(int id) {
        for(final DBMysteryBoxLocationObj loc : mbLocations)
            if(loc.getId() == id)
                return loc;
        return null;
    }
}