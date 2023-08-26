package me.pk2.canalosaland.command;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandNPCTest implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(_COLOR("&cThis command can only be executed by players!"));
            return true;
        }

        Player player = (Player) sender;
        player.sendMessage(_COLOR("&aCreating NPC..."));

        NPC npc = new NPC("&cAtun", player.getLocation());
        try {
            npc.spawn(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}