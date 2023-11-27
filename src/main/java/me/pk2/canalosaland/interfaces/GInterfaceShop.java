package me.pk2.canalosaland.interfaces;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.db.buffer.DBBufferShops;
import me.pk2.canalosaland.db.buffer.shop.DBBSShop;
import me.pk2.canalosaland.db.buffer.shop.DBBSShopData;
import me.pk2.canalosaland.db.buffer.shop.items.DBBSShopItem;
import me.pk2.canalosaland.user.User;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GInterfaceShop extends GInterface {
    private DBBSShop bufferShop;
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

        bufferShop = shop;

        super.open();
        _SOUND_PAGE(owner.player);

        DBBSShopData shopData = shop.getShopData();
        for(int i = 0; i < shopData.getItems().size(); i++) {
            DBBSShopItem item = shopData.getItems().get(i);

            ItemStack stack = item.getMaterial().clone();
            ItemMeta meta = stack.getItemMeta();
            meta.setLore(Arrays.asList("", _COLOR(String.format("&e%.2f$", item.getPrice())), owner.translateC("INTERFACE_SHOP_PURCHASE")));
            stack.setItemMeta(meta);

            setItem(i, stack);
        }
    }
}