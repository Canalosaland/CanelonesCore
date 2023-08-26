package me.pk2.canalosaland.dependencies;

import me.pk2.canalosaland.CanelonesCore;
import net.luckperms.api.LuckPerms;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class DependencyLP {
    public static LuckPerms API = null;
    public static void register(RegisteredServiceProvider<LuckPerms> provider) {
        if(provider != null) {
            API = provider.getProvider();

            CanelonesCore.INSTANCE.getLogger().info("LuckPerms registered!");
        } else CanelonesCore.INSTANCE.getLogger().warning("LuckPerms is not installed, LuckPerms prefixes will not work!");
    }

    public static String getPrefix(Player player) {
        if(API == null)
            return "";

        String prefix = API.getPlayerAdapter(Player.class).getMetaData(player).getPrefix();
        return prefix==null?"":prefix;
    }

    public static String getSuffix(Player player) {
        if(API == null)
            return "";

        String suffix = API.getPlayerAdapter(Player.class).getMetaData(player).getSuffix();
        return suffix==null?"":suffix;
    }
}