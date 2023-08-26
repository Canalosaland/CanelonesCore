package me.pk2.canalosaland.command;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandTCMsg implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch(label.toLowerCase()) {
            case "msg":
            case "tell":
            case "pm": {
                if(args.length < 2) {
                    sender.sendMessage(_COLOR("&cUsage: /" + label + " <player> <message>"));
                    break;
                }

                String senderName = sender instanceof Player ? sender.getName() : "Console";
                Player target = Bukkit.getPlayer(args[0]);
                if(target == null) {
                    sender.sendMessage(_COLOR("&cPlayer not found."));
                    break;
                }

                StringBuilder builder = new StringBuilder();
                for(int i = 1; i < args.length; i++)
                    builder.append(args[i]).append(" ");
                String message = builder.toString().substring(0, builder.length() - 1);

                User user = UserManager.get(target);
                if(user != null)
                    user.lastMessageFrom = senderName;

                if(!ConfigMainBuffer.buffer.pm_tcom.enabled) {
                    sender.sendMessage(_COLOR("&aTo " + target.getName() + ": &7" + message));
                    target.sendMessage(_COLOR("&aFrom " + senderName + ": &7" + message));

                    break;
                }

                Bukkit.dispatchCommand(sender, "phone send " + target.getName() + " " + message);
            } break;
            case "r": {
                if(!(sender instanceof Player)) {
                    sender.sendMessage(_COLOR("&cOnly players can use this command."));
                    break;
                }

                if(args.length < 1) {
                    sender.sendMessage(_COLOR("&cUsage: /" + label + " <message>"));
                    break;
                }

                User user = UserManager.get((Player) sender);
                if(user == null) {
                    sender.sendMessage(_COLOR("&cYou are not a user ???!. Contact PK2_Stimpy to fix this."));
                    break;
                }

                if(user.lastMessageFrom == null) {
                    sender.sendMessage(_COLOR("&cYou have no one to reply to."));
                    break;
                }

                Player target = Bukkit.getPlayer(user.lastMessageFrom);
                if(target == null) {
                    sender.sendMessage(_COLOR("&cPlayer not found."));
                    break;
                }

                StringBuilder builder = new StringBuilder();
                for(int i = 0; i < args.length; i++)
                    builder.append(args[i]).append(" ");
                String message = builder.toString().substring(0, builder.length() - 1);

                User tUser = UserManager.get(target);
                if(tUser != null)
                    tUser.lastMessageFrom = sender.getName();

                if(!ConfigMainBuffer.buffer.pm_tcom.enabled) {
                    sender.sendMessage(_COLOR("&aTo " + user.lastMessageFrom + ": &7" + message));
                    target.sendMessage(_COLOR("&aFrom " + sender.getName() + ": &7" + message));
                    break;
                }

                Bukkit.dispatchCommand(sender, "phone send " + user.lastMessageFrom + " " + message);
            } break;
            default:
                break;
        }

        return true;
    }
}