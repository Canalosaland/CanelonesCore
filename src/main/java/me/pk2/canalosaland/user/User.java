package me.pk2.canalosaland.user;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.dependencies.DependencyLP;
import me.pk2.canalosaland.interfaces.*;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class User {
    public final Player player;
    public Team team;
    public String lastMessageFrom = null;
    // Interfaces
    public HashMap<Class<? extends GInterface>, GInterface> interfaces;
    public User(Player player) {
        this.player = player;

        String teamName = "can_" + RandomStringUtils.randomAlphanumeric(12);
        while(SCORE.getTeam(teamName) != null)
            teamName = "can_" + RandomStringUtils.randomAlphanumeric(12);
        this.team = SCORE.registerNewTeam(teamName);

        this.interfaces = new HashMap<>();
        this.interfaces.put(GInterfacePhone.class, new GInterfacePhone(this));
        this.interfaces.put(GInterfacePhoneProviders.class, new GInterfacePhoneProviders(this));
        this.interfaces.put(GInterfacePhoneBizum.class, new GInterfacePhoneBizum(this));
        this.interfaces.put(GInterfaceATM.class, new GInterfaceATM(this));
        this.interfaces.put(GInterfaceATMIn.class, new GInterfaceATMIn(this));
        this.interfaces.put(GInterfaceATMOut.class, new GInterfaceATMOut(this));
    }

    public void updateTeam() {
        if(this.team == null)
            return;

        this.team.setPrefix(_COLOR(DependencyLP.getPrefix(player)));
        this.team.setSuffix(_COLOR(DependencyLP.getSuffix(player)));
    }

    public void openI(Class<? extends GInterface> iClass) {
        GInterface gui = interfaces.get(iClass);
        if(gui == null)
            return;
        gui.open();
    }

    public void handleJoin() {
        for(GInterface gui : interfaces.values())
            gui.init();

        team.addEntry(player.getName());
        updateTeam();
    }

    public void handleQuit() {
        team.removeEntry(player.getName());
        team.unregister();

        team = null;
    }
}