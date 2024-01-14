package tk.taverncraft.playerhide.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tk.taverncraft.playerhide.Main;
import tk.taverncraft.playerhide.player.PlayerState;
import tk.taverncraft.playerhide.utils.MessageManager;

/**
 * PlayerUseItemEvent handles the event when a player tries to use an item.
 */
public class PlayerUseItemEvent implements Listener {
    private Main main;

    /**
     * Constructor for PlayerUseItemEvent.
     */
    public PlayerUseItemEvent(Main main) {
        this.main = main;
    }

    @EventHandler
    private void onPlayerUseItem(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
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

        if (!EventHelper.isPlayerHideItem(meta.getDisplayName(), this.main.getConfig().getString(
            "item.name", "&bPlayerHide Stick") + "§g§c§u§v§r§r")) {
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            PlayerState playerState = this.main.getPlayerManager().togglePlayer(player);
            if (playerState != null) {
                MessageManager.sendMessage(player, "toggle-self-success",
                    new String[]{"%player%", "%state%"},
                    new String[]{player.getName(), String.valueOf(playerState)});
            } else {
                MessageManager.sendMessage(player, "toggle-self-fail",
                    new String[]{"%player%"},
                    new String[]{player.getName()});
            }
        }
    }
}
