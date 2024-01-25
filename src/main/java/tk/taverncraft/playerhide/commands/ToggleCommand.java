package tk.taverncraft.playerhide.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.taverncraft.playerhide.Main;
import tk.taverncraft.playerhide.player.PlayerState;
import tk.taverncraft.playerhide.utils.MessageManager;
import tk.taverncraft.playerhide.utils.ValidationManager;

/**
 * ToggleCommand contains the execute method for when points are given to a user.
 */
public class ToggleCommand {

    private final String toggleSelfPerm = "phide.toggle.self";
    private final String toggleOthersPerm = "phide.toggle.others";
    private Main main;
    private ValidationManager validationManager;

    /**
     * Constructor for ToggleCommand.
     */
    public ToggleCommand(Main main) {
        this.main = main;
        this.validationManager = new ValidationManager(main);
    }

    /**
     * Toggles player hide for self.
     *
     * @param sender user who sent the command
     *
     * @return true at end of execution
     */
    public boolean execute(CommandSender sender) {
        if (!(sender instanceof Player)) {
            MessageManager.sendMessage(sender, "player-only-command");
            return true;
        }

        if (!validationManager.hasPermission(toggleSelfPerm, sender)) {
            return true;
        }

        if (main.getWorldGuardManager() != null && !main.getWorldGuardManager().checkApplyPlayerHide((Player) sender)) {
            MessageManager.sendMessage(sender, "toggle-self-region-denied");
            return true;
        }

        PlayerState playerState = this.main.getPlayerManager().togglePlayer((Player) sender);
        if (playerState != null) {
            MessageManager.sendMessage(sender, "toggle-self-success",
                new String[]{"%player%", "%state%"},
                new String[]{sender.getName(),
                    main.getPlayerManager().getParsedPlayerState((Player) sender)});
        } else {
            MessageManager.sendMessage(sender, "toggle-self-fail",
                new String[]{"%player%"},
                new String[]{sender.getName()});
        }
        return true;
    }

    /**
     * Toggles player hide for others.
     *
     * @param sender user who sent the command
     *
     * @return true at end of execution
     */
    public boolean execute(CommandSender sender, String[] args) {
        if (!validationManager.hasPermission(toggleOthersPerm, sender)) {
            return true;
        }

        if (!this.validationManager.playerExist(args[1], sender)) {
            return true;
        }

        String playerName = args[1];

        if (main.getWorldGuardManager() != null && !main.getWorldGuardManager().checkApplyPlayerHide(Bukkit.getPlayer(playerName))) {
            MessageManager.sendMessage(sender, "toggle-others-region-denied",
                new String[]{"%player%"},
                new String[]{playerName});
            return true;
        }

        PlayerState playerState = this.main.getPlayerManager().togglePlayer(Bukkit.getPlayer(playerName));
        if (playerState != null) {
            MessageManager.sendMessage(sender, "toggle-others-success",
                new String[]{"%player%", "%state%"},
                new String[]{playerName,
                    main.getPlayerManager().getParsedPlayerState(Bukkit.getPlayer(playerName))});
        } else {
            MessageManager.sendMessage(sender, "toggle-others-fail",
                new String[]{"%player%"},
                new String[]{playerName});
        }

        return true;
    }
}

