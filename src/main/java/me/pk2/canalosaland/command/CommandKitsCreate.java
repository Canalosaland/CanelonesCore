package me.pk2.canalosaland.command;

import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.buffer.DBBufferKits;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandKitsCreate implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("canalosaland.kits.create")) {
            sender.sendMessage(_COLOR("&cNo tienes permisos para ejecutar este comando."));
            return true;
        }

        if(!(sender instanceof Player player)) {
            sender.sendMessage(_COLOR("&cEste comando solo puede ser ejecutado por un jugador."));
            return true;
        }

        if(args.length < 1) {
            sender.sendMessage(_COLOR("&cUso: /" + label + " <nombre>"));
            return true;
        }

        String name = args[0];
        if(name.length() > 32) {
            sender.sendMessage(_COLOR("&cEl nombre del kit no puede ser mayor a 32 caracteres."));
            return true;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();
        ItemStack[] slots = new ItemStack[27];
        for(int i = 0; i < 27; i++)
            slots[i] = player.getInventory().getItem(i+9);

        sender.sendMessage(_COLOR("&eConectando con la base de datos..."));
        DBApi.enqueue(() -> {
            Connection conn = DBApi.connect();

            sender.sendMessage(_COLOR("&eCreando kit..."));
            if(DBApi.API.kits.exists(conn, name) == 1) {
                sender.sendMessage(_COLOR("&cYa existe un kit con ese nombre."));
                return;
            }

            int exCode = DBApi.API.kits.register(conn, name, slots, hand);
            if(exCode != 1) {
                sender.sendMessage(_COLOR("&cOcurrio un error al crear el kit. [" + exCode + "]"));
                return;
            }

            DBBufferKits.BUFFER.updateKits();
            sender.sendMessage(_COLOR("&aKit creado correctamente."));
        });
        return true;
    }
}
