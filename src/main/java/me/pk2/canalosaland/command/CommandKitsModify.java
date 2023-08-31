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

public class CommandKitsModify implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("canalosaland.kits.modify")) {
            sender.sendMessage(_COLOR("&cNo tienes permisos para ejecutar este comando."));
            return true;
        }

        if(!(sender instanceof Player player)) {
            sender.sendMessage(_COLOR("&cEste comando solo puede ser ejecutado por un jugador."));
            return true;
        }

        if(args.length < 1) {
            sender.sendMessage(_COLOR("&cUso: /" + label + " <kit_id>"));
            return true;
        }

        int kid;
        try {
            kid = Integer.parseInt(args[0]);
        } catch(NumberFormatException ex) {
            sender.sendMessage(_COLOR("&cEl id del kit debe ser un numero."));
            return true;
        }

        if(kid < 0) {
            sender.sendMessage(_COLOR("&cEl id del kit debe ser mayor o igual a 0."));
            return true;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();
        ItemStack[] slots = new ItemStack[27];
        for(int i = 0; i < 27; i++)
            slots[i] = player.getInventory().getItem(i+9);

        sender.sendMessage(_COLOR("&eConectando con la base de datos..."));
        DBApi.enqueue(() -> {
            Connection conn = DBApi.connect();

            sender.sendMessage(_COLOR("&eModificando kit..."));
            int exCode = DBApi.API.kits.modify(conn, kid, slots, hand);
            if(exCode != 1) {
                sender.sendMessage(_COLOR("&cOcurrio un error al modificar el kit. [" + exCode + "]"));
                return;
            }

            DBBufferKits.BUFFER.updateKits();
            sender.sendMessage(_COLOR("&aKit modificado correctamente."));
        });
        return true;
    }
}