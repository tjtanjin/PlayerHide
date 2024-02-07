package tk.taverncraft.playerhide.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tk.taverncraft.playerhide.Main;
import tk.taverncraft.playerhide.player.PlayerManager;
import tk.taverncraft.playerhide.utils.StringUtils;

/**
 * PlayerHopOnEvent handles the event when a player comes online.
 */
public class PlayerHopOnEvent implements Listener {
    private Main main;

    /**
     * Constructor for PlayerHopOnEvent.
     */
    public PlayerHopOnEvent(Main main) {
        this.main = main;
    }

    @EventHandler
    private void onPlayerJoinServer(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerManager playerManager = this.main.getPlayerManager();
        if (main.getWorldGuardManager() == null || main.getWorldGuardManager().checkApplyPlayerHide(player)) {
            playerManager.togglePlayer(player);
        }

        playerManager.hidePlayerForThoseInHiddenState(player);

        boolean giveItem = this.main.getConfig().getBoolean("item-on-join");
        if (!giveItem) {
            return;
        }

        main.getPlayerManager().givePlayerItem(player, true);
    }
}
