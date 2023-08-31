package me.pk2.canalosaland.command;

import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.buffer.DBBufferKits;
import me.pk2.canalosaland.db.obj.DBKitObj;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandKitsGive implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("canalosaland.kits.give")) {
            sender.sendMessage(_COLOR("&cNo tienes permisos para ejecutar este comando."));
            return true;
        }

        if(args.length < 2) {
            sender.sendMessage(_COLOR("&cUso: /" + label + " <jugador> <kid>"));
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if(player == null) {
            sender.sendMessage(_COLOR("&cEl jugador no esta conectado."));
            return true;
        }

        User user = UserManager.get(player);

        int kid;
        try {
            kid = Integer.parseInt(args[1]);
        } catch(NumberFormatException exception) {
            sender.sendMessage(_COLOR("&cEl id del kit debe ser un numero."));
            return true;
        }

        DBKitObj kit = DBBufferKits.BUFFER.getKit(kid);
        if(kit == null) {
            sender.sendMessage(_COLOR("&cNo existe un kit con ese id."));
            return true;
        }

        int amount;
        if(args.length > 2) {
            try {
                amount = Integer.parseInt(args[2]);
            } catch(NumberFormatException exception) {
                sender.sendMessage(_COLOR("&cLa cantidad debe ser un numero."));
                return true;
            }

            if(amount < 1) {
                sender.sendMessage(_COLOR("&cLa cantidad debe ser mayor a 0."));
                return true;
            }
        } else amount = 1;

        sender.sendMessage(_COLOR("&eConectando con la base de datos..."));
        DBApi.enqueue(() -> {
            Connection conn = DBApi.connect();

            sender.sendMessage(_COLOR("&eDando kit..."));
            int exCode = DBApi.API.users_kits.add(conn, user.getUserId(), kid, amount);
            if(exCode != 1) {
                sender.sendMessage(_COLOR("&cOcurrio un error al dar el kit. [" + exCode + "]"));
                return;
            }

            user.fetchData();
            sender.sendMessage(_COLOR("&aSe ha dado el kit correctamente."));
            player.sendMessage(_COLOR("&aHas recibido " + amount + " " + kit.name + "&a."));
            _SOUND_EXP(player);
        });
        return true;
    }
}