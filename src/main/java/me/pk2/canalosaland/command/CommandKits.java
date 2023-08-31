package me.pk2.canalosaland.command;

import me.pk2.canalosaland.interfaces.GInterfaceKits;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandKits implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(_COLOR("&cEste comando solo puede ser ejecutado por un jugador."));
            return true;
        }

        User user = UserManager.get(player);
        if(user == null) {
            player.sendMessage(_COLOR("&cNo se ha podido encontrar tu usuario."));
            return true;
        }

        user.openI(GInterfaceKits.class);
        return true;
    }
}