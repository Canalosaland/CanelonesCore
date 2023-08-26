package me.pk2.canalosaland.config.buff;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.config.buff.atm.ATMObj;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static me.pk2.canalosaland.util.Wrapper.*;

public class ConfigAtmBuffer {
    public static YamlConfiguration CONFIG = null;

    public static class buffer {
        public static class prices {
            public static double
                    rabbit_foot,
                    coal_ore,
                    iron_ore,
                    gold_ore,
                    redstone_ore,
                    golden_horse_armor,
                    nether_quartz_ore,
                    golden_apple,
                    nether_gold_ore,
                    iron_horse_ore,
                    lapis_ore,
                    diamond_horse_armor,
                    emerald,
                    shulker_shell,
                    diamond,
                    diamond_ore,
                    heart_of_the_sea,
                    emerald_ore,
                    conduit,
                    ancient_debris,
                    netherite_scrap,
                    music_disc_pigstep,
                    piglin_banner_pattern,
                    gilded_blackstone,
                    dragon_head,
                    netherite_ingot,
                    mojang_banner_pattern,
                    enchanted_golden_apple;

        }
        public static ArrayList<ATMObj> atms;
    }

    public static void remove(Location location) {
        for(int i = 0; i < buffer.atms.size(); i++) {
            ATMObj atm = buffer.atms.get(i);
            if(atm.location.equals(location)) {
                buffer.atms.remove(i);
                return;
            }
        }
    }

    public static ATMObj get(Location location) {
        for(int i = 0; i < buffer.atms.size(); i++) {
            ATMObj atm = buffer.atms.get(i);
            if(atm.location.equals(location))
                return atm;
        }

        return null;
    }

    public static void load() {
        _LOG("atm.yml", "Loading...");

        File file = new File(_CONFIG("atm.yml"));
        if(!file.exists())
            saveDefault();

        CONFIG = YamlConfiguration.loadConfiguration(file);
        buffer.prices.rabbit_foot = CONFIG.getDouble("prices.rabbit_foot");
        buffer.prices.coal_ore = CONFIG.getDouble("prices.coal_ore");
        buffer.prices.iron_ore = CONFIG.getDouble("prices.iron_ore");
        buffer.prices.gold_ore = CONFIG.getDouble("prices.gold_ore");
        buffer.prices.redstone_ore = CONFIG.getDouble("prices.redstone_ore");
        buffer.prices.golden_horse_armor = CONFIG.getDouble("prices.gold_horse_armor");
        buffer.prices.nether_quartz_ore = CONFIG.getDouble("prices.nether_quartz_ore");
        buffer.prices.golden_apple = CONFIG.getDouble("prices.golden_apple");
        buffer.prices.nether_gold_ore = CONFIG.getDouble("prices.nether_gold_ore");
        buffer.prices.iron_horse_ore = CONFIG.getDouble("prices.iron_horse_ore");
        buffer.prices.lapis_ore = CONFIG.getDouble("prices.lapis_ore");
        buffer.prices.diamond_horse_armor = CONFIG.getDouble("prices.diamond_horse_armor");
        buffer.prices.emerald = CONFIG.getDouble("prices.emerald");
        buffer.prices.shulker_shell = CONFIG.getDouble("prices.shulker_shell");
        buffer.prices.diamond = CONFIG.getDouble("prices.diamond");
        buffer.prices.diamond_ore = CONFIG.getDouble("prices.diamond_ore");
        buffer.prices.heart_of_the_sea = CONFIG.getDouble("prices.heart_of_the_sea");
        buffer.prices.emerald_ore = CONFIG.getDouble("prices.emerald_ore");
        buffer.prices.conduit = CONFIG.getDouble("prices.conduit");
        buffer.prices.ancient_debris = CONFIG.getDouble("prices.ancient_debris");
        buffer.prices.netherite_scrap = CONFIG.getDouble("prices.netherite_scrap");
        buffer.prices.music_disc_pigstep = CONFIG.getDouble("prices.music_disc_pigstep");
        buffer.prices.piglin_banner_pattern = CONFIG.getDouble("prices.piglin_banner_pattern");
        buffer.prices.gilded_blackstone = CONFIG.getDouble("prices.gilded_blackstone");
        buffer.prices.dragon_head = CONFIG.getDouble("prices.dragon_head");
        buffer.prices.netherite_ingot = CONFIG.getDouble("prices.netherite_ingot");
        buffer.prices.mojang_banner_pattern = CONFIG.getDouble("prices.mojang_banner_pattern");
        buffer.prices.enchanted_golden_apple = CONFIG.getDouble("prices.enchanted_golden_apple");

        buffer.atms = new ArrayList<>();
        for(String key : CONFIG.getConfigurationSection("atms").getKeys(false)) {
            ATMObj atm = new ATMObj(
                    CONFIG.getBoolean("atms." + key + ".enabled"), new Location(_WORLD_OR_DEFAULT(
                    CONFIG.getString("atms." + key + ".location.world")),
                    CONFIG.getInt("atms." + key + ".location.x"),
                    CONFIG.getInt("atms." + key + ".location.y"),
                    CONFIG.getInt("atms." + key + ".location.z"))
            );
            buffer.atms.add(atm);
        }
    }

