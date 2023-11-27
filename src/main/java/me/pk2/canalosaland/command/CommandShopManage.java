package me.pk2.canalosaland.command;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.buffer.DBBufferShops;
import me.pk2.canalosaland.db.buffer.shop.DBBSShop;
import me.pk2.canalosaland.db.obj.shops.DBShopData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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

            default:
                sendHelp(sender);
                break;
        }
        return true;
    }
}