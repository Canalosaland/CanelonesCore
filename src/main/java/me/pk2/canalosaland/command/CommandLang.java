package me.pk2.canalosaland.command;

import me.pk2.canalosaland.interfaces.GInterfaceLanguage;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandLang implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) { // TODO: Change /lang to other thing
        if(!(sender instanceof Player))
            return true;

        User user = UserManager.get((Player) sender);
        if(user == null) {
            sender.sendMessage("&c&lError: &cNo se pudo encontrar tu usuario, reconectate!");
            return true;
        }

        user.openI(GInterfaceLanguage.class);
        return true;
    }
}