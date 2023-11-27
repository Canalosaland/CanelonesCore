package me.pk2.canalosaland.interfaces;

import me.pk2.canalosaland.db.buffer.DBBufferShops;
import me.pk2.canalosaland.db.buffer.shop.DBBSShop;
import me.pk2.canalosaland.user.User;

public class GInterfaceShop extends GInterface {
    public GInterfaceShop(User user) {
        super(user, "&9Shop", 6);
    }

    @Override
    public void init() {}

    @Override
    public void click(int slot) {

    }

    @Override
    public void open() {
        super.open();

        String menuContext = owner.getMenuContext();
        if(menuContext == null) {
            owner.sendLocale("INTERFACE_SHOP_ERROR_CONTEXT");
            return;
        }

        DBBSShop shop = DBBufferShops.BUFFER.getShop(menuContext);
        if(shop == null) {
            owner.sendLocale("INTERFACE_SHOP_ERROR_INSTANCE");
            return;
        }
    }
}