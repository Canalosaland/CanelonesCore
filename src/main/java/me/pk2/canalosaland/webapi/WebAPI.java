package me.pk2.canalosaland.webapi;

import static me.pk2.canalosaland.util.Wrapper.*;

import com.sun.net.httpserver.HttpServer;
import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import me.pk2.canalosaland.webapi.handler.handlers.WebServerHandlerRoot;
import me.pk2.canalosaland.webapi.handler.handlers.api.WebServerHandlerAPICarriers;
import me.pk2.canalosaland.webapi.handler.handlers.api.WebServerHandlerAPIPlayer;
import me.pk2.canalosaland.webapi.handler.handlers.api.WebServerHandlerAPIPlayers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebAPI {
    private HttpServer server;

    public void start() {
        stop();

        _LOG("WebAPI", "Starting...");
        try {
            this.server = HttpServer.create(new InetSocketAddress(ConfigMainBuffer.buffer.webapi.port), 0);
        } catch (IOException exception) {
            exception.printStackTrace();
            _LOG("WebAPI", "Failed to start!");

            return;
        }

        _LOG("WebAPI", "Registering handlers...");
        server.createContext("/", new WebServerHandlerRoot());
        server.createContext("/api", new WebServerHandlerRoot());
        server.createContext("/api/players", new WebServerHandlerAPIPlayers());
        server.createContext("/api/player", new WebServerHandlerAPIPlayer());
        server.createContext("/api/carriers", new WebServerHandlerAPICarriers());

        server.start();
        _LOG("WebAPI", "Started!");
    }

    public void stop() {
        _LOG("WebAPI", "Stopping...");

        if(server != null) {
            server.stop(0);
            server = null;
        }
    }
}