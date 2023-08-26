package me.pk2.canalosaland.dependencies;

import com.dbteku.telecom.api.TelecomApi;
import com.dbteku.telecom.models.Carrier;
import me.pk2.canalosaland.CanelonesCore;
import org.bukkit.entity.Player;

public class DependencyTCom {
    public static TelecomApi API = null;
    public static void register() {
        API = TelecomApi.get();

        CanelonesCore.INSTANCE.getLogger().info("Telecom registered!");
    }

    public static Carrier getCarrierByPlayer(Player player) {
        for(Carrier carrier : API.getAllCarriers())
            if(carrier.getSubscribers().contains(player.getName()))
                return carrier;
        return null;
    }
}