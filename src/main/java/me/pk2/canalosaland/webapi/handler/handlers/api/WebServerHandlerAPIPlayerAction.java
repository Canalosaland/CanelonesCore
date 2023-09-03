package me.pk2.canalosaland.webapi.handler.handlers.api;

import static me.pk2.canalosaland.util.Wrapper.*;

import com.dbteku.telecom.models.Carrier;
import com.dbteku.telecom.models.CellTower;
import com.dbteku.telecom.models.WorldLocation;
import com.sun.net.httpserver.HttpExchange;
import me.pk2.canalosaland.dependencies.DependencyAuthMe;
import me.pk2.canalosaland.dependencies.DependencyTCom;
import me.pk2.canalosaland.dependencies.DependencyVault;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import me.pk2.canalosaland.webapi.handler.WebServerHandler;
import me.pk2.canalosaland.webapi.handler.response.WebServerResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class WebServerHandlerAPIPlayerAction extends WebServerHandler {
    public static final HashMap<String, String> authTokens = new HashMap<>();

    public String generateToken(User user, String pass) {
        String decoToken = System.currentTimeMillis() + user.player.getName() + user.player.getUniqueId() + pass;
        return _HASH(decoToken);
    }

    @Override
    public WebServerResponse createResponse(HttpExchange httpExchange) {
        String[] args = httpExchange.getRequestURI().getPath().split("/");

        String uuid = args[3].toLowerCase();
        User user = UserManager.users.get(uuid);
        if(user == null)
            return new WebServerResponse("Offline player");

        String actionResponse = """
                <h1>%1 actions</h1>
                <p>/api/player/%2/action/session_auth?pass=</p>
                <p>/api/player/%2/action/session_valid?token=</p>
                <p>/api/player/%2/action/session_close?token=</p>
                <p>/api/player/%2/action/carrier_join?token=&carrier=</p>
                <p>/api/player/%2/action/carrier_leave?token=</p>
                <p>/api/player/%2/action/carrier_signal</p>
                <p>/api/player/%2/action/bizum_send?token=&to=&amount=</p>
                """;
        actionResponse = actionResponse.replace("%1", user.player.getName());
        actionResponse = actionResponse.replace("%2", uuid);
        if(args.length < 5)
            return new WebServerResponse(actionResponse);

        String[] query = getQuery(httpExchange.getRequestURI());
        String lastArg = args[args.length - 1];
        if(lastArg.contains("?"))
            lastArg = lastArg.substring(0, lastArg.indexOf("?"));

        switch(lastArg.toLowerCase()) {
            case "session_auth" -> {
                if(query.length < 1)
                    return new WebServerResponse(actionResponse);

                String[] pass = query[0].split("=");
                if(pass.length < 2 || !pass[0].equalsIgnoreCase("pass"))
                    return new WebServerResponse(actionResponse);

                if(!DependencyAuthMe.auth(user.player.getName(), pass[1]))
                    return new WebServerResponse("Invalid password");
                String token = generateToken(user, pass[1]);

                authTokens.put(token, user.player.getName());
                return new WebServerResponse(token);
            }

            case "session_valid" -> {
                if(query.length < 1)
                    return new WebServerResponse(actionResponse);

                String[] token = query[0].split("=");
                if(token.length < 2 || !token[0].equalsIgnoreCase("token"))
                    return new WebServerResponse(actionResponse);

                if(!authTokens.containsKey(token[1]))
                    return new WebServerResponse("Invalid token");
                return new WebServerResponse("Valid token");
            }

            case "session_close" -> {
                if(query.length < 1)
                    return new WebServerResponse(actionResponse);

                String[] token = query[0].split("=");
                if(token.length < 2 || !token[0].equalsIgnoreCase("token"))
                    return new WebServerResponse(actionResponse);

                if(!authTokens.containsKey(token[1]))
                    return new WebServerResponse("Invalid token");
                authTokens.remove(token[1]);
                return new WebServerResponse("Closed session");
            }

            case "carrier_join" -> {
                if(query.length < 2)
                    return new WebServerResponse(actionResponse);

                String[] token = query[0].split("=");
                if(token.length < 2 || !token[0].equalsIgnoreCase("token"))
                    return new WebServerResponse(actionResponse);

                if(!authTokens.containsKey(token[1]))
                    return new WebServerResponse("Invalid token");

                String[] carrier = query[1].split("=");
                if(carrier.length < 2 || !carrier[0].equalsIgnoreCase("carrier"))
                    return new WebServerResponse(actionResponse);

                Carrier c = DependencyTCom.API.getCarrierByName(carrier[1]);
                if(c == null)
                    return new WebServerResponse("Invalid carrier");

                user.player.sendMessage(user.translateC("WAPI_CARRIER_JOIN").replace("%carrier%", c.getName()));
                c.subscribe(user.player.getName());
                return new WebServerResponse("Joined carrier");
            }

            case "carrier_leave" -> {
                if(query.length < 1)
                    return new WebServerResponse(actionResponse);

                String[] token = query[0].split("=");
                if(token.length < 2 || !token[0].equalsIgnoreCase("token"))
                    return new WebServerResponse(actionResponse);

                if(!authTokens.containsKey(token[1]))
                    return new WebServerResponse("Invalid token");

                Carrier c = DependencyTCom.getCarrierByPlayer(user.player);
                if(c == null)
                    return new WebServerResponse("You are not in a carrier");
                if(c.getOwner().contentEquals(user.player.getName()))
                    return new WebServerResponse("You cannot leave your own carrier");

                user.player.sendMessage(user.translateC("WAPI_CARRIER_LEAVE"));
                c.unsubscribe(user.player.getName());
                return new WebServerResponse("Left carrier");
            }

            case "carrier_signal" -> {
                Carrier carrier = DependencyTCom.getCarrierByPlayer(user.player);
                if(carrier == null)
                    return new WebServerResponse("You are not in a carrier");

                WorldLocation location = new WorldLocation(user.player.getLocation());
                CellTower tower = carrier.getByLocation(location);
                if(tower == null)
                    return new WebServerResponse("0");
                return new WebServerResponse(String.valueOf(tower.determineStrength(location)));
            }

            case "bizum_send" -> {
                if(query.length < 3)
                    return new WebServerResponse(actionResponse);

                String[] token = query[0].split("=");
                if(token.length < 2 || !token[0].equalsIgnoreCase("token"))
                    return new WebServerResponse(actionResponse);

                if(!authTokens.containsKey(token[1]))
                    return new WebServerResponse("Invalid token");

                String[] to = query[1].split("=");
                if(to.length < 2 || !to[0].equalsIgnoreCase("to"))
                    return new WebServerResponse(actionResponse);

                String[] amount = query[2].split("=");
                if(amount.length < 2 || !amount[0].equalsIgnoreCase("amount"))
                    return new WebServerResponse(actionResponse);

                Player player = Bukkit.getPlayer(to[1]);
                if(player == null)
                    return new WebServerResponse("Invalid player");

                double amountDouble;
                try {
                    amountDouble = Double.parseDouble(amount[1]);
                } catch(NumberFormatException e) {
                    return new WebServerResponse("Invalid amount");
                }

                if(amountDouble <= 0)
                    return new WebServerResponse("Invalid amount");

                if(!DependencyVault.has(user.player, amountDouble))
                    return new WebServerResponse("Insufficient funds");

                DependencyVault.withdraw(user.player, amountDouble);
                DependencyVault.deposit(Bukkit.getPlayer(to[1]), amountDouble);

                user.player.sendMessage(user.translateC("INTERFACE_PHONE_BIZUM_SUCCESS_SENT").replace("%amount%", String.valueOf(amountDouble)).replace("%player%", player.getName()));
                player.sendMessage(_SENDER_TRANSLATE(player, "INTERFACE_PHONE_BIZUM_SUCCESS_RECEIVED").replace("%amount%", String.valueOf(amountDouble)).replace("%player%", user.player.getName()));

                _SOUND_NOTIFICATION(user.player);
                _SOUND_NOTIFICATION(player);
                return new WebServerResponse("Sent");
            }

            default -> {}
        }

        return new WebServerResponse(actionResponse);
    }
}