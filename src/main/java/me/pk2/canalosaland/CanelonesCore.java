package me.pk2.canalosaland;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.command.*;
import me.pk2.canalosaland.config.ConfigLoader;
import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.buffer.DBBufferKits;
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
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

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
        _LOG("Database", "Loading...");
        DBApi.init();
        _LOG("Database", "Updating kits...");
        DBBufferKits.BUFFER.updateKits();

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
        Bukkit.getPluginManager().registerEvents(new TPAListener(), this);
        Bukkit.getPluginManager().registerEvents(new MBListener(), this);

        // Register users if it was a reload
        _LOG("Users", "Registering...");
        Bukkit.getOnlinePlayers().forEach(UserManager::add);

        // Register commands
        _LOG("Commands", "Registering...");
        Objects.requireNonNull(getCommand("canelones-reload")).setExecutor(new CommandReload());

        Objects.requireNonNull(getCommand("pm")).setExecutor(new CommandTCMsg());
        Objects.requireNonNull(getCommand("msg")).setExecutor(new CommandTCMsg());
        Objects.requireNonNull(getCommand("tell")).setExecutor(new CommandTCMsg());
        Objects.requireNonNull(getCommand("r")).setExecutor(new CommandTCMsg());

        Objects.requireNonNull(getCommand("iphone")).setExecutor(new CommandPhone());

        Objects.requireNonNull(getCommand("atm-manage")).setExecutor(new CommandATMManage());

        Objects.requireNonNull(getCommand("baltop")).setExecutor(new CommandBaltop());

        Objects.requireNonNull(getCommand("say")).setExecutor(new CommandSay());

        Objects.requireNonNull(getCommand("soundall")).setExecutor(new CommandSoundAll());

        Objects.requireNonNull(getCommand("halal")).setExecutor(new CommandHalalToggle());

        Objects.requireNonNull(getCommand("lightning")).setExecutor(new CommandLightning());

        Objects.requireNonNull(getCommand("bounty")).setExecutor(new CommandBounty());

        Objects.requireNonNull(getCommand("interface")).setExecutor(new CommandInterface());

        Objects.requireNonNull(getCommand("kits")).setExecutor(new CommandKits());
        Objects.requireNonNull(getCommand("kits-remove")).setExecutor(new CommandKitsRemove());
        Objects.requireNonNull(getCommand("kits-create")).setExecutor(new CommandKitsCreate());
        Objects.requireNonNull(getCommand("kits-give")).setExecutor(new CommandKitsGive());
        Objects.requireNonNull(getCommand("kits-modify")).setExecutor(new CommandKitsModify());

        Objects.requireNonNull(getCommand("spawn")).setExecutor(new CommandSpawn());
        Objects.requireNonNull(getCommand("spawn-set")).setExecutor(new CommandSpawnSet());

        Objects.requireNonNull(getCommand("rename")).setExecutor(new CommandRename());

        Objects.requireNonNull(getCommand("home")).setExecutor(new CommandHome());
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new CommandSetHome());
        Objects.requireNonNull(getCommand("delhome")).setExecutor(new CommandDelHome());

        Objects.requireNonNull(getCommand("lang")).setExecutor(new CommandLang());

        Objects.requireNonNull(getCommand("tpa")).setExecutor(new CommandTpa());
        Objects.requireNonNull(getCommand("tpaccept")).setExecutor(new CommandTpaAccept());
        Objects.requireNonNull(getCommand("tpdeny")).setExecutor(new CommandTpaDeny());

        Objects.requireNonNull(getCommand("mb")).setExecutor(new CommandMysteryBox());

        _LOG("Enabled!");
    }

    @Override
    public void onDisable() {
        tabTask.cancel();
        balTask.cancel();

        Bukkit.getOnlinePlayers().forEach(UserManager::remove);
        UserManager.users.clear();

        _LOG("Disabled!");
    }
}