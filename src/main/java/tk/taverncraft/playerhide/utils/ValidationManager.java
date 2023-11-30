package tk.taverncraft.playerhide.utils;

import org.bukkit.command.CommandSender;

import tk.taverncraft.playerhide.Main;

/**
 * Validator performs common validations that are required as necessary.
 */
public class ValidationManager {
    private Main main;

    /**
     * Constructor for Validator.
     */
    public ValidationManager(Main main) {
        this.main = main;
    }

    /**
     * Validates if sender has permission and sends a message if not.
     *
     * @param permission permission node to check for
     * @param sender the player executing the command
     */
    public boolean hasPermission(String permission, CommandSender sender) {
        if (sender.hasPermission(permission)) {
            return true;
        }
        MessageManager.sendMessage(sender, "no-permission");
        return false;
    }

    /**
     * Validates if inputted player exist and sends a message if not.
     *
     * @param name the name of the player to check for
     * @param sender the player executing the command
     */
    public boolean playerExist(String name, CommandSender sender) {
        if (name.length() > 16 || this.main.getServer().getOfflinePlayer(name).getFirstPlayed() == 0L) {
            MessageManager.sendMessage(sender, "player-not-exist",
                new String[]{"%player%"},
                new String[]{name});
            return false;
        }
        return true;
    }
}
