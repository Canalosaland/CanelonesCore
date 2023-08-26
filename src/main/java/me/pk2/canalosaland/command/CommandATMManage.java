package me.pk2.canalosaland.command;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.config.buff.ConfigAtmBuffer;
import me.pk2.canalosaland.config.buff.atm.ATMObj;
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
            sender.sendMessage(_COLOR("&cThis command can only be executed by players."));
            return true;
        }

        if(!player.hasPermission("canalosaland.atm")) {
            player.sendMessage(_COLOR("&cYou do not have permission to execute this command."));
            return true;
        }

        if(args.length < 1) {
            player.sendMessage(_COLOR("&cUsage: /atm-manage <list|create|delete|change>"));
            return true;
        }

        switch(args[0].toLowerCase()) {
            case "list" -> {
                player.sendMessage(_COLOR("&eATM List:"));
                for(int i = 0; i < ConfigAtmBuffer.buffer.atms.size(); i++) {
                    ATMObj atm = ConfigAtmBuffer.buffer.atms.get(i);
                    player.sendMessage(_COLOR("&e" + i + ". &f" + atm.location.getWorld().getName() + " &7(" + atm.location.getBlockX() + "," + atm.location.getBlockY() + "," + atm.location.getBlockZ() + ")"));
                }
            }

            case "create" -> {
                Block block = player.getTargetBlock(5);
                if(block == null) {
                    player.sendMessage(_COLOR("&cYou must be looking at a block."));
                    return true;
                }

                if(ConfigAtmBuffer.buffer.atms.stream().anyMatch(atm -> atm.location.equals(block.getLocation()))) {
                    player.sendMessage(_COLOR("&cThere is already an ATM at this location."));
                    return true;
                }

                ConfigAtmBuffer.buffer.atms.add(new ATMObj(true, block.getLocation()));
                ConfigAtmBuffer.save();

                player.sendMessage(_COLOR("&aSuccessfully created ATM."));
            }

            case "delete" -> {
                Block block = player.getTargetBlock(5);
                if(block == null) {
                    player.sendMessage(_COLOR("&cYou must be looking at a block."));
                    return true;
                }

                ATMObj atm = ConfigAtmBuffer.get(block.getLocation());
                if(atm == null) {
                    player.sendMessage(_COLOR("&cThere is no ATM at this location."));
                    return true;
                }

                ConfigAtmBuffer.remove(atm.location);
                ConfigAtmBuffer.save();

                player.sendMessage(_COLOR("&aSuccessfully deleted ATM."));
            }

            case "change" -> {
                if(args.length < 2) {
                    player.sendMessage(_COLOR("&cUsage: /atm-manage change <id>"));
                    return true;
                }

                int id;
                try {
                    id = Integer.parseInt(args[1]);
                } catch(NumberFormatException e) {
                    player.sendMessage(_COLOR("&cInvalid ID."));
                    return true;
                }

                if(id < 0 || id >= ConfigAtmBuffer.buffer.atms.size()) {
                    player.sendMessage(_COLOR("&cInvalid ID."));
                    return true;
                }

                ATMObj atm = ConfigAtmBuffer.buffer.atms.get(id);

                Block block = player.getTargetBlock(5);
                if(block == null) {
                    player.sendMessage(_COLOR("&cYou must be looking at a block."));
                    return true;
                }

                atm.location = block.getLocation();
                ConfigAtmBuffer.save();

                player.sendMessage(_COLOR("&aSuccessfully changed ATM " + id + "."));
            }

            default -> {
                player.sendMessage(_COLOR("&cUsage: /atm-manage <list|create|delete|change>"));
            }
        }
        return true;
    }
}