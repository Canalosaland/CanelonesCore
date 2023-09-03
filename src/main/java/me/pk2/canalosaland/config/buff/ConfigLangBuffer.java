package me.pk2.canalosaland.config.buff;

import me.pk2.canalosaland.config.buff.lang.ConfigLangLocaleBuffer;
import me.pk2.canalosaland.user.User;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static me.pk2.canalosaland.util.Wrapper.*;

public class ConfigLangBuffer {
    private static HashMap<String, ConfigLangLocaleBuffer> locales = new HashMap<>();
    public static void load() {
        _LOG("Lang", "Loading...");
        locales.clear();
        locales.put("es", new ConfigLangLocaleBuffer("es"));
        locales.put("en", new ConfigLangLocaleBuffer("en"));

        for(ConfigLangLocaleBuffer locale : locales.values())
            locale.load();
    }

    public static ConfigLangLocaleBuffer getLocale(String locale) { return locales.get(locale)==null ? locales.values().stream().findFirst().get() : locales.get(locale); }
    public static String translate(String locale, String key) { return getLocale(locale).translate(key); }
    public static String translate(String locale, Player player, String key) { return getLocale(locale).translate(player, key); }
    public static String translate(User user, String key) { return translate(user.locale, user.player, key); }
    public static String translateC(String locale, String key) { return getLocale(locale).translateC(key); }
    public static String translateC(String locale, Player player, String key) { return getLocale(locale).translateC(player, key); }
    public static String translateC(User user, String key) { return translateC(user.locale, user.player, key); }
}