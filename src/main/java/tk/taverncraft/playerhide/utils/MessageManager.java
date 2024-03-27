package tk.taverncraft.playerhide.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * MessageManager handles all formatting and sending of messages to the command sender.
 */
public class MessageManager {
    private static final HashMap<String, String> messageKeysMap = new HashMap<>();
    private static ArrayList<String> completeHelpBoard;

    /**
     * Sets the messages to use.
     *
     * @param lang the configuration to base the messages on
     */
    public static void setMessages(FileConfiguration lang) {
        Set<String> messageKeysSet = lang.getConfigurationSection("").getKeys(false);

        for (String messageKey : messageKeysSet) {
            messageKeysMap.put(messageKey, StringUtils.formatStringColor(lang.get(messageKey).toString()));
        }

        setUpHelpBoard();
    }

    /**
     * Sends message to the sender.
     *
     * @param sender sender to send message to
     * @param messageKey key to get message with
     */
    public static void sendMessage(CommandSender sender, String messageKey) {
        String message = getPrefixedMessage(messageKey);
        sender.sendMessage(message);
    }

    /**
     * Sends message to the sender, replacing placeholders.
     *
     * @param sender sender to send message to
     * @param messageKey key to get message with
     * @param keys placeholder keys
     * @param values placeholder values
     */
    public static void sendMessage(CommandSender sender, String messageKey, String[] keys, String[] values) {
        String message = getPrefixedMessage(messageKey);
        for (int i = 0; i < keys.length; i++) {
            message = message.replaceAll(keys[i], values[i]);
        }
        sender.sendMessage(message);
    }

    /**
     * Retrieves plain message value given the message key.
     *
     * @param messageKey key to retrieve message with
     */
    public static String getPlainMessage(String messageKey) {
        return messageKeysMap.get(messageKey);
    }

    /**
     * Retrieves message value given the message key and appends plugin prefix.
     *
     * @param messageKey key to retrieve message with
     */
    public static String getPrefixedMessage(String messageKey) {
        String prefix = messageKeysMap.get("prefix");
        return prefix.substring(0, prefix.length() - 1) + messageKeysMap.get(messageKey);
    }

    /**
     * Sets up the help board to show to the user.
     */
    private static void setUpHelpBoard() {
        int linesPerPage = 10;

        completeHelpBoard = new ArrayList<>();
        String header = getPrefixedMessage("help-header");
        String footer = messageKeysMap.get("help-footer");
        String[] messageBody = messageKeysMap.get("help-body").split("\n", -1);
        StringBuilder message = new StringBuilder();
        int lineNum = 1;
        int currentPage = 1;
        for (String body : messageBody) {
            if (lineNum == 1) {
                message = new StringBuilder(header + "\n");
            }
            message.append(body).append("\n");
            if (lineNum % linesPerPage == 0) {
                currentPage++;
                lineNum = 0;
                message = new StringBuilder(message.append(footer).append("\n").toString().replaceAll("%page%", String.valueOf(currentPage)));
                completeHelpBoard.add(message.toString());
            }
            lineNum++;
        }
        completeHelpBoard.add(message.toString());
    }

    /**
     * Shows help menu to the user.
     *
     * @param sender sender to send message to
     * @param pageNum page number to view
     */
    public static void showHelpBoard(CommandSender sender, int pageNum) {
        int index = pageNum - 1;
        if (pageNum > completeHelpBoard.size()) {
            sender.sendMessage(completeHelpBoard.get(completeHelpBoard.size() - 1));
        } else {
            sender.sendMessage(completeHelpBoard.get(index));
        }
    }
}
