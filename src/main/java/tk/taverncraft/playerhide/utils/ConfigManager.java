package tk.taverncraft.playerhide.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tk.taverncraft.playerhide.Main;

public class ConfigManager {
    private Main main;

    public ConfigManager(Main main) {
        this.main = main;
    }

    /**
     * Creates config file.
     */
    public void createConfig() {
        FileConfiguration config = getConfig("config.yml");
        main.setConfig(config);
    }

    /**
     * Creates message file.
     */
    public void createMessageFile() {
        String langFileName = main.getConfig().getString("lang-file");

        // default language
        if (langFileName == null) {
            langFileName = "en.yml";
        }

        File langFile = new File(main.getDataFolder() + "/lang", langFileName);
        FileConfiguration lang = new YamlConfiguration();
        if (!langFile.exists()) {
            langFile.getParentFile().mkdirs();
            try {
                Path dest = Paths.get(main.getDataFolder() + "/lang/" + langFileName);
                Files.copy(main.getResource(langFileName), dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            lang.load(langFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        MessageManager.setMessages(lang);
    }

    /**
     * Gets the configuration file with given name.
     *
     * @param configName name of config file
     *
     * @return file configuration for config
     */
    private FileConfiguration getConfig(String configName) {
        File configFile = new File(main.getDataFolder(), configName);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            main.saveResource(configName, false);
        }

        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return config;
    }
}
