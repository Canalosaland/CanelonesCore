package me.pk2.canalosaland.user;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.config.buff.ConfigLangBuffer;
import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.obj.*;
import me.pk2.canalosaland.db.obj.mb.DBMysteryBoxLocationObj;
import me.pk2.canalosaland.dependencies.DependencyLP;
import me.pk2.canalosaland.interfaces.*;
import me.pk2.canalosaland.interfaces.jobs.*;
import me.pk2.canalosaland.jobs.Job;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.HashMap;

public class User {
    public final Player player;
    public Team team;
    public String lastMessageFrom = null;
    public String locale;
    // Interfaces
    public HashMap<Class<? extends GInterface>, GInterface> interfaces;

    private int userId;
    private DBUserKitObj[] kits;
    private DBHomeObj[] homes;
    private DBUserMBObj[] boxes;
    private DBMysteryBoxLocationObj lastMB;
    private String lastTpa;
    private String menuContext;
    private long lastBizum;
    private String job;
    public User(Player player) {
        this.player = player;
        this.locale = "es";
        this.lastTpa = "";
        this.lastMB = null;
        this.lastBizum = -1L;
        this.boxes = new DBUserMBObj[0];
        this.menuContext = "";
        this.job = "";

        fetchData();

        String teamName = "can_" + RandomStringUtils.randomAlphanumeric(12);
        while(SCORE.getTeam(teamName) != null)
            teamName = "can_" + RandomStringUtils.randomAlphanumeric(12);
        this.team = SCORE.registerNewTeam(teamName);

        this.interfaces = new HashMap<>();
        this.interfaces.put(GInterfacePhone.class, new GInterfacePhone(this));
        this.interfaces.put(GInterfacePhoneProviders.class, new GInterfacePhoneProviders(this));
        this.interfaces.put(GInterfacePhoneBizum.class, new GInterfacePhoneBizum(this));
        this.interfaces.put(GInterfacePhoneKits.class, new GInterfacePhoneKits(this));
        this.interfaces.put(GInterfaceATM.class, new GInterfaceATM(this));
        this.interfaces.put(GInterfaceATMIn.class, new GInterfaceATMIn(this));
        this.interfaces.put(GInterfaceATMOut.class, new GInterfaceATMOut(this));
        this.interfaces.put(GInterfaceKits.class, new GInterfaceKits(this));
        this.interfaces.put(GInterfaceKitsOpen.class, new GInterfaceKitsOpen(this));
        this.interfaces.put(GInterfaceLanguage.class, new GInterfaceLanguage(this));
        this.interfaces.put(GInterfaceMB.class, new GInterfaceMB(this));
        this.interfaces.put(GInterfaceJobBrewer.class, new GInterfaceJobBrewer(this));
        this.interfaces.put(GInterfaceJobEnchanter.class, new GInterfaceJobEnchanter(this));
        this.interfaces.put(GInterfaceJobFisherman.class, new GInterfaceJobFisherman(this));
        this.interfaces.put(GInterfaceJobHunter.class, new GInterfaceJobHunter(this));
        this.interfaces.put(GInterfaceJobWeaponsmith.class, new GInterfaceJobWeaponsmith(this));
        this.interfaces.put(GInterfaceWorlds.class, new GInterfaceWorlds(this));
        this.interfaces.put(GInterfaceShop.class, new GInterfaceShop(this));
    }

    public int getUserId() { return userId; }
    public DBUserKitObj[] getKits() { return kits; }
    public DBHomeObj[] getHomes() { return homes; }
    public DBUserMBObj[] getBoxes() { return boxes; }

    public void setLastTpa(String uuid) { this.lastTpa = uuid; }
    public String getLastTpa() { return this.lastTpa; }

    public void setLastMB(DBMysteryBoxLocationObj obj) { this.lastMB = obj; }
    public DBMysteryBoxLocationObj getLastMB() { return this.lastMB; }

    public void setMenuContext(String menuContext) { this.menuContext = menuContext; }
    public String getMenuContext() { return this.menuContext; }

    public void updateLastBizum() {
        this.lastBizum = System.currentTimeMillis();
    }
    public long getLastBizum() {
        if(lastBizum == -1L)
            return lastBizum;
        return System.currentTimeMillis()-lastBizum;
    }

