package me.pk2.canalosaland.config.buff;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.CanelonesCore;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ConfigMainBuffer {
    public static YamlConfiguration CONFIG = null;

    public static class buffer {
        public static class tablist {
            public static boolean enabled;
            public static int update;
            public static List<String> header;
            public static List<String> footer;
        }
        public static class messages {
            public static class join {
                public static boolean enabled;
                public static String message;
            }
            public static class leave {
                public static boolean enabled;
                public static String message;
            }
            public static class chat {
                public static boolean enabled;
                public static String message;
            }
        }
        public static class pm_tcom {
            public static boolean enabled;
        }
        public static class baltop {
            public static int update;
        }
        public static class webapi {
            public static boolean enabled;
            public static int port;
        }
        public static String server_prefix;
        public static class halal_mode {
            public static boolean enabled;
            public static class activate {
                public static boolean pork;
            }
            public static List<String> action;
        }
    }

    public static void load() {
        _LOG("config.yml", "Loading...");

        File file = new File(_CONFIG("config.yml"));
        if(!file.exists())
            saveDefault();

        CONFIG = YamlConfiguration.loadConfiguration(file);
        // Tablist
        buffer.tablist.enabled = CONFIG.getBoolean("tablist.enabled");
        buffer.tablist.update = CONFIG.getInt("tablist.update");
        buffer.tablist.header = CONFIG.getStringList("tablist.header");
        buffer.tablist.footer = CONFIG.getStringList("tablist.footer");
        // Messages
        buffer.messages.join.enabled = CONFIG.getBoolean("messages.join.enabled");
        buffer.messages.join.message = CONFIG.getString("messages.join.message");

        buffer.messages.leave.enabled = CONFIG.getBoolean("messages.leave.enabled");
        buffer.messages.leave.message = CONFIG.getString("messages.leave.message");

        buffer.messages.chat.enabled = CONFIG.getBoolean("messages.chat.enabled");
        buffer.messages.chat.message = CONFIG.getString("messages.chat.message");
        // PM_TCOM
        buffer.pm_tcom.enabled = CONFIG.getBoolean("pm_tcom.enabled");
        // Baltop
        buffer.baltop.update = CONFIG.getInt("baltop.update");
        // WebAPI
        buffer.webapi.enabled = CONFIG.getBoolean("webapi.enabled");
        buffer.webapi.port = CONFIG.getInt("webapi.port");
        // Server prefix
        buffer.server_prefix = CONFIG.getString("server_prefix");
        // Halal mode
        buffer.halal_mode.enabled = CONFIG.getBoolean("halal_mode.enabled");

        buffer.halal_mode.activate.pork = CONFIG.getBoolean("halal_mode.activate.pork");

        buffer.halal_mode.action = CONFIG.getStringList("halal_mode.action");

        _LOG("config.yml", "Config loaded!");
    }

    public static void save() {
        _LOG("config.yml", "Saving...");

        // Tablist
        CONFIG.set("tablist.enabled", buffer.tablist.enabled);
        CONFIG.set("tablist.update", buffer.tablist.update);
        CONFIG.set("tablist.header", buffer.tablist.header);
        CONFIG.set("tablist.footer", buffer.tablist.footer);
        // Messages
        CONFIG.set("messages.join.enabled", buffer.messages.join.enabled);
        CONFIG.set("messages.join.message", buffer.messages.join.message);

        CONFIG.set("messages.leave.enabled", buffer.messages.leave.enabled);
        CONFIG.set("messages.leave.message", buffer.messages.leave.message);

        CONFIG.set("messages.chat.enabled", buffer.messages.chat.enabled);
        CONFIG.set("messages.chat.message", buffer.messages.chat.message);
        // PM_TCOM
        CONFIG.set("pm_tcom.enabled", buffer.pm_tcom.enabled);
        // Baltop
        CONFIG.set("baltop.update", buffer.baltop.update);
        // WebAPI
        CONFIG.set("webapi.enabled", buffer.webapi.enabled);
        CONFIG.set("webapi.port", buffer.webapi.port);
        // Server prefix
        CONFIG.set("server_prefix", buffer.server_prefix);
        // Halal mode
        CONFIG.set("halal_mode.enabled", buffer.halal_mode.enabled);

        CONFIG.set("halal_mode.activate.pork", buffer.halal_mode.activate.pork);

        CONFIG.set("halal_mode.action", buffer.halal_mode.action);

        try {
            CONFIG.save(_CONFIG("config.yml"));
        } catch (Exception ex) { ex.printStackTrace(); }

        _LOG("config.yml", "Config saved!");
    }

    public static void saveDefault() {
        _LOG("config.yml", "Saving default...");

        CanelonesCore.INSTANCE.getDataFolder().mkdirs();

        PrintWriter writer;
        try {
            if(!new File(_CONFIG("config.yml")).exists())
                new File(_CONFIG("config.yml")).createNewFile();

            writer = new PrintWriter(_CONFIG("config.yml"), StandardCharsets.UTF_8);
            InputStream is = ConfigMainBuffer.class.getClassLoader().getResourceAsStream("config.yml");
            int i;

            while(true) {
                assert is != null;
                if ((i = is.read()) == -1)
                    break;

                writer.write(i);
            }

            writer.close();
            is.close();
        } catch (Exception ex) { ex.printStackTrace(); }

        _LOG("config.yml", "Default saved!");
    }
}