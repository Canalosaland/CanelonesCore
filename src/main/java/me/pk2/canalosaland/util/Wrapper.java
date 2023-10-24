package me.pk2.canalosaland.util;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.config.buff.ConfigLangBuffer;
import me.pk2.canalosaland.config.buff.ConfigMainBuffer;
import me.pk2.canalosaland.dependencies.DependencyLP;
import me.pk2.canalosaland.dependencies.DependencyPAPI;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Wrapper {
    public static String _CONFIG(String path) { return "plugins/CanelonesCore/" + path; }
    public static void _LOG(String prefix, String message) { _LOG("[" + prefix + "] " + message); }
    public static void _LOG(String message) { CanelonesCore.INSTANCE.getLogger().info(message); }
    public static String _COLOR(String text) { return ChatColor.translateAlternateColorCodes('&', text); }
    public static TextComponent _COMP(String text) { return new TextComponent(_COLOR(text)); }
    public static String _UUID(Player player) { return player.getUniqueId().toString().toLowerCase(); }
    public static void _ACTION_BAR(Player player, String text) { player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(_COLOR(text))); }
    public static String _PLACEHOLDER(Player player, String text) {
        return DependencyPAPI.parse(player, text
                .replace("%player%", player.getName())
                .replace("%prefix%", DependencyLP.getPrefix(player))
                .replace("%suffix%", DependencyLP.getSuffix(player))
        );
    }
    public static String _SENDER_LOCALE(CommandSender sender) {
        String locale = "es";
        if(sender instanceof Player player) {
            User user = UserManager.get(player);
            if(user != null)
                locale = user.locale;
        }

        return locale;
    }
    public static String _SENDER_TRANSLATE(CommandSender sender, String key) {
        if(sender instanceof Player player) {
            User user = UserManager.get(player);
            if(user != null)
                return user.translateC(key);
        }

        return ConfigLangBuffer.translateC("es", key);
    }
    public static World _WORLD_OR_DEFAULT(String worldName) { return (worldName == null || Bukkit.getWorld(worldName) == null) ? Bukkit.getWorlds().get(0) : Bukkit.getWorld(worldName); }
    public static void _GLOBAL_MESSAGE(String message) { Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(_COLOR(ConfigMainBuffer.buffer.server_prefix + message))); }
    public static void _GLOBAL_MESSAGE_LOCALE(String locale, Object... args) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if(args != null && args.length > 0)
                p.sendMessage(_COLOR(ConfigMainBuffer.buffer.server_prefix) + String.format(_SENDER_TRANSLATE(p, locale), args));
            else p.sendMessage(_COLOR(ConfigMainBuffer.buffer.server_prefix) + _SENDER_TRANSLATE(p, locale));
        });
    }
    public static void _SOUND_OPEN(Player player) { player.playSound(player.getLocation(), "block.ender_chest.open", 1.0f, 1.2f); }
    public static void _SOUND_CLOSE(Player player) { player.playSound(player.getLocation(), "block.ender_chest.close", 1.0f, 1.2f); }
    public static void _SOUND_CLICK(Player player) { player.playSound(player.getLocation(), "ui.button.click", 1.0f, 1.8f); }
    public static void _SOUND_EXP(Player player) { player.playSound(player.getLocation(), "entity.experience_orb.pickup", 1.0f, 1.5f); }
    public static void _SOUND_EXP2(Player player) { player.playSound(player.getLocation(), "entity.experience_orb.pickup", 1.0f, 2f); }
    public static void _SOUND_PAGE(Player player) { player.playSound(player.getLocation(), "item.book.page_turn", 1f, 1.5f); }
    public static void _SOUND_NOTIFICATION(Player player) { player.playSound(player.getLocation(), "block.note_block.chime", 1f, 1.8f); }
    public static void _SOUND_BOUNTY(Player player) { player.playSound(player.getLocation(), "block.chest.locked", 1f, 1.5f);}
    public static void _SOUND_ERROR(Player player) { player.playSound(player.getLocation(), "block.note_block.bass", 1f, 0f); }
    public static void _SOUND_TELEPORT(Player player) { player.playSound(player.getLocation(), "entity.enderman.teleport", 1f, 1.2f); }

    public static String _HASH(String text) {
        String SALT = "9Y^#h6o8K*j!WpNQw7R@d4g5XvE$bLSCJnFzA2O1Ty0qVxmZtfuDlIicr3GsH+";
        String out = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(SALT.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(byte b : bytes)
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            out = sb.toString();
        } catch (Exception ex) { ex.printStackTrace(); }

        return out;
    }

    public static Scoreboard SCORE = Bukkit.getScoreboardManager().getMainScoreboard();
}