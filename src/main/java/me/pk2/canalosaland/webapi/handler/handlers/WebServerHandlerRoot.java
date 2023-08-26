package me.pk2.canalosaland.webapi.handler.handlers;

import static me.pk2.canalosaland.CanelonesCore.INSTANCE;

import com.sun.net.httpserver.HttpExchange;
import me.pk2.canalosaland.webapi.handler.WebServerHandler;
import me.pk2.canalosaland.webapi.handler.response.WebServerResponse;

public class WebServerHandlerRoot extends WebServerHandler {
    @Override
    public WebServerResponse createResponse(HttpExchange httpExchange) {
        return new WebServerResponse("This server is running CanelonesCore " + INSTANCE.getDescription().getVersion() + " by PK2_Stimpy");
    }
}