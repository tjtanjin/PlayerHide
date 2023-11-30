package tk.taverncraft.playerhide.commands;

import org.bukkit.command.CommandSender;

import tk.taverncraft.playerhide.Main;
import tk.taverncraft.playerhide.utils.MessageManager;
import tk.taverncraft.playerhide.utils.ValidationManager;

/**
 * HelpCommand contains the execute method for when a user inputs the command to get help for the plugin.
 */
public class HelpCommand {

    private final String helpPerm = "phide.help";
    private ValidationManager validationManager;

    /**
     * Constructor for HelpCommand.
     */
    public HelpCommand(Main main) {
        this.validationManager = new ValidationManager(main);
    }

    /**
     * Shows a list of commands to the user.
     *
     * @param sender user who sent the command
     *
     * @return true at end of execution
     */
    public boolean execute(CommandSender sender, String[] args) {
        if (!validationManager.hasPermission(helpPerm, sender)) {
            return true;
        }

        try {
            int pageNum = Integer.parseInt(args[1]);
            MessageManager.showHelpBoard(sender, pageNum);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            MessageManager.showHelpBoard(sender, 1);
        }
        return true;
    }
}

