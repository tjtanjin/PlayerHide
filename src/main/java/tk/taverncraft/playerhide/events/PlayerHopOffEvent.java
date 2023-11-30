package tk.taverncraft.playerhide.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import tk.taverncraft.playerhide.Main;

/**
 * PlayerHopOffEvent handles the event when a player goes offline.
 */
public class PlayerHopOffEvent implements Listener {
    private Main main;

    /**
     * Constructor for PlayerHopOffEvent.
     */
    public PlayerHopOffEvent(Main main) {
        this.main = main;
    }

    @EventHandler
    private void onPlayerLeaveServer(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        this.main.getPlayerManager().removePlayer(player);
    }
}