    public Job getJob() { return CanelonesCore.INSTANCE.jobSystem.job(job==null?"":job); }
    public void newJob(@Nullable Job job) {
        String oldJob = this.job;
		Job oldJobRef = getJob();
		
        if(job == null)
            this.job = null;
        else this.job = job.getName();
		
		if(oldJobRef != null)
			oldJobRef.quit(player);
		if(job != null)
			job.join(player);

        DBApi.enqueue(() -> {
            Connection conn = DBApi.connect();

            int exCode = DBApi.API.users.changeJob(conn, userId, this.job);
            if(exCode != 1) {
                this.job = oldJob;
                sendLocale("JOBS_ERROR_DB");

                if(oldJobRef != null)
                    oldJobRef.join(player);
                if(job != null)
                    job.quit(player);
				
                _LOG("Jobs", "ERROR DB at " + player.getName() + " code " + exCode);
                return;
            }

            DBApi.disconnect(conn);
        });
    }

    public void fetchData() {
        String uuid = _UUID(player);

        _LOG(uuid + "[" + Thread.currentThread().getId() + "]", "Sending data job");
        DBApi.enqueue(() -> {
            _LOG(uuid + "[" + Thread.currentThread().getId() + "]", "Fetching data from database");
            Connection conn = DBApi.connect();

            if(DBApi.API.users.exists(conn, uuid) != 1)
                DBApi.API.users.register(conn, player.getName(), uuid);
            this.userId = DBApi.API.users.getId(conn, uuid);
            this.kits = DBApi.API.users_kits.getByUid(conn, this.userId);
            this.homes = DBApi.API.homes.getByUid(conn, this.userId);
            this.boxes = DBApi.API.users_mb.getByUid(conn, this.userId);

            DBUserDataObj data = DBApi.API.users.getData(conn, this.userId);
            this.locale = data.locale;
            this.job = data.job;

            DBBanObj[] bansUser = DBApi.API.bans.getBans(conn, (short)0, player.getName().toLowerCase());
            DBBanObj[] bansIP = new DBBanObj[0];
            if(player.getAddress() != null)
                bansIP = DBApi.API.bans.getBans(conn, (short)1, player.getAddress().getHostName());

            DBBanObj[] bans = new DBBanObj[bansUser.length + bansIP.length];
            for(int i = 0; i < bans.length; i++) {
                if(i >= bansUser.length) {
                    bans[i] = bansIP[i - bansUser.length];
                    continue;
                }

                bans[i] = bansUser[i];
            }

            for(DBBanObj ban : bans)
                if(!ban.expired()) {
                    sendBanKick(ban);

                    DBApi.disconnect(conn);
                    _LOG(uuid + "[" + Thread.currentThread().getId() + "]", "User is banned(" + ban.getId() + ")");
                    return;
                }

            DBApi.disconnect(conn);
            _LOG(uuid + "[" + Thread.currentThread().getId() + "]", "Data fetched from database");
        });
    }
    public void sendBanKick(DBBanObj ban) {
        Bukkit.getScheduler().runTask(CanelonesCore.INSTANCE, () -> player.kick(Component.text(_COLOR(
                ban.getTime() == 0L ? String.format("""
                                    &cYou are permanently banned from this server!
                                                                        
                                    &7Reason: &f%s
                                    &7Find out more: &f%s
                                    
                                    &7Ban ID: &f%d
                                    &7Sharing your Ban ID may affect the processing of your appeal!
                                    """, ban.getReason(), ConfigMainBuffer.buffer.appeal_link, ban.getId()) : String.format("""
                                    &cYou are temporarily banned for %s from this server!
                                                                        
                                    &7Reason: &f%s
                                    &7Find out more: &f%s
                                    
                                    &7Ban ID: &f%d
                                    &7Sharing your Ban ID may affect the processing of your appeal!
                                    """, ban.getTimeExp(), ban.getReason(), ConfigMainBuffer.buffer.appeal_link, ban.getId())
        ))));
    }

    public String translate(String key) { return ConfigLangBuffer.translate(this, key); }
    public String translateC(String key) { return ConfigLangBuffer.translateC(this, key); }
    public void sendLocale(String key) { player.sendMessage(translateC(key)); }
    public void sendLocaleArgs(String key, Object... args) { player.sendMessage(_COLOR(String.format(translate(key), args))); }

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

        Job job = CanelonesCore.INSTANCE.jobSystem.job(this.job);
        if(job != null)
            job.join(player);
    }

    public void handleQuit() {
        team.removeEntry(player.getName());
        team.unregister();

        team = null;
        Job job = CanelonesCore.INSTANCE.jobSystem.job(this.job);
        if(job != null)
            job.quit(player);
    }
}