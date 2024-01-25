package tk.taverncraft.playerhide.worldguard;

import java.util.HashSet;
import java.util.Set;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;

import tk.taverncraft.playerhide.Main;

/**
 * Handles worldguard region when players enter or exit. Implementation referenced from:
 * https://github.com/aromaa/WorldGuardExtraFlags/blob/master/WG/src/main/java/net/goldtreeservers/worldguardextraflags/wg/handlers/CommandOnExitFlagHandler.java
 */
public class WorldGuardRegionHandler extends Handler {
    private final Main main;

    public static final Factory FACTORY(Main main) {
        return new Factory(main);
    }

    public static class Factory extends Handler.Factory<WorldGuardRegionHandler> {
        private final Main main;
        public Factory(Main main) {
            this.main = main;
        }

        @Override
        public WorldGuardRegionHandler create(Session session) {
            return new WorldGuardRegionHandler(main, session);
        }
    }

    protected WorldGuardRegionHandler(Main main, Session session) {
        super(session);
        this.main = main;
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer localPlayer, Location from, Location to,
            ApplicableRegionSet toSet, Set<ProtectedRegion> entered,
            Set<ProtectedRegion> exited, MoveType moveType) {

        // nothing to do if region not changed
        if (exited.size() == 0 && entered.size() == 0) {
            return true;
        }

        // if player can bypass, then we do nothing
        if (this.getSession().getManager().hasBypass(localPlayer, (World) to.getExtent())) {
            return true;
        }

        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        entered = new HashSet<>(entered);
        entered.add(regionContainer.get(localPlayer.getWorld()).getRegion("__global__"));

        boolean applyPlayerHide = main.getWorldGuardManager().getApplyPlayerHide(entered);
        main.getPlayerManager().handlePlayerExemption(localPlayer.getUniqueId(), applyPlayerHide);
        return true;
    }
}
