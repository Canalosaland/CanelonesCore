package me.pk2.canalosaland.dependencies.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.pk2.canalosaland.config.buff.ConfigLangBuffer;
import me.pk2.canalosaland.interfaces.GInterfaceLanguage;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.pk2.canalosaland.util.Wrapper.*;

public class PAPIExpansion extends PlaceholderExpansion {
    public @Override @NotNull String getIdentifier() { return "Canelones"; }
    public @Override @NotNull String getAuthor() { return "PK2_Stimpy"; }
    public @Override @NotNull String getVersion() { return "1.0.0"; }
    public @Override boolean persist() { return true; }
    public @Override String onRequest(OfflinePlayer player, @NotNull String arg) {
        switch (arg.toLowerCase()) { // switch just in case I need to add more placeholders (and bc it looks cool tbh)
            case "language_skin": {
                User user = UserManager.get((Player)player);
                if(user == null)
                    return GInterfaceLanguage.HEAD_SPANISH;
                return user.locale.equalsIgnoreCase("es")?GInterfaceLanguage.HEAD_SPANISH:GInterfaceLanguage.HEAD_ENGLISH;
            }
            case "mb_available": {
                User user = UserManager.get((Player)player);
                if(user == null)
                    return ConfigLangBuffer.getLocale("es").translateC("HOLOGRAM_MB_NOT_AVAILABLE");
                if(user.getBoxes().length > 0)
                    return user.translateC("HOLOGRAM_MB_AVAILABLE");
                return user.translateC("HOLOGRAM_MB_NOT_AVAILABLE");
            }
            default:
                break;
        }

        if(arg.toLowerCase().startsWith("translate_")) {
            User user = UserManager.get((Player)player);
            if(user == null)
                return null;

            String key = arg.substring(10);
            String translate = user.translateC(key);
            if(translate.isBlank())
                return null;
            return translate;
        }

        return null;
    }
}