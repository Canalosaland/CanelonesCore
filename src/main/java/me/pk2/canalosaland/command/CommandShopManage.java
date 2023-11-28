package me.pk2.canalosaland.command;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.buffer.DBBufferShops;
import me.pk2.canalosaland.db.buffer.shop.DBBSShop;
import me.pk2.canalosaland.db.buffer.shop.DBBSShopData;
import me.pk2.canalosaland.db.buffer.shop.items.DBBSShopItem;
import me.pk2.canalosaland.db.buffer.shop.items.DBBSShopItemCommand;
import me.pk2.canalosaland.db.buffer.shop.items.DBBSShopItemStack;
import me.pk2.canalosaland.db.obj.shops.DBShopData;
import me.pk2.canalosaland.db.obj.shops.items.DBSItem;
import me.pk2.canalosaland.db.obj.shops.items.DBSItemCommand;
import me.pk2.canalosaland.db.obj.shops.items.DBSItemStack;
import me.pk2.canalosaland.util.BukkitSerialization;
import org.apache.commons.lang.SerializationUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.ArrayList;

public class CommandShopManage implements CommandExecutor {
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(_COLOR("""
                &7&l&m_________&a &lShop &aManagement &7&l&m_________
                &8&l> &e/shopmanage list
                &8&l> &e/shopmanage new <name>
                &8&l> &e/shopmanage item <shop>
                &8&l> &e/shopmanage item <shop> clear
                &8&l> &e/shopmanage item <shop> delete <item>
                &8&l> &e/shopmanage item <shop> add stack <price>
                &8&l> &e/shopmanage item <shop> add cmd <price> <command>
                &8&l> &e/shopmanage item <shop> update price <item> <price>
                &8&l> &e/shopmanage item <shop> update content <item> [command]
                &8&l> &e/shopmanage shop <shop>
                &8&l> &e/shopmanage shop <shop> delete
                &8&l> &e/shopmanage shop <shop> rename <name>
                &8&l> &e/shopmanage shop <shop> update <local/dbs>
                &7&l___________________________________
                """));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("canalosaland.command.shopmanage")) {
            sender.sendMessage(_COLOR("&cNo tienes permisos para ejecutar este comando."));
            return true;
        }

        if(args.length < 1) {
            sendHelp(sender);
            return true;
        }

        switch(args[0].toLowerCase()) {
            case "list": {
                sender.sendMessage(_COLOR("&a&lShops:"));
                for(DBBSShop shop : DBBufferShops.BUFFER.getShops())
                    sender.sendMessage(_COLOR(" &8- id: &f" + shop.getDbShop().getId() + " &8name: &f" + shop.getDbShop().getName()));
                sender.sendMessage(_COLOR(" &r"));
            } break;

            case "new": {
                if(args.length < 2) {
                    sendHelp(sender);
                    break;
                }

                String name = args[1];
                DBBSShop exists = DBBufferShops.BUFFER.getShop(name);
                if(exists != null)
                    break;

                sender.sendMessage(_COLOR("&eConnecting..."));

                DBShopData tempData = new DBShopData(name, new ArrayList<>());
                DBApi.enqueue(() -> {
                    Connection conn = DBApi.connect();

                    int exCode = DBApi.API.shops.insertShop(conn, name, tempData);

                    DBApi.disconnect(conn);

                    if(exCode != 1) {
                        sender.sendMessage(_COLOR("&cError creating shop"));
                        return;
                    }

                    DBBufferShops.BUFFER.updateShops();
                    sender.sendMessage(_COLOR("&aShop created!"));
                });
            } break;

            case "item": {
                if(args.length < 2) {
                    sendHelp(sender);
                    break;
                }

                DBBSShop shop = DBBufferShops.BUFFER.getShop(args[1]);
                if(shop == null) {
                    sender.sendMessage(_COLOR("&cShop not found."));
                    break;
                }

                DBBSShopData shopData = shop.getShopData();
                ArrayList<DBBSShopItem> items = shop.getShopData().getItems();
                if(shopData.updateNeeded())
                    sender.sendMessage(_COLOR("&6&lWARNING! &6You have unsaved changes, push them with &e/shopmanage shop " + shop.getDbShop().getName() + " update dbs"));
                if(args.length < 3) {
                    sender.sendMessage(_COLOR("&a&l" + args[1]));
                    for (int i = 0; i < items.size(); i++) {
                        DBBSShopItem item = items.get(i);
                        if (item instanceof DBBSShopItemCommand) {
                            DBBSShopItemCommand itemCommand = (DBBSShopItemCommand) item;
                            sender.sendMessage(_COLOR(" &8- id: &f" + i + " &8type: &fCMD &8mat: &f" + itemCommand.getMaterial().getType().name() + " &8price: &f" + String.format("%.2f$", itemCommand.getPrice()) + " &8exec: '&f/" + itemCommand.getCommand() + "&8'"));
                            continue;
                        }

                        DBBSShopItemStack itemStack = (DBBSShopItemStack) item;
                        sender.sendMessage(_COLOR(" &8- id: &f" + i + " &8type: &fCMD &8mat: &f" + itemStack.getMaterial().getType().name() + " &8price: &f" + String.format("%.2f$", itemStack.getPrice()) + " &8stack: '&f/" + itemStack.getItemStack().getType().name() + "&8'"));
                    }

                    break;
                }

                String subCmd = args[2].toLowerCase();
                if(subCmd.equals("clear")) {
                    shopData.clearItems();
                    sender.sendMessage(_COLOR("&aCleared items."));
                    break;
                }

                if(subCmd.equals("delete") && args.length > 3) {
                    int id = Integer.parseInt(args[3]);
                    if(id < 0 || id >= items.size()) {
                        sender.sendMessage(_COLOR("&cInvalid item id."));
                        break;
                    }

                    shopData.removeItem(id);
                    sender.sendMessage(_COLOR("&aRemoved item " + id + "."));
                    break;
                }

                if(subCmd.equals("add") && args.length > 4 && sender instanceof Player) {
                    subCmd = args[3].toLowerCase();

                    Player player = (Player)sender;
                    int price = Integer.parseInt(args[4]);
                    if(subCmd.equals("stack")) {
                        ItemStack iStack = player.getInventory().getItemInMainHand();
                        byte[] bytes = BukkitSerialization.serializeItems(iStack);

                        DBBSShopItemStack item = new DBBSShopItemStack(new DBSItemStack(price, bytes, bytes));
                        shopData.insertItem(item);
                        sender.sendMessage(_COLOR("&aAdded item."));
                        break;
                    }

                    if(subCmd.equals("cmd") && args.length > 5) {
                        ItemStack iStack = player.getInventory().getItemInMainHand();
                        byte[] bytes = BukkitSerialization.serializeItems(iStack);

                        StringBuilder builder = new StringBuilder();
                        for(int i = 0; i < 5; i++)
                            builder.append(args[i]).append(" ");
                        builder.deleteCharAt(builder.length()-1);

                        DBBSShopItemCommand item = new DBBSShopItemCommand(new DBSItemCommand(price, bytes, builder.toString()));
                        shopData.insertItem(item);
                        sender.sendMessage(_COLOR("&aAdded item."));
                        break;
                    }

                    sendHelp(sender);
                }
            } break;

            default:
                sendHelp(sender);
                break;
        }
        return true;
    }
}