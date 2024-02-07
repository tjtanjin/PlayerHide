package tk.taverncraft.playerhide.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import tk.taverncraft.playerhide.Main;
import tk.taverncraft.playerhide.utils.MessageManager;
import tk.taverncraft.playerhide.utils.StringUtils;

/**
 * In charge of handling players affected by player hide.
 */
public class PlayerManager {
    private final String exemptPerm = "phide.exempt";
    private Main main;
    private HashMap<UUID, PlayerState> playerStates = new HashMap<>();
    private List<UUID> exemptedPlayers = new ArrayList<>();
    private PlayerState defaultPlayerState;
    private String particles = "NONE";
    private BukkitTask spawnParticlesTask = null;
    private int particleCount = 25;

    /**
     * Constructor for PlayerManager.
     */
    public PlayerManager(Main main) {
        this.main = main;
        initializeValues();
    }

    /**
     * Initializations for PlayerManager.
     */
    public void initializeValues() {
        this.particleCount = this.main.getConfig().getInt("particle-count", 25);
        if (spawnParticlesTask != null) {
            spawnParticlesTask.cancel();
        }
        boolean hideOnJoin = main.getConfig().getBoolean("hide-on-join", false);
        if (hideOnJoin) {
            this.defaultPlayerState = PlayerState.HIDDEN;
        } else {
            this.defaultPlayerState = PlayerState.VISIBLE;
        }
        this.particles = main.getConfig().getString("particles", "NONE");

        if (!this.particles.equals("NONE")) {
            try {
                Particle particle = Particle.valueOf(this.particles);
                runSpawnParticlesTask(particle);
            } catch (Exception ex) {
                this.main.getLogger().info(ex.getMessage());
            }
        }
    }

    /**
     * Removes a player when the player leaves.
     *
     * @param player player who left
     */
    public void removePlayer(Player player) {
        if (player == null) {
            return;
        }

        this.playerStates.remove(player.getUniqueId());
    }

    /**
     * Toggles a player's visibility state.
     *
     * @param player player to toggle for
     */
    public PlayerState togglePlayer(Player player) {
        if (player == null) {
            return null;
        }

        UUID uuid = player.getUniqueId();
        PlayerState playerPlayerState = playerStates.get(uuid);
        if (playerPlayerState == null) {
            this.playerStates.put(uuid, this.defaultPlayerState);
            if (this.defaultPlayerState == PlayerState.HIDDEN) {
                hidePlayers(player);
            } else {
                showPlayers(player);
            }
            return this.defaultPlayerState;
        }

        if (playerPlayerState == PlayerState.HIDDEN) {
            showPlayers(player);
            playerStates.put(uuid, PlayerState.VISIBLE);
            return PlayerState.VISIBLE;
        } else {
            hidePlayers(player);
            playerStates.put(uuid, PlayerState.HIDDEN);
            return PlayerState.HIDDEN;
        }
    }

    /**
     * Called on player joins to ensure they are hidden for existing players with
     * visibility state set to HIDDEN and that are not exempted.
     *
     * @param player player who just joined
     */
    public void hidePlayerForThoseInHiddenState(Player player) {
        for (Map.Entry<UUID, PlayerState> set : this.playerStates.entrySet()) {
            Player p = Bukkit.getPlayer(set.getKey());
            PlayerState playerState = set.getValue();
            if (playerState == PlayerState.HIDDEN && !exemptedPlayers.contains(p.getUniqueId())
                    && !player.hasPermission(exemptPerm)) {
                p.hidePlayer(player);
            } else {
                p.showPlayer(player);
            }
        }
    }

    /**
     * Hides all players from the player toggled.
     *
     * @param player player who toggled to HIDDEN state
     */
    public void hidePlayers(Player player) {
        for (Map.Entry<UUID, PlayerState> set : this.playerStates.entrySet()) {
            Player p = Bukkit.getPlayer(set.getKey());
            if (player != p && !p.hasPermission(exemptPerm)) {
                player.hidePlayer(p);
            }
        }
    }

    /**
     * Shows all players from the player toggled.
     *
     * @param player player who toggled to VISIBLE state
     */
    public void showPlayers(Player player) {
        for (Map.Entry<UUID, PlayerState> set : this.playerStates.entrySet()) {
            Player p = Bukkit.getPlayer(set.getKey());
            if (player != p) {
                player.showPlayer(p);
            }
        }
    }

    /**
     * BukkitTask for spawning particles.
     *
     * @param particle particle to spawn
     */
    public void runSpawnParticlesTask(Particle particle) {
        this.spawnParticlesTask = new BukkitRunnable() {
            @Override
            public void run() {
                spawnParticles(particle);
            }

        }.runTaskTimerAsynchronously(main, 5, 5);
    }

