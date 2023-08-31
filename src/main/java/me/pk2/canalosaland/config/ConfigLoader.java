package me.pk2.canalosaland.config;

import static me.pk2.canalosaland.CanelonesCore.INSTANCE;
import static me.pk2.canalosaland.util.Wrapper._LOG;

import me.pk2.canalosaland.config.buff.ConfigAtmBuffer;
import me.pk2.canalosaland.config.buff.ConfigBountyBuffer;
import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.buffer.DBBufferKits;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import me.pk2.canalosaland.webapi.WebAPI;

public class ConfigLoader {
    public static void load() {
        ConfigMainBuffer.load();
        ConfigAtmBuffer.load();
        ConfigBountyBuffer.load();

        if(INSTANCE.webAPI != null)
            if (ConfigMainBuffer.buffer.webapi.enabled)
                INSTANCE.webAPI.start();
            else INSTANCE.webAPI.stop();
        if(INSTANCE.webAPI == null && ConfigMainBuffer.buffer.webapi.enabled) {
            INSTANCE.webAPI = new WebAPI();
            INSTANCE.webAPI.start();
        }

        _LOG("Database", "Updating kits...");
        DBBufferKits.BUFFER.updateKits();
        _LOG("Database", "Updating users...");
        for(User user : UserManager.users.values())
            user.fetchData();
    }

    public static void save() {
        ConfigMainBuffer.save();
        ConfigAtmBuffer.save();
        ConfigBountyBuffer.save();
    }
}