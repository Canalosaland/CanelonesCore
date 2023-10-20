package me.pk2.canalosaland.command;

import me.pk2.canalosaland.interfaces.GInterface;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import me.pk2.canalosaland.util.ClassUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandInterface implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player) && sender.hasPermission("canalosaland.interface")) {
            if(args.length < 2) {
                sender.sendMessage(_COLOR("&cUsage: /interface <player> <interface> [context]"));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                sender.sendMessage(_COLOR("&cPlayer not found!"));
                return true;
            }

            User user = UserManager.get(target);
            if(user == null) {
                sender.sendMessage(_COLOR("&cUser not found!"));
                return true;
            }

            Class<? extends GInterface> clazz;
            try {
                Class<?> c = Class.forName(args[1]); // Yes, they have to type full classpath
                if(ClassUtil.classExtends(c, GInterface.class))
                    clazz = c.asSubclass(GInterface.class);
                else {
                    sender.sendMessage(_COLOR("&cClass is not an interface!"));
                    return true;
                }
            } catch (Exception exception) {
                sender.sendMessage(_COLOR("&cInterface not found!"));
                return true;
            }

            if(args.length > 3) {
                StringBuilder builder = new StringBuilder();
                for(int i = 2; i < args.length; i++)
                    builder.append(args[i] + " ");
                user.setMenuContext(builder.toString().strip());
            }

            user.openI(clazz);

            sender.sendMessage(_COLOR("&aInterface opened for " + target.getName() + "!"));
            return true;
        }

        if(args.length < 1) {
            sender.sendMessage(_COLOR("&cUsage: /interface <interface> [context]"));
            return true;
        }

        Player player = (Player) sender;
        User user = UserManager.get(player);
        if(user == null) {
            sender.sendMessage(_COLOR("&cUser not found! Contact pk2_stimpy if this error persists."));
            return true;
        }

        Class<? extends GInterface> clazz;
        try {
            Class<?> c = Class.forName(args[0]);
            if(ClassUtil.classExtends(c, GInterface.class))
                clazz = c.asSubclass(GInterface.class);
            else {
                sender.sendMessage(_COLOR("&cClass is not an interface!"));
                return true;
            }
        } catch (Exception exception) {
            sender.sendMessage(_COLOR("&cInterface not found!"));
            return true;
        }

        if(args.length > 2) {
            StringBuilder builder = new StringBuilder();
            for(int i = 1; i < args.length; i++)
                builder.append(args[i] + " ");
            user.setMenuContext(builder.toString().strip());
        }

        user.openI(clazz);
        return true;
    }
}