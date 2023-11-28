package me.pk2.canalosaland.interfaces;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.db.buffer.DBBufferShops;
import me.pk2.canalosaland.db.buffer.shop.DBBSShop;
import me.pk2.canalosaland.db.buffer.shop.DBBSShopData;
import me.pk2.canalosaland.db.buffer.shop.items.DBBSShopItem;
import me.pk2.canalosaland.db.buffer.shop.items.DBBSShopItemCommand;
import me.pk2.canalosaland.db.buffer.shop.items.DBBSShopItemStack;
import me.pk2.canalosaland.dependencies.DependencyVault;
import me.pk2.canalosaland.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Map;

public class GInterfaceShop extends GInterface {
    private DBBSShop bufferShop;
    public GInterfaceShop(User user) {
        super(user, "&9Shop", 6);
    }

    @Override
    public void init() {}

    @Override
    public void click(int slot) {
        if(bufferShop == null) {
            owner.sendLocale("INTERFACE_SHOP_ERROR_INSTANCE");
            _SOUND_ERROR(owner.player);
            return;
        }

        DBBSShopData shopData = bufferShop.getShopData();
        if(slot >= shopData.getItems().size())
            return;

        DBBSShopItem shopItem = shopData.getItem(slot);
        double price = shopItem.getPrice();
        if(!DependencyVault.has(owner.player, price)) {
            owner.sendLocale("INTERFACE_SHOP_ERROR_MONEY");
            _SOUND_ERROR(owner.player);
            return;
        }

        DependencyVault.withdraw(owner.player, price);
        if(shopItem instanceof DBBSShopItemCommand) {
            DBBSShopItemCommand itemCmd = (DBBSShopItemCommand)shopItem;

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), _PLACEHOLDER(owner.player, itemCmd.getCommand()));

            owner.player.sendMessage(owner.translateC("INTERFACE_SHOP_PURCHASED")
                    .replace("%name%", _COLOR(itemCmd.getMaterial().getItemMeta().hasDisplayName()?itemCmd.getMaterial().getItemMeta().getDisplayName():itemCmd.getMaterial().getType().name()))
                    .replace("%amount%", "1"));
            _SOUND_CLICK(owner.player);
            return;
        }

        DBBSShopItemStack itemStack = (DBBSShopItemStack)shopItem;
        ItemStack iStack = itemStack.getItemStack();

        Map<Integer, ItemStack> left = owner.player.getInventory().addItem(iStack);
        if(!left.isEmpty())
            for(ItemStack item : left.values())
                owner.player.getWorld().dropItem(owner.player.getLocation(), item);

        owner.player.sendMessage(owner.translateC("INTERFACE_SHOP_PURCHASED")
                .replace("%name%", _COLOR(itemStack.getMaterial().getItemMeta().hasDisplayName()?itemStack.getMaterial().getItemMeta().getDisplayName():itemStack.getMaterial().getType().name()))
                .replace("%amount%", iStack.getAmount()+""));
        _SOUND_CLICK(owner.player);
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
        for(int i = 0; i < inv.getSize(); i++)
            setItem(i, new ItemStack(Material.AIR));
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