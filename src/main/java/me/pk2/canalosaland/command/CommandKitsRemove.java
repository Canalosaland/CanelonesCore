package me.pk2.canalosaland.command;

import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.buffer.DBBufferKits;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandKitsRemove implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("canalosaland.kits.remove")) {
            sender.sendMessage(_COLOR("&cNo tienes permiso para ejecutar este comando."));
            return true;
        }

        if(args.length < 1) {
            sender.sendMessage(_COLOR("&cUso: /kitsremove <id>"));
            return true;
        }

        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch(NumberFormatException ex) {
            sender.sendMessage(_COLOR("&cEl id debe ser un numero."));
            return true;
        }

        if(id < 0) {
            sender.sendMessage(_COLOR("&cEl id debe ser mayor o igual a 0."));
            return true;
        }

        if(DBBufferKits.BUFFER.getKit(id) == null) {
            sender.sendMessage(_COLOR("&cNo existe un kit con ese id."));
            return true;
        }

        sender.sendMessage(_COLOR("&eConectando a la base de datos..."));
        DBApi.enqueue(() -> {
            Connection conn = DBApi.connect();

            sender.sendMessage(_COLOR("&eEliminando kit..."));
            int exCode = DBApi.API.kits.remove(conn, id);

            if(exCode == 1) {
                DBBufferKits.BUFFER.updateKits();
                sender.sendMessage(_COLOR("&aKit eliminado correctamente."));
            } else sender.sendMessage(_COLOR("&cOcurrio un error al eliminar el kit. [" + exCode + "]"));

            DBApi.disconnect(conn);
        });

        return true;
    }
}