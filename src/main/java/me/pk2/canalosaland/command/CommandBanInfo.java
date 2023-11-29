package me.pk2.canalosaland.command;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.obj.DBBanObj;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

public class CommandBanInfo implements CommandExecutor {
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(_COLOR("&c/baninfo <id/user/ip> <player>"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("canalosaland.command.baninfo")) {
            sender.sendMessage(_COLOR("&cNo tienes permiso para ejecutar este comando."));
            return true;
        }

        if(args.length < 2) {
            sendHelp(sender);
            return true;
        }

        String subCmd = args[0].toLowerCase();
        if(subCmd.equals("id")) {
            int id = Integer.parseInt(args[1]);

            sender.sendMessage(_COLOR("&eFetching data..."));
            DBApi.enqueue(() -> {
                Connection conn = DBApi.connect();

                DBBanObj ban = DBApi.API.bans.getBan(conn, id);
                if(ban == null) {
                    sender.sendMessage(_COLOR("&cCould not find ban."));
                    return;
                }

                DBApi.disconnect(conn);

                sender.sendMessage(_COLOR("&8 - id: &f" + ban.getId() + "&8 type: &f" + ban.getType() + "&8 player: &f" + ban.getPlayer() + "&8 time: &f" + ban.getTimeExp() + "&8 reason: &f" + ban.getReason() + "&8 pardon: &f" + (ban.pardon()?"YES":"NO")));
            });
            return true;
        }

        if(subCmd.equals("user") || subCmd.equals("ip")) {
            String user = args[1].toLowerCase();

            sender.sendMessage(_COLOR("&eFetching data..."));
            DBApi.enqueue(() -> {
                Connection conn = DBApi.connect();

                DBBanObj[] bans = DBApi.API.bans.getBans(conn, subCmd.equals("user")?(short)0:(short)1, user);
                if(bans.length < 1) {
                    sender.sendMessage(_COLOR("&cCould not find bans."));
                    return;
                }

                DBApi.disconnect(conn);

                for(DBBanObj ban : bans)
                    sender.sendMessage(_COLOR("&8 - id: &f" + ban.getId() + "&8 type: &f" + ban.getType() + "&8 player: &f" + ban.getPlayer() + "&8 time: &f" + ban.getTimeExp() + "&8 reason: &f" + ban.getReason() + "&8 pardon: &f" + (ban.pardon()?"YES":"NO")));
            });
            return true;
        }

        sendHelp(sender);
        return true;
    }
}