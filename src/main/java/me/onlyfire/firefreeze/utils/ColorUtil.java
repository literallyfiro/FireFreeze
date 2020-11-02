package me.onlyfire.firefreeze.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtil {

    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String colorize(String text) {
        String toReturn = "";

        // For HEX Colors
        if (Bukkit.getVersion().contains("1.16")) {
            Matcher match = pattern.matcher(text);
            while (match.find()) {
                String newText = text.substring(match.start(), match.end());
                toReturn = text.replace(newText, ChatColor.of(newText) + "");
                match = pattern.matcher(text);
            }
        }
        //Normal colorize
        return ChatColor.translateAlternateColorCodes('&', toReturn);
    }

    public static String colorizePAPI(Player player, String text) {
        String toReturn = "";

        // For HEX Colors
        if (Bukkit.getVersion().contains("1.16")) {
            Matcher match = pattern.matcher(text);
            while (match.find()) {
                String newText = text.substring(match.start(), match.end());
                toReturn = text.replace(newText, ChatColor.of(newText) + "");
                match = pattern.matcher(text);
            }
        }
        //Normal colorize
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            toReturn = PlaceholderAPI.setPlaceholders(player, text);
        }
        return ChatColor.translateAlternateColorCodes('&', toReturn);
    }

}
