package me.pk2.canalosaland.command;

import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandDelHome implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(_COLOR("&cEste comando solo puede ser ejecutado por un jugador."));
            return true;
        }

        if(args.length == 0) {
            player.sendMessage(_COLOR("&cUso: /delhome <nombre>"));
            return true;
        }

        User user = UserManager.get(player);
        if(user == null) {
            player.sendMessage(_COLOR("&8(&c!&8) &aCasa &8» &7No se ha podido encontrar tu usuario."));
            return true;
        }

        String name = args[0];
        if(name.length() > 24) {
            player.sendMessage(_COLOR("&8(&c!&8) &aCasa &8» &7El nombre de la casa no puede ser mayor a 24 caracteres."));
            return true;
        }

        player.sendMessage(_COLOR("&8(&e!&8) &aCasa &8» &7Conectando con la base de datos..."));
        DBApi.enqueue(() -> {
            Connection conn = DBApi.connect();
            if(DBApi.API.homes.existsHome(conn, user.getUserId(), name) != 1) {
                player.sendMessage(_COLOR("&8(&c!&8) &aCasa &8» &7No tienes una casa con ese nombre."));
                DBApi.disconnect(conn);
                return;
            }

            int exCode = DBApi.API.homes.removeHome(conn, user.getUserId(), name);
            if(exCode != 1) {
                player.sendMessage(_COLOR("&8(&c!&8) &aCasa &8» &7Ha ocurrido un error al eliminar la casa. [" + exCode + "]"));
                DBApi.disconnect(conn);
                return;
            }

            DBApi.disconnect(conn);
            user.fetchData();
            player.sendMessage(_COLOR("&8(&a!&8) &aCasa &8» &7Has eliminado la casa con el nombre &e" + name + "&7."));
        });
        return true;
    }
}