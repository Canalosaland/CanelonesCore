package me.pk2.canalosaland.command;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandRename implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(_COLOR("&cEste comando solo puede ser ejecutado por un jugador."));
            return true;
        }

        if(!player.hasPermission("canalosaland.rename")) {
            player.sendMessage(_COLOR("&cNo tienes permisos para ejecutar este comando."));
            return true;
        }

        if(args.length < 1) {
            player.sendMessage(_COLOR("&cUso: /rename <nombre>"));
            return true;
        }

        if(player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.sendMessage(_COLOR("&cDebes tener un item en la mano."));
            return true;
        }

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < args.length; i++) {
            builder.append(args[i]);
            if(i != args.length - 1)
                builder.append(" ");
        }

        String name = _COLOR(builder.toString().trim());
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        meta.displayName(Component.text(name));
        player.getInventory().getItemInMainHand().setItemMeta(meta);

        _SOUND_CLICK(player);
        return true;
    }
}