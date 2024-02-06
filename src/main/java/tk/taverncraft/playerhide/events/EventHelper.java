package tk.taverncraft.playerhide.events;

import org.bukkit.ChatColor;

import tk.taverncraft.playerhide.utils.StringUtils;

public class EventHelper {
    public static boolean isPlayerHideItem(String itemName, String displayName) {
        String unformattedItemName = ChatColor.stripColor(itemName);
        String unformattedDisplayName = ChatColor.stripColor(StringUtils.formatStringColor(displayName));

        return unformattedItemName.equalsIgnoreCase(unformattedDisplayName);
    }
}
