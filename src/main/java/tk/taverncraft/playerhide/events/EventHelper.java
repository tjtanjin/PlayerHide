package tk.taverncraft.playerhide.events;

import org.bukkit.ChatColor;

public class EventHelper {

    public static String parseWithColours(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static boolean isPlayerHideItem(String itemName, String displayName) {
        String unformattedItemName = ChatColor.stripColor(itemName);
        String unformattedDisplayName = ChatColor.stripColor(parseWithColours(displayName));

        return unformattedItemName.equalsIgnoreCase(unformattedDisplayName);
    }
}
