package tk.taverncraft.playerhide.utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    /**
     * Formats color in chat messages.
     *
     * @param message message to format
     */
    public static String formatStringColor(String message) {
        Pattern pattern = Pattern.compile("(?<!\\\\)#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.substring(0, matcher.start()) + builder.toString() + message.substring(matcher.end());
            matcher = pattern.matcher(message);
        }

        message = message.replace("\\#", "#");
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
