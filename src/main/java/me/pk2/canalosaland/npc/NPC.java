package me.pk2.canalosaland.npc;

import static me.pk2.canalosaland.CanelonesCore.INSTANCE;
import static me.pk2.canalosaland.util.Wrapper.*;

import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class NPC {
    private String name;
    private Location loc;
    private Object npc;
    public NPC(String name, Location loc) {
        this.name = _COLOR(name);
        if(this.name.length() > 16)
            this.name = this.name.substring(0, 16);

        this.loc = loc;
    }

    public void make() throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Object server, world;
        GameProfile gameProfile;

        Object craftServer = INSTANCE.reflectionsManager._CraftServer.getClazz().cast(Bukkit.getServer()); // CraftServer instance
        server = INSTANCE.reflectionsManager._CraftServer.getDeclaredMethod("getServer", false).invoke(craftServer); // MinecraftServer instance
        Object craftWorld = INSTANCE.reflectionsManager._CraftWorld.getClazz().cast(loc.getWorld()); // CraftWorld instance
        world = INSTANCE.reflectionsManager._CraftWorld.getDeclaredMethod("getHandle", false).invoke(craftWorld); // WorldServer instance
        gameProfile = new GameProfile(UUID.randomUUID(), name); // GameProfile instance
        Object playerInteractManager = INSTANCE.reflectionsManager._PlayerInteractManager.getDeclaredConstructor(world.getClass()).newInstance(world); // PlayerInteractManager instance

        npc = INSTANCE.reflectionsManager._EntityPlayer
                .getDeclaredConstructor(server.getClass(), world.getClass(), gameProfile.getClass(), playerInteractManager.getClass())
                .newInstance(server, world, gameProfile, playerInteractManager); // EntityPlayer instance
        INSTANCE.reflectionsManager._EntityPlayer.getMethod("setLocation", false, double.class, double.class, double.class, float.class, float.class)
                .invoke(npc, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()); // Set location
    }

    public void spawn(Player player) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Object craftPlayer = INSTANCE.reflectionsManager._CraftPlayer.getClazz().cast(player); // CraftPlayer instance
        Object entityPlayer = INSTANCE.reflectionsManager._CraftPlayer.getDeclaredMethod("getHandle", false).invoke(craftPlayer); // EntityPlayer instance
        Object entityHuman = INSTANCE.reflectionsManager._EntityHuman.getClazz().cast(npc); // EntityHuman instance
        Object entity = INSTANCE.reflectionsManager._Entity.getClazz().cast(npc); // Entity instance
        Object connection = INSTANCE.reflectionsManager._EntityPlayer.getDeclaredField("playerConnection", false).get(entityPlayer); // PlayerConnection instance
        Object actionAddPlayer = INSTANCE.reflectionsManager._PPOPI_EnumPlayerInfoAction_ADD_PLAYER; // EnumPlayerInfoAction.ADD_PLAYER
        Object arrPlayers = Array.newInstance(entityPlayer.getClass(), 1); // EntityPlayer[] instance
        Array.set(arrPlayers, 0, npc); // Set EntityPlayer instance to EntityPlayer[] instance
        Object packetPlayOutPlayerInfo = INSTANCE.reflectionsManager._PacketPlayOutPlayerInfo
                .getDeclaredConstructor(INSTANCE.reflectionsManager._PPOPI_EnumPlayerInfoAction.getClazz(), arrPlayers.getClass())
                .newInstance(actionAddPlayer, arrPlayers); // PacketPlayOutPlayerInfo instance
        Object packetPlayOutNamedEntitySpawn = INSTANCE.reflectionsManager._PacketPlayOutNamedEntitySpawn
                .getDeclaredConstructor(INSTANCE.reflectionsManager._EntityHuman.getClazz())
                .newInstance(entityHuman); // PacketPlayOutNamedEntitySpawn instance
        Object packetPlayOutEntityHeadRotation = INSTANCE.reflectionsManager._PacketPlayOutEntityHeadRotation
                .getDeclaredConstructor(INSTANCE.reflectionsManager._Entity.getClazz(), byte.class)
                .newInstance(entity, (byte) (loc.getYaw() * 256 / 360)); // PacketPlayOutEntityHeadRotation instance

        // Send all packets finally!!!!
        INSTANCE.reflectionsManager._PlayerConnection
                .getDeclaredMethod("sendPacket", false, packetPlayOutPlayerInfo.getClass().getInterfaces()[0])
                .invoke(connection, packetPlayOutPlayerInfo); // Send PacketPlayOutPlayerInfo packet
        INSTANCE.reflectionsManager._PlayerConnection
                .getDeclaredMethod("sendPacket", false, packetPlayOutNamedEntitySpawn.getClass().getInterfaces()[0])
                .invoke(connection, packetPlayOutNamedEntitySpawn); // Send PacketPlayOutNamedEntitySpawn packet
        INSTANCE.reflectionsManager._PlayerConnection
                .getDeclaredMethod("sendPacket", false, packetPlayOutEntityHeadRotation.getClass().getInterfaces()[0])
                .invoke(connection, packetPlayOutEntityHeadRotation); // Send PacketPlayOutEntityHeadRotation packet
    }
}