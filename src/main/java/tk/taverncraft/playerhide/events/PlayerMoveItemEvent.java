package tk.taverncraft.playerhide.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tk.taverncraft.playerhide.Main;

/**
 * PlayerMoveItemEvent handles the event when a player tries to move the playerhide item.
 */
public class PlayerMoveItemEvent implements Listener {
    private Main main;

    /**
     * Constructor for PlayerMoveItemEvent.
     */
    public PlayerMoveItemEvent(Main main) {
        this.main = main;
    }

    @EventHandler
    private void onPlayerMoveItem(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null) {
            return;
        }

        if (!item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        if (!meta.hasDisplayName()) {
            return;
        }

        // if not item, return
        if (!EventHelper.isPlayerHideItem(meta.getDisplayName(), this.main)) {
            return;
        }

        // if allowed to move the item, return
        if (main.getConfig().getBoolean("allow-move", false)) {
            return;
        }

        // cancel events that move items
        InventoryAction action = e.getAction();
        if (checkInventoryEvent(action, e)) {
            e.setCancelled(true);
        }
    }

    /**
     * Checks if an inventory click event has to be cancelled.
     *
     * @param action inventory action from user
     * @param e inventory click event
     *
     * @return true if event has to be cancelled, false otherwise
     */
    private boolean checkInventoryEvent(InventoryAction action, InventoryClickEvent e) {
        return (action == InventoryAction.PICKUP_ONE
            || action == InventoryAction.PICKUP_SOME || action == InventoryAction.PICKUP_HALF
            || action == InventoryAction.PICKUP_ALL
            || action == InventoryAction.MOVE_TO_OTHER_INVENTORY
            || action == InventoryAction.CLONE_STACK || action == InventoryAction.HOTBAR_SWAP
            || action == InventoryAction.SWAP_WITH_CURSOR) || e.isShiftClick();
    }

}
