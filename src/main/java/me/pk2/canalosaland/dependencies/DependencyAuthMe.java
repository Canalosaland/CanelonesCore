package me.pk2.canalosaland.dependencies;

import static me.pk2.canalosaland.util.Wrapper.*;

import fr.xephi.authme.api.v3.AuthMeApi;

public class DependencyAuthMe {
    public static AuthMeApi api;

    public static void register() {
        api = AuthMeApi.getInstance();

        _LOG("AuthMe registered!");
    }

    public static boolean auth(String name, String password) {
        if(api == null)
            return false;
        return api.checkPassword(name, password);
    }
}