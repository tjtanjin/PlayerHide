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

        String materialName = this.main.getConfig().getString("item.material", "STICK");
        Material material = Material.valueOf(materialName);
        ItemStack item = new ItemStack(material);
        boolean isEnchanted = this.main.getConfig().getBoolean("item.enchanted", false);

        if (isEnchanted) {
            item.addUnsafeEnchantment(Enchantment.LURE, 1);
        }

        final ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        String displayName = this.main.getConfig().getString("item.name","&bPlayerHide Stick") + "§g§c§u§v§r§r";
        meta.setDisplayName(EventHelper.parseWithColours(displayName));

        List<String> lore = this.main.getConfig().getStringList("item.lore");
        List<String> colouredLore = new ArrayList<>();
        for (String line : lore) {
            colouredLore.add(ChatColor.translateAlternateColorCodes('&', line));
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

        player.getInventory().setItem(this.main.getConfig().getInt("item.slot", 0), item);
    }
}
