package me.pk2.canalosaland.db;

import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import me.pk2.canalosaland.db.obj.*;
import me.pk2.canalosaland.db.obj.mb.DBMysteryBoxLocationObj;
import me.pk2.canalosaland.db.obj.mb.DBMysteryBoxObj;
import me.pk2.canalosaland.db.obj.mb.action.MysteryBoxAction;
import me.pk2.canalosaland.util.BukkitSerialization;
import org.apache.commons.lang3.SerializationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static me.pk2.canalosaland.util.Wrapper.*;

public class DBApi {
    private static ExecutorService API_QUEUE;

    public static void init() {
        _LOG("DBApi", "Starting API...");

        API_QUEUE = Executors.newFixedThreadPool(1);

        /* SQLITE HAS PROBLEMS WITH LOCKS AND STUFF I DONT LIKE IT
        _LOG("DBApi", "Checking for file...");

        File file = new File(_CONFIG("data/canelones.db"));
        if(!file.exists()) {
            _LOG("DBApi", "File not found, creating...");
            try {
                file.getParentFile().mkdirs();

                InputStream is = DBApi.class.getClassLoader().getResourceAsStream("data/canelones.db");
                assert is != null;

                Files.copy(is, file.toPath());

                _LOG("DBApi", "File created!");
            } catch (Exception ex) {
                _LOG("DBApi", "Could not create file! " + ex.getMessage());
            }
        }*/

        _LOG("DBApi", "API started!");
    }

    public static Connection connect() {
        Connection connection = null;
        try {
            //connection = DriverManager.getConnection("jdbc:sqlite:"+_CONFIG("data/canelones.db"));
            connection = DriverManager.getConnection("jdbc:mysql://" + ConfigMainBuffer.buffer.database.host + "/" + ConfigMainBuffer.buffer.database.schema + "?useSSL=false", ConfigMainBuffer.buffer.database.username, ConfigMainBuffer.buffer.database.password);
        } catch (SQLException e) {
            _LOG("DBApi", "Could not connect to database! " + e.getMessage());
        }

        return connection;
    }

