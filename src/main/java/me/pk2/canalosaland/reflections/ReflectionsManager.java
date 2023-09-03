package me.pk2.canalosaland.reflections;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.reflections.buffer.ClassBuffer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

public class ReflectionsManager {
    public final ClassBuffer _CraftServer;
    public final ClassBuffer _CraftWorld;
    public final ClassBuffer _MinecraftServer;
    public final ClassBuffer _WorldServer;
    public final ClassBuffer _GameProfile;
    public final ClassBuffer _CraftPlayer;
    public final ClassBuffer _EntityPlayer;
    public final ClassBuffer _EntityHuman;
    public final ClassBuffer _Entity;
    public final ClassBuffer _PlayerInteractManager;
    public final ClassBuffer _PlayerConnection;
    public final ClassBuffer _PacketPlayOutPlayerInfo;
    public final ClassBuffer _PPOPI_EnumPlayerInfoAction;
    public Object _PPOPI_EnumPlayerInfoAction_ADD_PLAYER;
    public final ClassBuffer _PacketPlayOutNamedEntitySpawn;
    public final ClassBuffer _PacketPlayOutEntityHeadRotation;
    public final ClassBuffer _SkullMeta;

    public ReflectionsManager() {
        String version = getVersion();

        _CraftServer = new ClassBuffer("org.bukkit.craftbukkit." + version + ".CraftServer");
        _CraftWorld = new ClassBuffer("org.bukkit.craftbukkit." + version + ".CraftWorld");
        _MinecraftServer = new ClassBuffer("net.minecraft.server." + version + ".MinecraftServer");
        _WorldServer = new ClassBuffer("net.minecraft.server." + version + ".WorldServer");
        _GameProfile = new ClassBuffer("com.mojang.authlib.GameProfile");
        _CraftPlayer = new ClassBuffer("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
        _EntityPlayer = new ClassBuffer("net.minecraft.server." + version + ".EntityPlayer");
        _EntityHuman = new ClassBuffer("net.minecraft.server." + version + ".EntityHuman");
        _Entity = new ClassBuffer("net.minecraft.server." + version + ".Entity");
        _PlayerInteractManager = new ClassBuffer("net.minecraft.server." + version + ".PlayerInteractManager");
        _PlayerConnection = new ClassBuffer("net.minecraft.server." + version + ".PlayerConnection");
        _PacketPlayOutPlayerInfo = new ClassBuffer("net.minecraft.server." + version + ".PacketPlayOutPlayerInfo");
        _PPOPI_EnumPlayerInfoAction = new ClassBuffer("net.minecraft.server." + version + ".PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
        _PacketPlayOutNamedEntitySpawn = new ClassBuffer("net.minecraft.server." + version + ".PacketPlayOutNamedEntitySpawn");
        _PacketPlayOutEntityHeadRotation = new ClassBuffer("net.minecraft.server." + version + ".PacketPlayOutEntityHeadRotation");
        _SkullMeta = new ClassBuffer("org.bukkit.inventory.meta.SkullMeta");
    }

    public void init() {
        /*Field field = _SkullMeta.getDeclaredField("profile", false);
        field.setAccessible(true);*/

        // Load methods into buffers
        /*
        _CraftServer.getDeclaredMethod("getServer", false);
        _CraftWorld.getDeclaredMethod("getHandle", false);
        _CraftPlayer.getDeclaredMethod("getHandle", false);
        _PlayerConnection.getDeclaredMethod("sendPacket", false, _PacketPlayOutPlayerInfo.getClazz().getInterfaces()[0]);
        _EntityPlayer.getMethod("setLocation", false, double.class, double.class, double.class, float.class, float.class);*/
        //_PacketPlayOutPlayerInfo.getDeclaredMethod("addPlayer", false, _EntityPlayer.getClazz());

        /*_EntityPlayer.getDeclaredField("playerConnection", false);
        _PPOPI_EnumPlayerInfoAction_ADD_PLAYER = Arrays.stream(_PPOPI_EnumPlayerInfoAction.getClazz().getEnumConstants()).filter(o -> o.toString().equals("ADD_PLAYER")).findFirst().orElse(null);

        _GameProfile.getDeclaredConstructor(UUID.class, String.class);
        _EntityPlayer.getDeclaredConstructor(_MinecraftServer.getClazz(), _WorldServer.getClazz(), _GameProfile.getClazz(), _PlayerInteractManager.getClazz());
        _PlayerInteractManager.getDeclaredConstructor(_WorldServer.getClazz());
        _PacketPlayOutPlayerInfo.getDeclaredConstructor(_PPOPI_EnumPlayerInfoAction.getClazz(), Array.newInstance(_EntityPlayer.getClazz(), 0).getClass());
        _PacketPlayOutNamedEntitySpawn.getDeclaredConstructor(_EntityHuman.getClazz());
        _PacketPlayOutEntityHeadRotation.getDeclaredConstructor(_Entity.getClazz(), byte.class);*/
    }

    // STATIC
    public static String getVersion() {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        return version;
    }
}