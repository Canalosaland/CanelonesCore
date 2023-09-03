package me.pk2.canalosaland.command;

import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandDelHome implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_ONLY_PLAYER"));
            return true;
        }

        if(args.length == 0) {
            player.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_USAGE") + "/delhome <nombre>");
            return true;
        }

        User user = UserManager.get(player);
        if(user == null) {
            player.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_USER_NOT_FOUND"));
            return true;
        }

        String name = args[0];
        if(name.length() > 24) {
            player.sendMessage(user.translateC("COMMAND_HOME_NAME_TOO_LONG"));
            return true;
        }

        player.sendMessage(user.translateC("COMMAND_HOME_CONNECTING"));
        DBApi.enqueue(() -> {
            Connection conn = DBApi.connect();
            if(DBApi.API.homes.existsHome(conn, user.getUserId(), name) != 1) {
                player.sendMessage(user.translateC("COMMAND_HOME_NOT_FOUND"));
                DBApi.disconnect(conn);
                return;
            }

            int exCode = DBApi.API.homes.removeHome(conn, user.getUserId(), name);
            if(exCode != 1) {
                player.sendMessage(user.translateC("COMMAND_HOME_ERROR") + " [" + exCode + "]");
                DBApi.disconnect(conn);
                return;
            }

            DBApi.disconnect(conn);
            user.fetchData();
            player.sendMessage(user.translateC("COMMAND_HOME_DELETE_SUCCESS"));
        });
        return true;
    }
}