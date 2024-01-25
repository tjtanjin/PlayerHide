package tk.taverncraft.playerhide.worldguard;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.sk89q.worldguard.session.SessionManager;

import tk.taverncraft.playerhide.Main;

/**
 * Manages the creation of the worldguard flag and initialization of handler.
 */
public class WorldGuardManager {
    public final static StateFlag APPLY_PLAYERHIDE = new StateFlag("apply-playerhide", true);

    private final Main main;
    private SessionManager sessionManager;

    /**
     * Constructor for WorldGuardManager.
     */
    public WorldGuardManager(Main main) {
        this.main = main;
        FlagRegistry flagRegistry = WorldGuard.getInstance().getFlagRegistry();
        flagRegistry.register(APPLY_PLAYERHIDE);
    }

    /**
     * Initializers handler for worldguard region.
     */
    public void initializeHandler() {
        this.sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
        sessionManager.registerHandler(WorldGuardRegionHandler.FACTORY(main), null);
    }

    /**
     * Checks if playerhide should be applied to a player within a set of regions
     *
     * @param entered region that player entered
     */
    public boolean getApplyPlayerHide(Set<ProtectedRegion> entered) {
        boolean applyPlayerHide = true;

        for (ProtectedRegion region : entered) {
            if (region.getFlag(WorldGuardManager.APPLY_PLAYERHIDE) == null) {
                continue;
            }

            if (region.getFlag(WorldGuardManager.APPLY_PLAYERHIDE) == StateFlag.State.DENY) {
                applyPlayerHide = false;
                break;
            }
        }

        return applyPlayerHide;
    }

    /**
     * Checks if playerhide can be applied to player.
     *
     * @param player player to check
     */
    public boolean checkApplyPlayerHide(Player player) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        if (sessionManager.hasBypass(localPlayer, localPlayer.getWorld())) {
            return true;
        }

        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = regionContainer.createQuery();
        Set<ProtectedRegion> regions = new HashSet<>(query.getApplicableRegions(localPlayer.getLocation()).getRegions());
        regions.add(regionContainer.get(localPlayer.getWorld()).getRegion("__global__"));
        return getApplyPlayerHide(regions);
    }
}