    public static void save() {
        _LOG("atm.yml", "Saving...");

        CONFIG.set("prices.rabbit_foot", buffer.prices.rabbit_foot);
        CONFIG.set("prices.coal_ore", buffer.prices.coal_ore);
        CONFIG.set("prices.iron_ore", buffer.prices.iron_ore);
        CONFIG.set("prices.gold_ore", buffer.prices.gold_ore);
        CONFIG.set("prices.redstone_ore", buffer.prices.redstone_ore);
        CONFIG.set("prices.gold_horse_armor", buffer.prices.golden_horse_armor);
        CONFIG.set("prices.nether_quartz_ore", buffer.prices.nether_quartz_ore);
        CONFIG.set("prices.golden_apple", buffer.prices.golden_apple);
        CONFIG.set("prices.nether_gold_ore", buffer.prices.nether_gold_ore);
        CONFIG.set("prices.iron_horse_ore", buffer.prices.iron_horse_ore);
        CONFIG.set("prices.lapis_ore", buffer.prices.lapis_ore);
        CONFIG.set("prices.diamond_horse_armor", buffer.prices.diamond_horse_armor);
        CONFIG.set("prices.emerald", buffer.prices.emerald);
        CONFIG.set("prices.shulker_shell", buffer.prices.shulker_shell);
        CONFIG.set("prices.diamond", buffer.prices.diamond);
        CONFIG.set("prices.diamond_ore", buffer.prices.diamond_ore);
        CONFIG.set("prices.heart_of_the_sea", buffer.prices.heart_of_the_sea);
        CONFIG.set("prices.emerald_ore", buffer.prices.emerald_ore);
        CONFIG.set("prices.conduit", buffer.prices.conduit);
        CONFIG.set("prices.ancient_debris", buffer.prices.ancient_debris);
        CONFIG.set("prices.netherite_scrap", buffer.prices.netherite_scrap);
        CONFIG.set("prices.music_disc_pigstep", buffer.prices.music_disc_pigstep);
        CONFIG.set("prices.piglin_banner_pattern", buffer.prices.piglin_banner_pattern);
        CONFIG.set("prices.gilded_blackstone", buffer.prices.gilded_blackstone);
        CONFIG.set("prices.dragon_head", buffer.prices.dragon_head);
        CONFIG.set("prices.netherite_ingot", buffer.prices.netherite_ingot);
        CONFIG.set("prices.mojang_banner_pattern", buffer.prices.mojang_banner_pattern);
        CONFIG.set("prices.enchanted_golden_apple", buffer.prices.enchanted_golden_apple);

        for(int i = 0; i < buffer.atms.size(); i++) {
            ATMObj atm = buffer.atms.get(i);
            CONFIG.set("atms." + i + ".enabled", atm.enabled);
            CONFIG.set("atms." + i + ".location.world", atm.location.getWorld().getName());
            CONFIG.set("atms." + i + ".location.x", atm.location.getBlockX());
            CONFIG.set("atms." + i + ".location.y", atm.location.getBlockY());
            CONFIG.set("atms." + i + ".location.z", atm.location.getBlockZ());
        }

        try {
            CONFIG.save(new File(_CONFIG("atm.yml")));
        } catch (Exception ex) { ex.printStackTrace(); }

        _LOG("atm.yml", "Config saved!");
    }

    public static void saveDefault() {
        _LOG("atm.yml", "Saving default...");

        CanelonesCore.INSTANCE.getDataFolder().mkdirs();

        PrintWriter writer;
        try {
            if(!new File(_CONFIG("atm.yml")).exists())
                new File(_CONFIG("atm.yml")).createNewFile();

            writer = new PrintWriter(_CONFIG("atm.yml"), StandardCharsets.UTF_8);
            InputStream is = ConfigMainBuffer.class.getClassLoader().getResourceAsStream("atm.yml");
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

        _LOG("atm.yml", "Default saved!");
    }
}