package me.pk2.canalosaland.command;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.obj.DBBanObj;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

public class CommandUnban implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("canalosaland.command.unban")) {
            sender.sendMessage(_COLOR("&cNo tienes permiso para ejecutar este comando."));
            return true;
        }

        if(args.length < 1) {
            sender.sendMessage(_COLOR("&c/unban <banId>"));
            return true;
        }

        sender.sendMessage(_COLOR("&eUnbanning..."));
        DBApi.enqueue(() -> {
            Connection conn = DBApi.connect();

            DBBanObj ban = DBApi.API.bans.getBan(conn, Integer.parseInt(args[0]));
            if(ban == null) {
                sender.sendMessage(_COLOR("&cCould not find ban."));

                DBApi.disconnect(conn);
                return;
            }

            int exCode = DBApi.API.bans.pardonBan(conn, ban.getId());
            if(exCode != 1) {
                sender.sendMessage(_COLOR("&cCould not remove ban. " + exCode));

                DBApi.disconnect(conn);
                return;
            }

            DBApi.disconnect(conn);

            sender.sendMessage(_COLOR("&aUnbanned id " + args[0] + "."));
        });
        return true;
    }
}