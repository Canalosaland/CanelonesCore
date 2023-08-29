package me.pk2.canalosaland.db;

import me.pk2.canalosaland.db.obj.DBKitObj;
import me.pk2.canalosaland.db.obj.DBUserKitObj;
import me.pk2.canalosaland.util.BukkitSerialization;
import org.apache.commons.lang3.SerializationUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.*;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static me.pk2.canalosaland.util.Wrapper.*;

public class DBApi {
    private static ExecutorService API_QUEUE;

    public static void init() {
        _LOG("DBApi", "Starting API...");

        API_QUEUE = Executors.newFixedThreadPool(1);

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
        }

        _LOG("DBApi", "API started!");
    }

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+_CONFIG("data/canelones.db"));
        } catch (SQLException e) {
            _LOG("DBApi", "Could not connect to database! " + e.getMessage());
        }

        return connection;
    }

    public static void enqueue(Runnable runnable) {
        API_QUEUE.submit(runnable);
    }

    public static class API {
        public static class users {
            public static int register(Connection conn, Player player) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(username,uuid) VALUES(?,?)");
                    stmt.setString(1, player.getName());
                    stmt.setString(2, _UUID(player));
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not register user! " + e.getMessage());
                    return 0;
                }

                return 1;
            }
            public static int exists(Connection conn, Player player) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE uuid=?");
                    stmt.setString(1, _UUID(player));
                    return stmt.executeQuery().next() ? 1 : 0;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not check if user exists! " + e.getMessage());
                    return 0;
                }
            }
            public static int getId(Connection conn, Player player) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE uuid=?");
                    stmt.setString(1, _UUID(player));
                    return stmt.executeQuery().getInt("id");
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not get user id! " + e.getMessage());
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
                    Blob blobItems = conn.createBlob();
                    blobItems.setBytes(1, items);

                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO kits(name,slots,material) VALUES(?,?,?)");
                    stmt.setString(1, name);
                    stmt.setBlob(2, blobItems);
                    stmt.executeUpdate();
                    return 1;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not register kit! " + e.getMessage());
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

                    DBKitObj[] kits = new DBKitObj[rs.getFetchSize()];

                    int i = 0;
                    while(rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        ItemStack[] slots = BukkitSerialization.deserializeItems(rs.getBlob("slots").getBytes(1, (int) rs.getBlob("slots").length()));
                        ItemStack material = BukkitSerialization.deserializeItems(rs.getBlob("material").getBytes(1, (int) rs.getBlob("material").length()))[0];

                        kits[i] = new DBKitObj(id, name, slots, material);
                        i++;
                    }

                    return kits;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not get kits! " + e.getMessage());
                    return new DBKitObj[0];
                }
            }
        }

        public static class users_kits {
            public static DBUserKitObj[] getByUid(Connection conn, int uid) {
                if(conn == null)
                    return new DBUserKitObj[0];
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users_kits WHERE uid=?");
                    stmt.setInt(1, uid);
                    ResultSet rs = stmt.executeQuery();

                    DBUserKitObj[] kits = new DBUserKitObj[rs.getFetchSize()];

                    int i = 0;
                    while(rs.next()) {
                        int id = rs.getInt("id");
                        int uid2 = rs.getInt("uid");
                        int kitId = rs.getInt("kid");
                        int amount = rs.getInt("amount");

                        kits[i] = new DBUserKitObj(id, uid2, kitId, amount);
                        i++;
                    }

                    return kits;
                } catch (SQLException e) {
                    _LOG("DBApi", "Could not get user kits! " + e.getMessage());
                    return new DBUserKitObj[0];
                }
            }
            public static int add(Connection conn, int uid, int kitId, int amount) {
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users_kits WHERE uid=? AND kid=?");
                    stmt.setInt(1, uid);
                    stmt.setInt(2, kitId);

                    ResultSet rs = stmt.executeQuery();
                    if(rs.next()) {
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
                if(conn == null)
                    return -1;
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users_kits WHERE uid=? AND kid=?");
                    stmt.setInt(1, uid);
                    stmt.setInt(2, kitId);

                    ResultSet rs = stmt.executeQuery();
                    if(rs.next()) {
                        PreparedStatement stmt2 = conn.prepareStatement("UPDATE users_kits SET amount=? WHERE uid=? AND kid=?");
                        stmt2.setInt(1, rs.getInt("amount") - amount);
                        stmt2.setInt(2, uid);
                        stmt2.setInt(3, kitId);
                        stmt2.executeUpdate();

                        if(rs.getInt("amount") - amount <= 0) {
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
        }
    }
}