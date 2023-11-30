package tk.taverncraft.playerhide.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tk.taverncraft.playerhide.Main;

/**
 * PlayerThrowItemEvent handles the event when a player tries to throw an item.
 */
public class PlayerThrowItemEvent implements Listener {
    private Main main;

    /**
     * Constructor for PlayerThrowItemEvent.
     */
    public PlayerThrowItemEvent(Main main) {
        this.main = main;
    }

    @EventHandler
    private void onPlayerThrowItem(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        if (!item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName()) {
            return;
        }

        if (!EventHelper.isPlayerHideItem(meta.getDisplayName(), this.main.getConfig().getString(
            "item.name" + "§g§c§u§v§r§r", "&bPlayerHide Stick"))) {
            return;
        }

        e.setCancelled(!this.main.getConfig().getBoolean("allow-drop"));
    }
}
