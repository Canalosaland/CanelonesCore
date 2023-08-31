package me.pk2.canalosaland.config.buff;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.config.buff.bounty.BountyObject;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static me.pk2.canalosaland.util.Wrapper._CONFIG;
import static me.pk2.canalosaland.util.Wrapper._LOG;

public class ConfigBountyBuffer {
    public static YamlConfiguration CONFIG = null;
    public static class buffer {
        public static ArrayList<BountyObject> bounties;
    }

    public static double getBounty(String player) {
        double count = 0.00;
        for(BountyObject bounty : buffer.bounties)
            if(bounty.getTarget().equalsIgnoreCase(player))
                count += bounty.getAmount();
        return count;
    }

    public static BountyObject[] getBountiesPlayer(String player) {
        return buffer.bounties.stream().filter(bounty -> bounty.getPlayer().equalsIgnoreCase(player)).toArray(BountyObject[]::new);
    }

    public static BountyObject[] getBountiesTarget(String player) {
        return buffer.bounties.stream().filter(bounty -> bounty.getTarget().equalsIgnoreCase(player)).toArray(BountyObject[]::new);
    }

    public static BountyObject[] getBountiesPlayerTarget(String player, String target) {
        return buffer.bounties.stream().filter(bounty -> bounty.getPlayer().equalsIgnoreCase(player) && bounty.getTarget().equalsIgnoreCase(target)).toArray(BountyObject[]::new);
    }

    public static void addBounty(String target, String player, double amount) {
        amount = Math.round(amount * 100.0) / 100.0;
        buffer.bounties.add(new BountyObject(target, player, amount));
    }

    public static void removeBounty(String target) {
        buffer.bounties.removeIf(bounty -> bounty.getTarget().equalsIgnoreCase(target));
    }

    public static void load() {
        _LOG("bounty.yml", "Loading...");
        File file = new File(_CONFIG("bounty.yml"));
        if(!file.exists())
            saveDefault();

        CONFIG = YamlConfiguration.loadConfiguration(new File(_CONFIG("bounty.yml")));

        List<String> bounties = CONFIG.getStringList("bounties");
        buffer.bounties = new ArrayList<>(bounties.stream().map(s -> {
            String[] split = s.split(" ");
            return new BountyObject(split[0], split[1], Double.parseDouble(split[2]));
        }).toList());

        _LOG("bounty.yml", "Loaded!");
    }

    public static void save() {
        _LOG("bounty.yml", "Saving...");

        List<String> bounties = buffer.bounties.stream().map(obj -> obj.getTarget() + " " + obj.getPlayer() + " " + obj.getAmount()).toList();
        CONFIG.set("bounties", bounties);

        try {
            CONFIG.save(new File(_CONFIG("bounty.yml")));
        } catch (Exception ex) { ex.printStackTrace(); }
        _LOG("bounty.yml", "Saved!");
    }

    public static void saveDefault() {
        _LOG("bounty.yml", "Saving default...");

        CanelonesCore.INSTANCE.getDataFolder().mkdirs();

        PrintWriter writer;
        try {
            if(!new File(_CONFIG("bounty.yml")).exists())
                new File(_CONFIG("bounty.yml")).createNewFile();

            writer = new PrintWriter(_CONFIG("bounty.yml"), StandardCharsets.UTF_8);
            InputStream is = ConfigMainBuffer.class.getClassLoader().getResourceAsStream("bounty.yml");
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

        _LOG("bounty.yml", "Default saved!");
    }
}