    public static void disconnect(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            _LOG("DBApi", "Could not disconnect from database! " + e.getMessage());
        }
    }

    public static void enqueue(Runnable runnable) {
        API_QUEUE.submit(runnable);
    }

    public static class API {
        public static class users {
            public static int register(Connection conn, String name, String uuid) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(username,uuid) VALUES(?,?)");
                    stmt.setString(1, name);
                    stmt.setString(2, uuid);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not register user! " + e.getMessage());
                    return 0;
                }

                return 1;
            }
            public static int exists(Connection conn, String uuid) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE uuid=?");
                    stmt.setString(1, uuid);
                    return stmt.executeQuery().next() ? 1 : 0;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not check if user exists! " + e.getMessage());
                    return 0;
                }
            }
            public static int getId(Connection conn, String uuid) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE uuid=?");
                    stmt.setString(1, uuid);

                    ResultSet set = stmt.executeQuery();
                    if(!set.next())
                        throw new SQLException("User not found.");
                    return set.getInt("id");
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not get user id! " + e.getMessage());
                    return 0;
                }
            }

            public static DBUserDataObj getData(Connection conn, int uid) {
                if(conn == null)
                    return null;
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id=?");
                    stmt.setInt(1, uid);

                    ResultSet set = stmt.executeQuery();
                    if(!set.next())
                        throw new SQLException("User not found.");

                    DBUserDataObj data = new DBUserDataObj(
                            uid,
                            set.getString("job"),
                            set.getString("locale"));
                    return data;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not get user locale! " + e.getMessage());
                    return null;
                }
            }
            public static int changeLocale(Connection conn, int uid, String locale) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("UPDATE users SET locale=? WHERE id=?");
                    stmt.setString(1, locale);
                    stmt.setInt(2, uid);
                    stmt.executeUpdate();
                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not change user locale! " + e.getMessage());
                    return 0;
                }
            }

            public static int changeJob(Connection conn, int uid, String job) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("UPDATE users SET job=? WHERE id=?");
                    stmt.setString(1, job);
                    stmt.setInt(2, uid);
                    stmt.executeUpdate();
                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not change user job! " + e.getMessage());
                    return 0;
                }
            }
        }

        public static class kits {
            public static int register(Connection conn, String name, ItemStack[] slots, ItemStack mat) {
                if(conn == null)
                    return -1;

                byte[] items = BukkitSerialization.serializeItems(slots);
                byte[] material = BukkitSerialization.serializeItems(mat);
                try {
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO kits(name,slots,material) VALUES(?,?,?)");
                    stmt.setString(1, name);
                    stmt.setBytes(2, items);
                    stmt.setBytes(3, material);
                    stmt.executeUpdate();
                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not register kit! " + e.getMessage());
                    e.printStackTrace();
                    return 0;
                }
            }
            public static int modify(Connection conn, int kid, ItemStack[] slots, ItemStack mat) {
                if(conn == null)
                    return -1;

                byte[] items = BukkitSerialization.serializeItems(slots);
                byte[] material = BukkitSerialization.serializeItems(mat);
                try {
                    PreparedStatement stmt = conn.prepareStatement("UPDATE kits SET slots=?,material=? WHERE id=?");
                    stmt.setBytes(1, items);
                    stmt.setBytes(2, material);
                    stmt.setInt(3, kid);
                    stmt.executeUpdate();
                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not modify kit! " + e.getMessage());
                    e.printStackTrace();
                    return 0;
                }
            }
            public static int exists(Connection conn, String name) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM kits WHERE name=?");
                    stmt.setString(1, name);
                    return stmt.executeQuery().next() ? 1 : 0;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not check if kit exists! " + e.getMessage());
                    return 0;
                }
            }
            public static DBKitObj[] getKits(Connection conn) {
                if(conn == null)
                    return new DBKitObj[0];
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM kits");
                    ResultSet rs = stmt.executeQuery();

                    ArrayList<DBKitObj> kits = new ArrayList<>();

                    int i = 0;
                    while(rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        ItemStack[] slots = BukkitSerialization.deserializeItems(rs.getBytes("slots"));
                        ItemStack material = BukkitSerialization.deserializeItems(rs.getBytes("material"))[0];

                        kits.add(new DBKitObj(id, name, slots, material));
                        i++;
                    }

                    return kits.toArray(new DBKitObj[0]);
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not get kits! " + e.getMessage());
                    return new DBKitObj[0];
                }
            }
            public static int remove(Connection conn, int id) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM kits WHERE id=?");
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not remove kit! " + e.getMessage());
                    return 0;
                }
            }
        }

        public static class users_kits {
            public static DBUserKitObj[] getByUid(Connection conn, int uid) {
                if (conn == null)
                    return new DBUserKitObj[0];
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users_kits WHERE uid=?");
                    stmt.setInt(1, uid);
                    ResultSet rs = stmt.executeQuery();

                    ArrayList<DBUserKitObj> kits = new ArrayList<>();

                    int i = 0;
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        int uid2 = rs.getInt("uid");
                        int kitId = rs.getInt("kid");
                        int amount = rs.getInt("amount");

                        kits.add(new DBUserKitObj(id, uid2, kitId, amount));
                        i++;
                    }

                    return kits.toArray(new DBUserKitObj[0]);
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not get user kits! " + e.getMessage());
                    return new DBUserKitObj[0];
                }
            }
            public static int add(Connection conn, int uid, int kitId, int amount) {
                if (conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users_kits WHERE uid=? AND kid=?");
                    stmt.setInt(1, uid);
                    stmt.setInt(2, kitId);

                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        PreparedStatement stmt2 = conn.prepareStatement("UPDATE users_kits SET amount=? WHERE uid=? AND kid=?");
                        stmt2.setInt(1, rs.getInt("amount") + amount);
                        stmt2.setInt(2, uid);
                        stmt2.setInt(3, kitId);
                        stmt2.executeUpdate();
                        return 1;
                    }

                    PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO users_kits(uid,kid,amount) VALUES(?,?,?)");
                    stmt2.setInt(1, uid);
                    stmt2.setInt(2, kitId);
                    stmt2.setInt(3, amount);
                    stmt2.executeUpdate();
                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not add user kit! " + e.getMessage());
                    return 0;
                }
            }
            public static int remove(Connection conn, int uid, int kitId, int amount) {
                if (conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users_kits WHERE uid=? AND kid=?");
                    stmt.setInt(1, uid);
                    stmt.setInt(2, kitId);

                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        PreparedStatement stmt2 = conn.prepareStatement("UPDATE users_kits SET amount=? WHERE uid=? AND kid=?");
                        stmt2.setInt(1, rs.getInt("amount") - amount);
                        stmt2.setInt(2, uid);
                        stmt2.setInt(3, kitId);
                        stmt2.executeUpdate();

                        if (rs.getInt("amount") - amount <= 0) {
                            PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM users_kits WHERE uid=? AND kid=?");
                            stmt3.setInt(1, uid);
                            stmt3.setInt(2, kitId);
                            stmt3.executeUpdate();
                        }
                        return 1;
                    }

                    return 0;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not remove user kit! " + e.getMessage());
                    return 0;
                }
            }
            public static int deleteAll(Connection conn, int kitId) {
                if (conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM users_kits WHERE kid=?");
                    stmt.setInt(1, kitId);
                    stmt.executeUpdate();
                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not delete all user kits! " + e.getMessage());
                    return 0;
                }
            }
        }

        public static class homes {
            public static DBHomeObj[] getByUid(Connection conn, int uid) {
                if(conn == null)
                    return new DBHomeObj[0];
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM homes WHERE uid=?");
                    stmt.setInt(1, uid);
                    ResultSet rs = stmt.executeQuery();

                    ArrayList<DBHomeObj> homes = new ArrayList<>();

                    int i = 0;
                    while(rs.next()) {
                        int id = rs.getInt("id");
                        int uid2 = rs.getInt("uid");
                        String name = rs.getString("name");
                        String world = rs.getString("world");
                        double x = rs.getDouble("x");
                        double y = rs.getDouble("y");
                        double z = rs.getDouble("z");
                        float yaw = rs.getFloat("yaw");
                        float pitch = rs.getFloat("pitch");

                        homes.add(new DBHomeObj(id, uid2, name, new Location(_WORLD_OR_DEFAULT(world), x, y, z, yaw, pitch)));
                        i++;
                    }

                    return homes.toArray(new DBHomeObj[0]);
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not get homes! " + e.getMessage());
                    return new DBHomeObj[0];
                }
            }
            public static int existsHome(Connection conn, int uid, String name) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM homes WHERE uid=? AND name=?");
                    stmt.setInt(1, uid);
                    stmt.setString(2, name);
                    return stmt.executeQuery().next() ? 1 : 0;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not check if home exists! " + e.getMessage());
                    return 0;
                }
            }
            public static int addHome(Connection conn, int uid, String name, Location loc) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO homes(uid,name,world,x,y,z,yaw,pitch) VALUES(?,?,?,?,?,?,?,?)");
                    stmt.setInt(1, uid);
                    stmt.setString(2, name);
                    stmt.setString(3, loc.getWorld().getName());
                    stmt.setDouble(4, loc.getX());
                    stmt.setDouble(5, loc.getY());
                    stmt.setDouble(6, loc.getZ());
                    stmt.setFloat(7, loc.getYaw());
                    stmt.setFloat(8, loc.getPitch());
                    stmt.executeUpdate();
                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not add home! " + e.getMessage());
                    return 0;
                }
            }
            public static int removeHome(Connection conn, int uid, String name) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM homes WHERE uid=? AND name=?");
                    stmt.setInt(1, uid);
                    stmt.setString(2, name);
                    stmt.executeUpdate();
                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not remove home! " + e.getMessage());
                    return 0;
                }
            }
        }

        public static class mystery_boxes {
            public static DBMysteryBoxObj[] getMysteryBoxes(Connection conn) {
                if(conn == null)
                    return new DBMysteryBoxObj[0];
                try {
                    List<DBMysteryBoxObj> ret = new ArrayList<>();
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM mystery_boxes");

                    ResultSet set = stmt.executeQuery();
                    while(set.next()) {
                        int id = set.getInt("id");
                        String name = set.getString("name");
                        MysteryBoxAction[] slots = (MysteryBoxAction[])SerializationUtils.deserialize(set.getBytes("slots"));
                        ItemStack material = BukkitSerialization.deserializeItems(set.getBytes("material"))[0];

                        DBMysteryBoxObj mbObj = new DBMysteryBoxObj(id, name, slots, material);
                        ret.add(mbObj);
                    }

                    return ret.toArray(DBMysteryBoxObj[]::new);
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not get mb! " + e.getMessage());
                    return new DBMysteryBoxObj[0];
                }
            }
            public static int register(Connection conn, DBMysteryBoxObj obj) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO mystery_boxes(name,slots,material) VALUES(?,?,?)");
                    stmt.setString(1, obj.getName());
                    stmt.setBytes(2, SerializationUtils.serialize(obj.getSlots()));
                    stmt.setBytes(3, BukkitSerialization.serializeItems(obj.getMaterial()));
                    stmt.executeUpdate();

                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not register mb! " + e.getMessage());
                    return -1;
                }
            }
            public static int modify(Connection conn, DBMysteryBoxObj obj) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("UPDATE mystery_boxes SET name=?,slots=?,material=? WHERE id=?");
                    stmt.setString(1, obj.getName());
                    stmt.setBytes(2, SerializationUtils.serialize(obj.getSlots()));
                    stmt.setBytes(3, BukkitSerialization.serializeItems(obj.getMaterial()));
                    stmt.setInt(4, obj.getId());
                    stmt.executeUpdate();

                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not modify mb! " + e.getMessage());
                    return -1;
                }
            }
            public static DBMysteryBoxObj get(Connection conn, int id) {
                if(conn == null)
                    return null;
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM mystery_boxes WHERE id=?");
                    stmt.setInt(1, id);

                    ResultSet set = stmt.executeQuery();
                    if(set.next()) {
                        String name = set.getString("name");
                        MysteryBoxAction[] slots = (MysteryBoxAction[])SerializationUtils.deserialize(set.getBytes("slots"));
                        ItemStack material = BukkitSerialization.deserializeItems(set.getBytes("material"))[0];

                        DBMysteryBoxObj obj = new DBMysteryBoxObj(id, name, slots, material);
                        return obj;
                    }

                    return null;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not get mb! " + e.getMessage());
                    return null;
                }
            }
            public static int remove(Connection conn, int id) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM mystery_boxes WHERE id=?");
                    stmt.setInt(1, id);
                    stmt.executeUpdate();

                    return 1;
                } catch(SQLException e) {
                    _LOG("DBApi", "Could not delete mb! " + e.getMessage());
                    return -1;
                }
            }
        }

        public static class mystery_boxes_location {
            public static DBMysteryBoxLocationObj[] getLocations(Connection conn) {
                if(conn == null)
                    return new DBMysteryBoxLocationObj[0];
                try {
                    List<DBMysteryBoxLocationObj> ret = new ArrayList<>();
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM mystery_boxes_locations");

                    ResultSet set = stmt.executeQuery();
                    while(set.next())
                        ret.add(new DBMysteryBoxLocationObj(set.getInt("id"), new Location(_WORLD_OR_DEFAULT(set.getString("world")), set.getInt("x"), set.getInt("y"), set.getInt("z"))));
                    return ret.toArray(DBMysteryBoxLocationObj[]::new);
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not get mb locations! " + e.getMessage());
                    return new DBMysteryBoxLocationObj[0];
                }
            }
            public static int register(Connection conn, Location location) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO mystery_boxes_locations(x,y,z,world) VALUES (?,?,?,?)");
                    stmt.setInt(1, location.getBlockX());
                    stmt.setInt(2, location.getBlockY());
                    stmt.setInt(3, location.getBlockZ());
                    stmt.setString(4, location.getWorld().getName());
                    stmt.executeUpdate();

                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not register mb location! " + e.getMessage());
                    return -1;
                }
            }
            public static int remove(Connection conn, int id) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM mystery_boxes_locations WHERE id=?");
                    stmt.setInt(1, id);
                    stmt.executeUpdate();

                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not remove mb location! " + e.getMessage());
                    return -1;
                }
            }
            public static int modify(Connection conn, DBMysteryBoxLocationObj obj) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("UPDATE mystery_boxes_locations SET x=?,y=?,z=?,world=? WHERE id=?");
                    stmt.setInt(1, obj.getLocation().getBlockX());
                    stmt.setInt(2, obj.getLocation().getBlockY());
                    stmt.setInt(3, obj.getLocation().getBlockZ());
                    stmt.setString(4, obj.getLocation().getWorld().getName());
                    stmt.setInt(5, obj.getId());
                    stmt.executeUpdate();

                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not modify mb location! " + e.getMessage());
                    return -1;
                }
            }
        }

        public static class users_mb {
            public static DBUserMBObj[] getByUid(Connection conn, int uid) {
                if (conn == null)
                    return new DBUserMBObj[0];
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users_mb WHERE uid=?");
                    stmt.setInt(1, uid);
                    ResultSet rs = stmt.executeQuery();

                    ArrayList<DBUserMBObj> kits = new ArrayList<>();

                    int i = 0;
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        int uid2 = rs.getInt("uid");
                        int mId = rs.getInt("mid");
                        int amount = rs.getInt("amount");

                        kits.add(new DBUserMBObj(id, uid2, mId, amount));
                        i++;
                    }

                    return kits.toArray(new DBUserMBObj[0]);
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not get user mb! " + e.getMessage());
                    return new DBUserMBObj[0];
                }
            }
            public static int add(Connection conn, int uid, int mId, int amount) {
                if (conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users_mb WHERE uid=? AND mid=?");
                    stmt.setInt(1, uid);
                    stmt.setInt(2, mId);

                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        PreparedStatement stmt2 = conn.prepareStatement("UPDATE users_mb SET amount=? WHERE uid=? AND mid=?");
                        stmt2.setInt(1, rs.getInt("amount") + amount);
                        stmt2.setInt(2, uid);
                        stmt2.setInt(3, mId);
                        stmt2.executeUpdate();
                        return 1;
                    }

                    PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO users_mb(uid,mid,amount) VALUES(?,?,?)");
                    stmt2.setInt(1, uid);
                    stmt2.setInt(2, mId);
                    stmt2.setInt(3, amount);
                    stmt2.executeUpdate();
                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not add user mb! " + e.getMessage());
                    return 0;
                }
            }
            public static int remove(Connection conn, int uid, int mId, int amount) {
                if (conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users_mb WHERE uid=? AND mid=?");
                    stmt.setInt(1, uid);
                    stmt.setInt(2, mId);

                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        PreparedStatement stmt2 = conn.prepareStatement("UPDATE users_mb SET amount=? WHERE uid=? AND mid=?");
                        stmt2.setInt(1, rs.getInt("amount") - amount);
                        stmt2.setInt(2, uid);
                        stmt2.setInt(3, mId);
                        stmt2.executeUpdate();

                        if (rs.getInt("amount") - amount <= 0) {
                            PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM users_mb WHERE uid=? AND mid=?");
                            stmt3.setInt(1, uid);
                            stmt3.setInt(2, mId);
                            stmt3.executeUpdate();
                        }
                        return 1;
                    }

                    return 0;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not remove user mb! " + e.getMessage());
                    return 0;
                }
            }
            public static int deleteAll(Connection conn, int mId) {
                if (conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM users_mb WHERE mid=?");
                    stmt.setInt(1, mId);
                    stmt.executeUpdate();
                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not delete all user mb! " + e.getMessage());
                    return 0;
                }
            }
        }

        public static class shops {
            // TODO: Make db communication.
        }
    }
}