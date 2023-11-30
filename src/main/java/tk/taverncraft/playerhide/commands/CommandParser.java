package tk.taverncraft.playerhide.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import tk.taverncraft.playerhide.Main;

/**
 * CommandParser contains the onCommand method that handles user command input.
 */
public class CommandParser implements CommandExecutor {
    Main main;

    /**
     * Constructor for CommandParser.
     */
    public CommandParser(Main main) {
        this.main = main;
    }

    /**
     * Entry point of commands.
     */
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("playerhide")) {

            // if no arguments provided or is null, return invalid command
            if (args.length == 0) {
                return new InvalidCommand().execute(sender);
            }

            final String chatCmd = args[0];

            if (chatCmd == null) {
                return new InvalidCommand().execute(sender);
            }

            // command to toggle visibility
            if (chatCmd.equals("toggle")) {
                if (args.length == 1) {
                    return new ToggleCommand(this.main).execute(sender);
                } else {
                    return new ToggleCommand(this.main).execute(sender, args);
                }
            }

            // command to view all commands
            if (chatCmd.equals("help")) {
                return new HelpCommand(this.main).execute(sender, args);
            }

            // command to reload plugin
            if (chatCmd.equals("reload")) {
                return new ReloadCommand(this.main).execute(sender);
            }

            return new InvalidCommand().execute(sender);
        }
        return true;
    }

}

