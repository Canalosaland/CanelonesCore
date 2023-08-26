package me.pk2.canalosaland.config.buff.atm;

import org.bukkit.Location;

public class ATMObj {
    public boolean enabled;
    public Location location;
    public ATMObj(boolean enabled, Location location) {
        this.enabled = enabled;
        this.location = location;
    }
}