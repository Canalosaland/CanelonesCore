package me.pk2.canalosaland.command;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.interfaces.GInterfacePhone;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandPhone implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(_COLOR("&cThis command can only be executed by players!"));
            return true;
        }

        User user = UserManager.get(player);
        if(user == null) {
            player.sendMessage(_COLOR("&cAn error occurred while loading your user data!"));
            return true;
        }

        user.openI(GInterfacePhone.class);
        return true;
    }
}