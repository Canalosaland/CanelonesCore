package me.pk2.canalosaland.webapi.handler.handlers.api;

import com.dbteku.telecom.models.Carrier;
import com.sun.net.httpserver.HttpExchange;
import me.pk2.canalosaland.dependencies.DependencyTCom;
import me.pk2.canalosaland.dependencies.DependencyVault;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import me.pk2.canalosaland.webapi.handler.WebServerHandler;
import me.pk2.canalosaland.webapi.handler.response.WebServerResponse;

public class WebServerHandlerAPIPlayer extends WebServerHandler {
    WebServerHandlerAPIPlayerAction actionHandler = new WebServerHandlerAPIPlayerAction();

    @Override
    public WebServerResponse createResponse(HttpExchange httpExchange) {
        String[] args = httpExchange.getRequestURI().getPath().split("/");
        if(args.length < 4)
            return new WebServerResponse("Invalid player");

        String uuid = args[3].toLowerCase();
        User user = UserManager.users.get(uuid);
        if(user == null)
            return new WebServerResponse("Offline player");

        if(args.length > 4 && args[4].equalsIgnoreCase("action"))
            return actionHandler.createResponse(httpExchange);

        Carrier carrier = DependencyTCom.getCarrierByPlayer(user.player);
        String isOwner = carrier==null?"false":(carrier.getOwner().contentEquals(user.player.getName())?"true":"false");

        StringBuilder builder = new StringBuilder();
        builder.append(uuid).append("\n");
        builder.append(user.player.getName()).append("\n");
        builder.append(DependencyVault.getBalance(user.player)).append("\n");
        builder.append(carrier==null?"null":carrier.getName()).append("\n");
        builder.append(isOwner);

        return new WebServerResponse(builder.toString());
    }
}