package me.pk2.canalosaland.command;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.obj.DBBanObj;
import me.pk2.canalosaland.dependencies.DependencyAuthMe;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

public class CommandBan implements CommandExecutor {
    private static void sendHelp(CommandSender sender) {
        sender.sendMessage(_COLOR("&c/ban <ip/user> <player> <sec> <reason>"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("canalosaland.command.ban")) {
            sender.sendMessage("&cNo tienes permiso para ejecutar este comando.");
            return true;
        }

        if(args.length < 4) {
            sendHelp(sender);
            return true;
        }

        short type = args[0].equalsIgnoreCase("ip")?(short)1:(args[0].equalsIgnoreCase("user")?(short)0:(short)-1);
        if(type == -1) {
            sendHelp(sender);
            return true;
        }

        String pName = args[1].toLowerCase();
        Player player = Bukkit.getPlayerExact(pName);
        User user;
        if(player != null)
            user = UserManager.get(player);
        else user = null;

        long sec = Long.parseLong(args[2]);

        StringBuilder reasonBuilder = new StringBuilder();
        for(int i = 3; i < args.length; i++)
            reasonBuilder.append(args[i]).append(" ");
        reasonBuilder.deleteCharAt(reasonBuilder.length()-1);

        String reason = _COLOR(reasonBuilder.toString());
        if(type == 1) {
            pName = DependencyAuthMe.api.getLastIp(pName);
            if(pName == null) {
                sender.sendMessage(_COLOR("&cCould not ban user, IP not found."));
                return true;
            }
        }

        final String fName = pName;

        sender.sendMessage(_COLOR("&eBanning user..."));
        DBApi.enqueue(() -> {
            Connection conn = DBApi.connect();

            int exCode = DBApi.API.bans.addBan(conn, type, fName, sec, reason);
            if(exCode != 1) {
                sender.sendMessage(_COLOR("&cCould not add ban. " + exCode));
                DBApi.disconnect(conn);
                return;
            }

            if(user != null) {
                DBBanObj[] bans = DBApi.API.bans.getBans(conn, type, fName);
                DBBanObj ban = bans[bans.length-1];
                user.sendBanKick(ban);
            }

            DBApi.disconnect(conn);

            sender.sendMessage(_COLOR("&aSuccessfully banned user(" + fName + ")."));
        });


        return true;
    }
}