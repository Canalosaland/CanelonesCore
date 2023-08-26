package me.pk2.canalosaland.user;

import static me.pk2.canalosaland.util.Wrapper.*;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class UserManager {
    public static final HashMap<String, User> users = new HashMap<>();

    public static User get(Player player) { return users.get(_UUID(player)); }
    public static void remove(Player player) { users.remove(_UUID(player)); }
    public static void add(Player player) { users.put(_UUID(player), new User(player)); }
}