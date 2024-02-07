package tk.taverncraft.playerhide.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
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
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }

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

        if (!EventHelper.isPlayerHideItem(meta.getDisplayName(), this.main)) {
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            System.out.println("Action: " + e.getAction());
            System.out.println(e.getAction() == Action.RIGHT_CLICK_AIR);
            System.out.println(e.getAction() == Action.RIGHT_CLICK_BLOCK);
            if (main.getWorldGuardManager() != null && !main.getWorldGuardManager().checkApplyPlayerHide(player)) {
                MessageManager.sendMessage(player, "toggle-self-region-denied");
                return;
            }

            PlayerState playerState = this.main.getPlayerManager().togglePlayer(player);
            if (playerState != null) {
                // 1 tick delay as changing item in hand causes this event to fire again
                // reference: https://www.spigotmc.org/threads/1-19-4-playerinteractevent-called-twice-when-right-clicking-a-block.601044/
                Bukkit.getScheduler().runTaskLater(this.main, () -> {
                    main.getPlayerManager().givePlayerItem(player, false);
                }, 1L);
                MessageManager.sendMessage(player, "toggle-self-success",
                    new String[]{"%player%", "%state%"},
                    new String[]{player.getName(),
                        main.getPlayerManager().getParsedPlayerState(player)});
            } else {
                MessageManager.sendMessage(player, "toggle-self-fail",
                    new String[]{"%player%"},
                    new String[]{player.getName()});
            }
        }
    }
}