    /**
     * Logic for particle spawning.
     *
     * @param particle particle to spawn
     */
    public void spawnParticles(Particle particle) {
        for (Map.Entry<UUID, PlayerState> set1 : this.playerStates.entrySet()) {
            Player pSelf = Bukkit.getPlayer(set1.getKey());
            if (pSelf == null) {
                return;
            }
            if (this.playerStates.get(pSelf.getUniqueId()) == PlayerState.VISIBLE
                    || exemptedPlayers.contains(pSelf.getUniqueId())) {
                continue;
            }
            for (Map.Entry<UUID, PlayerState> set2 : this.playerStates.entrySet()) {
                Player pOther = Bukkit.getPlayer(set2.getKey());
                if (pOther == null) {
                    return;
                }
                if (pSelf.equals(pOther)) {
                    continue;
                }
                Location location = pOther.getLocation();
                for (int i = 0; i < this.particleCount; i++) {
                    location.add(0,0.7, 0);
                    pSelf.spawnParticle(particle, location,1, 0.1, 0.2, 0.1, 0);
                    location.subtract(0,0.7, 0);
                }
            }
        }
    }

    /**
     * Retrieves the parsed player state for showing to players.
     *
     * @param player player to get parsed state for
     */
    public String getParsedPlayerState(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerState playerState = playerStates.get(uuid);
        if (playerState == PlayerState.HIDDEN) {
            return Optional.ofNullable(
                MessageManager.getPlainMessage("hidden-state")).orElse("HIDDEN");
        } else if (playerState == PlayerState.VISIBLE) {
            return Optional.ofNullable(
                MessageManager.getPlainMessage("visible-state")).orElse("VISIBLE");
        } else {
            return null;
        }
    }

    /**
     * Handles player exemption from worldguard region flag.
     *
     * @param uuid uuid of player to handle exemption for
     * @param applyPlayerHide whether to apply or not to apply playerhide (exempt the player)
     */
    public void handlePlayerExemption(UUID uuid, boolean applyPlayerHide) {
        Player player = Bukkit.getPlayer(uuid);
        if (applyPlayerHide) {
            exemptedPlayers.remove(uuid);
            PlayerState currentPlayerState = playerStates.get(uuid);
            if (currentPlayerState == PlayerState.HIDDEN) {
                hidePlayers(player);
                return;
            }
        } else {
            exemptedPlayers.add(uuid);
        }
        showPlayers(player);
    }

    /**
     * Removes a player from exemption.
     *
     * @param player player to remove from exemption
     */
    public void removePlayerFromExemption(Player player) {
        exemptedPlayers.remove(player.getUniqueId());
    }

    /**
     * Gives the playerhide item to specified player.
     *
     * @param player player to give playerhide item to
     * @param isJoin boolean indicating if player just joined
     */
    public void givePlayerItem(Player player, boolean isJoin) {
        boolean hasItemToggledOnDisabled = main.getConfig().getBoolean("item-toggled-on.disabled", false);
        if (!isJoin && hasItemToggledOnDisabled) {
            return;
        }

        String prefix = "item";
        PlayerState playerState = playerStates.get(player.getUniqueId());
        if (playerState == PlayerState.HIDDEN && !hasItemToggledOnDisabled) {
            prefix = "item-toggled-on";
        }

        String materialName = this.main.getConfig().getString(prefix + ".material", "STICK");
        Material material = Material.valueOf(materialName);
        ItemStack item = new ItemStack(material);
        boolean isEnchanted = this.main.getConfig().getBoolean(prefix + ".enchanted", false);

        if (isEnchanted) {
            item.addUnsafeEnchantment(Enchantment.LURE, 1);
        }

        final ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        String displayName = this.main.getConfig().getString(prefix + ".name","&bPlayerHide Stick") + "§g§c§u§v§r§r";
        meta.setDisplayName(StringUtils.formatStringColor(displayName));

        List<String> lore = this.main.getConfig().getStringList(prefix + ".lore");
        List<String> colouredLore = new ArrayList<>();
        for (String line : lore) {
            colouredLore.add(StringUtils.formatStringColor(line));
        }

        if (colouredLore.size() != 0) {
            meta.setLore(colouredLore);
        }

        if (isEnchanted) {
            try {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } catch (Exception ex) {
                this.main.getLogger().info(ex.getMessage());
            }
        }

        item.setItemMeta(meta);

        player.getInventory().setItem(this.main.getConfig().getInt(prefix + ".slot", 0), item);
    }
}

