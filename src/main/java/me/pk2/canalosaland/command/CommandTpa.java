package me.pk2.canalosaland.command;

import me.pk2.canalosaland.listeners.TPAListener;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandTpa implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_ONLY_PLAYER"));
            return true;
        }

        User user = UserManager.get(player);
        if(user == null) {
            player.sendMessage(_SENDER_TRANSLATE(player, "COMMAND_USER_NOT_FOUND"));
            return true;
        }

        if(args.length < 1) {
            player.sendMessage(user.translateC("COMMAND_USAGE") + "/tpa <player>");
            return true;
        }

        if(args[0].contentEquals(player.getName())) {
            player.sendMessage(user.translateC("COMMAND_TPA_PLAYER_SAME"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            player.sendMessage(user.translateC("COMMAND_TPA_PLAYER_NOTFOUND"));
            return true;
        }

        User targetUser = UserManager.get(target);
        if(targetUser == null) {
            player.sendMessage(user.translateC("COMMAND_TPA_PLAYER_NOTFOUND"));
            return true;
        }

        if(targetUser.getLastTpa().equalsIgnoreCase(_UUID(player))) {
            player.sendMessage(user.translateC("COMMAND_TPA_PLAYER_ALREADY"));
            return true;
        }

        if(TPAListener.users.containsKey(_UUID(player))) {
            player.sendMessage(user.translateC("COMMAND_TPA_PLAYER_TELEPORTING_ALREADY"));
            return true;
        }

        // PAListener.users.put(_UUID(player), System.currentTimeMillis()); /// WHILE TELEPORTING STATE
        targetUser.setLastTpa(_UUID(player));

        player.sendMessage(user.translateC("COMMAND_TPA_SENT").replace("%user%", target.getName()));
        target.sendMessage(targetUser.translateC("COMMAND_TPA_RECEIVED").replace("%user%", player.getName()));

        _SOUND_CLICK(player);
        _SOUND_CLICK(target);
        return true;
    }
}