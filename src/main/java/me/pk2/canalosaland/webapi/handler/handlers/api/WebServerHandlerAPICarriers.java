package me.pk2.canalosaland.webapi.handler.handlers.api;

import com.dbteku.telecom.models.Carrier;
import com.sun.net.httpserver.HttpExchange;
import me.pk2.canalosaland.dependencies.DependencyTCom;
import me.pk2.canalosaland.webapi.handler.WebServerHandler;
import me.pk2.canalosaland.webapi.handler.response.WebServerResponse;

import java.util.List;

public class WebServerHandlerAPICarriers extends WebServerHandler {
    @Override
    public WebServerResponse createResponse(HttpExchange httpExchange) {
        List<Carrier> carriers = DependencyTCom.API.getAllCarriers();

        StringBuilder builder = new StringBuilder();
        for(Carrier carrier : carriers)
            builder
                    .append(carrier.getName()).append(" ")
                    .append(carrier.getSubscribers().size()).append(" ")
                    .append(carrier.getPricePerText()).append("\n");

        String response = builder.toString();
        if(response.length() < 1)
            return new WebServerResponse("No carriers");
        return new WebServerResponse(response.substring(0, response.length() - 1));
    }
}