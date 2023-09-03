package me.pk2.canalosaland.command;

import static me.pk2.canalosaland.util.Wrapper.*;
import static me.pk2.canalosaland.config.buff.ConfigLangBuffer.translateC;

import me.pk2.canalosaland.config.buff.ConfigAtmBuffer;
import me.pk2.canalosaland.config.buff.atm.ATMObj;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandATMManage implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(translateC("es", "COMMAND_ONLY_PLAYER"));
            return true;
        }

        User user = UserManager.get(player);
        if(user == null) {
            player.sendMessage(translateC("es", "COMMAND_USER_NOT_FOUND"));
            return true;
        }

        if(!player.hasPermission("canalosaland.atm")) {
            player.sendMessage(user.translateC("COMMAND_NO_PERMISSION"));
            return true;
        }

        if(args.length < 1) {
            player.sendMessage(user.translateC("COMMAND_USAGE") + "/atm-manage <list|create|delete|change>");
            return true;
        }

        switch(args[0].toLowerCase()) {
            case "list" -> {
                player.sendMessage(user.translateC("COMMAND_ATM_LIST"));
                for(int i = 0; i < ConfigAtmBuffer.buffer.atms.size(); i++) {
                    ATMObj atm = ConfigAtmBuffer.buffer.atms.get(i);
                    player.sendMessage(user.translateC("GLOBAL_EXCLAMATION_INFO") + _COLOR("&e" + i + ". &f" + atm.location.getWorld().getName() + " &7(" + atm.location.getBlockX() + "," + atm.location.getBlockY() + "," + atm.location.getBlockZ() + ")"));
                }
            }

            case "create" -> {
                Block block = player.getTargetBlock(5);
                if(block == null) {
                    player.sendMessage(user.translateC("COMMAND_ATM_CREATE_MUST_LOOK"));
                    return true;
                }

                if(ConfigAtmBuffer.buffer.atms.stream().anyMatch(atm -> atm.location.equals(block.getLocation()))) {
                    player.sendMessage(user.translateC("COMMAND_ATM_CREATE_ALREADY_EXISTS"));
                    return true;
                }

                ConfigAtmBuffer.buffer.atms.add(new ATMObj(true, block.getLocation()));
                ConfigAtmBuffer.save();

                player.sendMessage(user.translateC("COMMAND_ATM_CREATE_SUCCESS"));
            }

            case "delete" -> {
                Block block = player.getTargetBlock(5);
                if(block == null) {
                    player.sendMessage(user.translateC("COMMAND_ATM_DELETE_MUST_LOOK"));
                    return true;
                }

                ATMObj atm = ConfigAtmBuffer.get(block.getLocation());
                if(atm == null) {
                    player.sendMessage(user.translateC("COMMAND_ATM_DELETE_NOT_FOUND"));
                    return true;
                }

                ConfigAtmBuffer.remove(atm.location);
                ConfigAtmBuffer.save();

                player.sendMessage(user.translateC("COMMAND_ATM_DELETE_SUCCESS"));
            }

            case "change" -> {
                if(args.length < 2) {
                    player.sendMessage(user.translateC("COMMAND_USAGE") + "/atm-manage change <id>");
                    return true;
                }

                int id;
                try {
                    id = Integer.parseInt(args[1]);
                } catch(NumberFormatException e) {
                    player.sendMessage(user.translateC("COMMAND_ATM_CHANGE_INVALID_ID"));
                    return true;
                }

                if(id < 0 || id >= ConfigAtmBuffer.buffer.atms.size()) {
                    player.sendMessage(user.translateC("COMMAND_ATM_CHANGE_INVALID_ID"));
                    return true;
                }

                ATMObj atm = ConfigAtmBuffer.buffer.atms.get(id);

                Block block = player.getTargetBlock(5);
                if(block == null) {
                    player.sendMessage(user.translateC("COMMAND_ATM_CHANGE_MUST_LOOK"));
                    return true;
                }

                atm.location = block.getLocation();
                ConfigAtmBuffer.save();

                player.sendMessage(user.translateC("COMMAND_ATM_CHANGE_SUCCESS").replace("%id%", String.valueOf(id)));
            }

            default -> {
                player.sendMessage(user.translateC("COMMAND_USAGE") + "/atm-manage <list|create|delete|change>");
            }
        }
        return true;
    }
}