package me.pk2.canalosaland;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.command.*;
import me.pk2.canalosaland.config.ConfigLoader;
import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.dependencies.*;
import me.pk2.canalosaland.listeners.*;
import me.pk2.canalosaland.reflections.ReflectionsManager;
import me.pk2.canalosaland.runnables.BalanceTopRunnable;
import me.pk2.canalosaland.runnables.TablistRunnable;
import me.pk2.canalosaland.user.UserManager;
import me.pk2.canalosaland.util.SignMenuFactory;
import me.pk2.canalosaland.webapi.WebAPI;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.NPC;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class CanelonesCore extends JavaPlugin {
    public static CanelonesCore INSTANCE;
    public ReflectionsManager reflectionsManager;
    public SignMenuFactory signMenuFactory;
    public WebAPI webAPI;

    private BukkitTask tabTask;
    private BukkitTask balTask;

    @Override
    public void onEnable() {
        INSTANCE = this;

        // Check for LuckPerms plugin
        try {
            Class.forName("net.luckperms.api.LuckPerms");

            RegisteredServiceProvider<LuckPerms> provider = getServer().getServicesManager().getRegistration(LuckPerms.class);
            DependencyLP.register(provider);
        } catch (ClassNotFoundException exception) {
            getLogger().warning("LuckPerms is not installed, LuckPerms prefixes will not work!");
        }

        // Check for PlaceholderAPI plugin
        if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
            DependencyPAPI.register();
        else getLogger().warning("PlaceholderAPI is not installed, placeholders will not work!");

        // Check for Telecom plugin
        if(getServer().getPluginManager().getPlugin("Telecom") != null)
            DependencyTCom.register();
        else getLogger().warning("Telecom is not installed, everything that has to do with telecom will not work!");

        // Check for Vault plugin
        if(getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            DependencyVault.register(rsp);
        } else getLogger().warning("Vault is not installed, economy will not work!");

        // Check for ProtocolLib plugin(no hook)
        if(getServer().getPluginManager().getPlugin("ProtocolLib") == null)
            getLogger().warning("ProtocolLib is not installed, some features will not work!");
        else getLogger().info("ProtocolLib registered!");

        // Check for AuthMe plugin
        if(getServer().getPluginManager().getPlugin("AuthMe") != null)
            DependencyAuthMe.register();
        else getLogger().warning("AuthMe is not installed, some features will not work!");

        // Load config
        ConfigLoader.load();

        // Load database
        DBApi.init();

        // Register sign menu factory
        _LOG("SignMenuFactory", "Registering...");
        this.signMenuFactory = new SignMenuFactory(this);

        // Register reflections
        _LOG("ReflectionsManager", "Registering...");
        this.reflectionsManager = new ReflectionsManager();
        this.reflectionsManager.init();

        // Check for score
        _LOG("Scoreboard", "Checking...");
        if(SCORE == null)
            SCORE = Bukkit.getScoreboardManager().getMainScoreboard();

        _LOG("Runnables", "Registering...");
        tabTask = new TablistRunnable().runTaskTimer(this, 0L, 20L * ConfigMainBuffer.buffer.tablist.update);
        balTask = new BalanceTopRunnable().runTaskTimerAsynchronously(this, 0L, 20L * 60L * ConfigMainBuffer.buffer.baltop.update);

        // Register listeners
        _LOG("Listeners", "Registering...");
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new InterfaceListener(), this);
        Bukkit.getPluginManager().registerEvents(new ATMListener(), this);
        Bukkit.getPluginManager().registerEvents(new HalalListener(), this);
        Bukkit.getPluginManager().registerEvents(new BountyListener(), this);

        // Register users if it was a reload
        _LOG("Users", "Registering...");
        Bukkit.getOnlinePlayers().forEach(UserManager::add);

        // Register commands
        _LOG("Commands", "Registering...");
        getCommand("canelones-reload").setExecutor(new CommandReload());

        getCommand("pm").setExecutor(new CommandTCMsg());
        getCommand("msg").setExecutor(new CommandTCMsg());
        getCommand("tell").setExecutor(new CommandTCMsg());
        getCommand("r").setExecutor(new CommandTCMsg());

        getCommand("iphone").setExecutor(new CommandPhone());

        getCommand("atm-manage").setExecutor(new CommandATMManage());

        getCommand("baltop").setExecutor(new CommandBaltop());

        getCommand("say").setExecutor(new CommandSay());

        getCommand("soundall").setExecutor(new CommandSoundAll());

        getCommand("halal").setExecutor(new CommandHalalToggle());

        getCommand("lightning").setExecutor(new CommandLightning());

        getCommand("bounty").setExecutor(new CommandBounty());

        getCommand("interface").setExecutor(new CommandInterface());

        //getCommand("npctest").setExecutor(new CommandNPCTest());

        _LOG("Enabled!");
    }

    @Override
    public void onDisable() {
        tabTask.cancel();

        Bukkit.getOnlinePlayers().forEach(UserManager::remove);
        UserManager.users.clear();

        _LOG("Disabled!");
    }
}