package tk.taverncraft.playerhide.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import tk.taverncraft.playerhide.Main;
import tk.taverncraft.playerhide.events.PlayerHopOffEvent;
import tk.taverncraft.playerhide.events.PlayerHopOnEvent;
import tk.taverncraft.playerhide.events.PlayerThrowItemEvent;
import tk.taverncraft.playerhide.events.PlayerUseItemEvent;
import tk.taverncraft.playerhide.utils.MessageManager;
import tk.taverncraft.playerhide.utils.ValidationManager;

/**
 * ReloadCommand contains the execute method for when a user inputs command to reload plugin.
 */
public class ReloadCommand {

    private final String reloadPerm = "phide.reload";
    private Main main;
    private ValidationManager validationManager;

    /**
     * Constructor for ReloadCommand.
     */
    public ReloadCommand(Main main) {
        this.main = main;
        this.validationManager = new ValidationManager(main);
    }

    /**
     * Reloads all files.
     *
     * @param sender user who sent the command
     *
     * @return true at end of execution
     */
    public boolean execute(CommandSender sender) {
        if (!validationManager.hasPermission(reloadPerm, sender)) {
            return true;
        }

        try {
            // reload files
            main.getConfigManager().createConfig();
            main.getConfigManager().createMessageFile();

            // reinitialize manager values
            main.getPlayerManager().initializeValues();

            // re-register events
            main.getEventManager().unregisterEvents();
            main.getEventManager().registerEvents();

            MessageManager.sendMessage(sender, "reload-success");
        } catch (Exception e) {
            main.getLogger().info(e.getMessage());
            MessageManager.sendMessage(sender, "reload-fail");
        }
        return true;
    }
}