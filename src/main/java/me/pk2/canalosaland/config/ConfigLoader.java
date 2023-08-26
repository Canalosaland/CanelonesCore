package me.pk2.canalosaland.config;

import static me.pk2.canalosaland.CanelonesCore.INSTANCE;

import me.pk2.canalosaland.config.buff.ConfigAtmBuffer;
import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import me.pk2.canalosaland.webapi.WebAPI;

public class ConfigLoader {
    public static void load() {
        ConfigMainBuffer.load();
        ConfigAtmBuffer.load();

        if(INSTANCE.webAPI != null)
            if (ConfigMainBuffer.buffer.webapi.enabled)
                INSTANCE.webAPI.start();
            else INSTANCE.webAPI.stop();
        if(INSTANCE.webAPI == null && ConfigMainBuffer.buffer.webapi.enabled) {
            INSTANCE.webAPI = new WebAPI();
            INSTANCE.webAPI.start();
        }
    }

    public static void save() {
        ConfigMainBuffer.save();
        ConfigAtmBuffer.save();
    }
}