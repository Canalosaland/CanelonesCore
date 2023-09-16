package me.pk2.canalosaland.command;

import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandTpaDeny implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_ONLY_PLAYER"));
            return true;
        }

        User user = UserManager.get(player);
        if(user == null) {
            player.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_USER_NOT_FOUND"));
            return true;
        }

        if(user.getLastTpa().isBlank()) {
            player.sendMessage(user.translateC("COMMAND_TPA_ACCEPT_NOT_FOUND"));
            return true;
        }

        Player fromPlayer = Bukkit.getPlayer(UUID.fromString(user.getLastTpa()));
        if(fromPlayer == null) {
            player.sendMessage(user.translateC("COMMAND_TPA_ACCEPT_NOT_FOUND"));
            return true;
        }

        user.setLastTpa("");

        player.sendMessage(user.translateC("COMMAND_TPA_CANCEL"));
        fromPlayer.sendMessage(_SENDER_TRANSLATE(fromPlayer, "COMMAND_TPA_CANCEL_CANCEL"));
        return true;
    }
}