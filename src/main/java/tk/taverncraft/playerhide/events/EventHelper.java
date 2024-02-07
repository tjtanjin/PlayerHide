package tk.taverncraft.playerhide.events;

import org.bukkit.ChatColor;

import tk.taverncraft.playerhide.Main;
import tk.taverncraft.playerhide.utils.StringUtils;

public class EventHelper {
    public static boolean isPlayerHideItem(String itemName, Main main) {
        String itemType1 = main.getConfig().getString("item.name", "&bPlayerHide Stick") + "§g§c§u§v§r§r";
        String itemType2 = main.getConfig().getString("item-toggled-on.name", "&bPlayerHide Stick") + "§g§c§u§v§r§r";
        String unformattedItemName = ChatColor.stripColor(itemName);
        String unformattedDisplayName1 = ChatColor.stripColor(StringUtils.formatStringColor(itemType1));
        String unformattedDisplayName2 = ChatColor.stripColor(StringUtils.formatStringColor(itemType2));

        return unformattedItemName.equalsIgnoreCase(unformattedDisplayName1)
            || unformattedItemName.equalsIgnoreCase(unformattedDisplayName2);
    }
}
