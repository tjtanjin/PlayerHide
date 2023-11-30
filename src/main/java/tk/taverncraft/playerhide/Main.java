package tk.taverncraft.playerhide;

import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import tk.taverncraft.playerhide.commands.CommandParser;
import tk.taverncraft.playerhide.commands.CommandTabCompleter;
import tk.taverncraft.playerhide.events.PlayerHopOffEvent;
import tk.taverncraft.playerhide.events.PlayerHopOnEvent;
import tk.taverncraft.playerhide.events.PlayerThrowItemEvent;
import tk.taverncraft.playerhide.events.PlayerUseItemEvent;
import tk.taverncraft.playerhide.player.PlayerManager;
import tk.taverncraft.playerhide.utils.ConfigManager;
import tk.taverncraft.playerhide.utils.UpdateChecker;

/**
 * The plugin class.
 */
public class Main extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");

    // Config
    private FileConfiguration config;

    // managers
    private ConfigManager configManager;
    private PlayerManager playerManager;

    @Override
    public void onDisable() {
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    @Override
    public void onEnable() {
        new UpdateChecker(this, 105677).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                getLogger().info("You are using the latest version of PlayerHide!");
            } else {
                getLogger().info("A new version of PlayerHide is now available on spigot!");
            }
        });

        this.configManager = new ConfigManager(this);

        // config setup
        configManager.createConfig();
        configManager.createMessageFile();

        //this.createScheduleConfig();
        this.getCommand("phide").setTabCompleter(new CommandTabCompleter());
        this.getCommand("phide").setExecutor(new CommandParser(this));

        try {
            this.playerManager = new PlayerManager(this);
        } catch (NullPointerException e) {
            this.getLogger().info(e.getMessage());
            this.getLogger().info("[Playerhide] Is your config.yml " +
                "updated/set up correctly?");
            getServer().getPluginManager().disablePlugin(this);
        }

        this.getServer().getPluginManager().registerEvents(new PlayerHopOnEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerHopOffEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerThrowItemEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerUseItemEvent(this), this);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void setConfig(FileConfiguration config) {
        this.config = config;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }
}