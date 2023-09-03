package me.pk2.canalosaland.interfaces;

import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.user.User;

import java.sql.Connection;

import static me.pk2.canalosaland.util.Wrapper.*;

public class GInterfaceLanguage extends GInterface{
    public GInterfaceLanguage(User user) {
        super(user, "&a&lSelect a language", 1);
    }

    @Override
    public void init() {
        setHead(0, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJiZDQ1MjE5ODMzMDllMGFkNzZjMWVlMjk4NzQyODc5NTdlYzNkOTZmOGQ4ODkzMjRkYThjODg3ZTQ4NWVhOCJ9fX0=", owner.translateC("LANG_SPANISH"));
        setHead(1, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNhYzk3NzRkYTEyMTcyNDg1MzJjZTE0N2Y3ODMxZjY3YTEyZmRjY2ExY2YwY2I0YjM4NDhkZTZiYzk0YjQifX19", owner.translateC("LANG_ENGLISH"));
    }

    @Override
    public void open() {
        super.open();
        _SOUND_OPEN(owner.player);
    }

    @Override
    public void click(int slot) {
        switch(slot) {
            case 0:
                DBApi.enqueue(() -> {
                    Connection conn = DBApi.connect();

                    DBApi.API.users.changeLocale(conn, owner.getUserId(), "es");
                    owner.fetchData();
                    owner.player.sendMessage(_COLOR("&a&lIdioma cambiado a español! Es posible que tengas que reconectarte para que algunos cambios tengan efecto!"));
                    _SOUND_NOTIFICATION(owner.player);

                    DBApi.disconnect(conn);
                });
                break;
            case 1:
                DBApi.enqueue(() -> {
                    Connection conn = DBApi.connect();

                    DBApi.API.users.changeLocale(conn, owner.getUserId(), "en");
                    owner.fetchData();
                    owner.player.sendMessage(_COLOR("&a&lLanguage changed to english! You may need to rejoin for some changes to take effect!"));
                    _SOUND_NOTIFICATION(owner.player);

                    DBApi.disconnect(conn);
                });
                break;
        }

        _SOUND_CLOSE(owner.player);
        owner.player.closeInventory();
    }
}