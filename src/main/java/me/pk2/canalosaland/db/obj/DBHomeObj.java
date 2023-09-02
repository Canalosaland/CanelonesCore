package me.pk2.canalosaland.db.obj;

import org.bukkit.Location;

public class DBHomeObj {
    public final int id;
    public final int uid;
    public final String name;
    public final Location location;
    public DBHomeObj(int id, int uid, String name, Location location) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.location = location;
    }
}