package me.pk2.canalosaland.webapi.handler.handlers.api;

import com.sun.net.httpserver.HttpExchange;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import me.pk2.canalosaland.webapi.handler.WebServerHandler;
import me.pk2.canalosaland.webapi.handler.response.WebServerResponse;

import java.util.Map;
import java.util.Set;

public class WebServerHandlerAPIPlayers extends WebServerHandler {
    @Override
    public WebServerResponse createResponse(HttpExchange httpExchange) {
        Set<Map.Entry<String, User>> users = UserManager.users.entrySet();

        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, User> entry : users)
            builder.append(entry.getKey()).append(" ").append(entry.getValue().player.getName()).append("\n");
        String response = builder.toString();
        if(response.length() < 1)
            return new WebServerResponse("No players online!");
        return new WebServerResponse(response.substring(0, response.length() - 1));
    }
}