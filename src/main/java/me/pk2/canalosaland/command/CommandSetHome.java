package me.pk2.canalosaland.command;

import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
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

public class CommandSetHome implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_ONLY_PLAYER"));
            return true;
        }

        if(args.length == 0) {
            player.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_USAGE" + "/" + label + " <name>"));
            return true;
        }

        User user = UserManager.get(player);
        if(user == null) {
            player.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_HOME_USER_NOT_FOUND"));
            return true;
        }

        if(user.getKits().length >= ConfigMainBuffer.buffer.homes.max_homes) {
            player.sendMessage(user.translateC("COMMAND_HOME_MAX_HOMES"));
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
            if(DBApi.API.homes.existsHome(conn, user.getUserId(), name) == 1) {
                player.sendMessage(user.translateC("COMMAND_HOME_ALREADY_EXISTS"));
                DBApi.disconnect(conn);
                return;
            }

            int exCode = DBApi.API.homes.addHome(conn, user.getUserId(), name, player.getLocation());
            if(exCode != 1) {
                player.sendMessage(user.translateC("COMMAND_HOME_ADD_ERROR") + " [" + exCode + "]");
                DBApi.disconnect(conn);
                return;
            }

            user.fetchData();
            player.sendMessage(user.translateC("COMMAND_HOME_ADDED").replace("%name%", name));
        });
        return true;
    }
}