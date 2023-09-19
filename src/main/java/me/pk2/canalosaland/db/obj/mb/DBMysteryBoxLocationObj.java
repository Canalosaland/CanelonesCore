package me.pk2.canalosaland.db.obj.mb;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DBMysteryBoxLocationObj {
    private final int id;
    private Location location;
    private boolean inUse;
    public DBMysteryBoxLocationObj(int id, Location location) {
        this.id = id;
        this.location = location;
        this.inUse = false;
    }

    public int getId() { return id; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public boolean isInUse() { return inUse; }
    public void setInUse(boolean use) { this.inUse = use; }
    public void toggleUse() { this.inUse = !inUse; }
}