package me.pk2.canalosaland.jobs;

import static me.pk2.canalosaland.util.Wrapper.*;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public abstract class Job implements Listener {
    private final ArrayList<String> users;
    public Job() {
        this.users = new ArrayList<>();
    }

    public void join(Player p) { if(!users.contains(_UUID(p))) users.add(_UUID(p)); }
    public void quit(Player p) { users.remove(_UUID(p)); }
    public boolean is(Player p) { return users.contains(_UUID(p)); }
    public void clear() { users.clear(); }

    public abstract String getName();
    public abstract Material getMaterial();
}