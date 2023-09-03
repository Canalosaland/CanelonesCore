package me.pk2.canalosaland.command;

import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import me.pk2.canalosaland.db.obj.DBHomeObj;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandHome implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_ONLY_PLAYER"));
            return true;
        }

        User user = UserManager.get(player);
        if(user == null) {
            player.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_HOME_USER_NOT_FOUND"));
            return true;
        }

        if(user.getHomes().length == 0) {
            player.sendMessage(user.translateC("COMMAND_HOME_NO_HOMES"));
            return true;
        }

        if(args.length == 0) {
            DBHomeObj[] homes = user.getHomes();

            StringBuilder builder = new StringBuilder();
            for(DBHomeObj home : homes)
                builder.append("&7").append(home.name).append("&8, ");

            String homesString = builder.substring(0, builder.length() - 2);
            player.sendMessage(user.translateC("COMMAND_HOME_LIST") + _COLOR(homesString));
            return true;
        }

        String name = args[0];
        DBHomeObj home = null;
        for(DBHomeObj h : user.getHomes()) {
            if(h.name.contentEquals(name)) {
                home = h;
                break;
            }
        }

        if(home == null) {
            player.sendMessage(user.translateC("COMMAND_HOME_NOT_FOUND"));
            return true;
        }

        player.teleport(home.location);
        player.sendMessage(user.translateC("COMMAND_HOME_TELEPORTED").replace("%name%", home.name));
        _SOUND_TELEPORT(player);
        return true;
    }
}