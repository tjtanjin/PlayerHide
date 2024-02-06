package tk.taverncraft.playerhide.utils;

import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import tk.taverncraft.playerhide.Main;

/**
 * An expansion class for PAPI.
 */
public class PapiManager extends PlaceholderExpansion {

    private final Main main;

    public PapiManager(Main main) {
        this.main = main;
    }

    @Override
    public String getAuthor() {
        return "tjtanjin - FrozenFever";
    }

    @Override
    public String getIdentifier() {
        return "phide";
    }

    @Override
    public String getVersion() {
        return main.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("player_status")) {
            if (player.getPlayer().isOnline()) {
                return main.getPlayerManager().getParsedPlayerState(player.getPlayer());
            }
            return null;
        }

        return null;
    }
}
