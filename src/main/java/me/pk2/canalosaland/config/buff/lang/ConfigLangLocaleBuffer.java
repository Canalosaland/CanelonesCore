package me.pk2.canalosaland.config.buff.lang;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static me.pk2.canalosaland.util.Wrapper.*;

public class ConfigLangLocaleBuffer {
    public final String locale;
    private YamlConfiguration config;
    public ConfigLangLocaleBuffer(String locale) {
        this.locale = locale;
    }

    public String translate(String key) { return config.getString(key); }
    public String translate(Player player, String key) { return _PLACEHOLDER(player, translate(key)); }

    public String translateC(String key) { return _COLOR(translate(key)); }
    public String translateC(Player player, String key) { return _COLOR(translate(player, key)); }

    public void load() {
        _LOG("messages/" + locale + ".yml", "Loading...");
        File file = new File(_CONFIG("messages/" + locale + ".yml"));
        if(!file.exists())
            saveDefault();

        config = YamlConfiguration.loadConfiguration(new File(_CONFIG("messages/" + locale + ".yml")));
    }

    public void saveDefault() {
        _LOG("messages/" + locale + ".yml", "Saving default...");

        File file = new File(_CONFIG("messages/" + locale + ".yml"));
        try {
            if(!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);
            InputStream is = getClass().getClassLoader().getResourceAsStream("messages/" + locale + ".yml");
            int i;

            while(true) {
                assert is != null;
                if ((i = is.read()) == -1)
                    break;
                writer.write(i);
            }

            writer.close();
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}