package tk.taverncraft.playerhide.utils;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.ChatPaginator;

import static org.bukkit.util.ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH;

/**
 * MessageManager handles all formatting and sending of messages to the command sender.
 */
public class MessageManager {
    private static final HashMap<String, String> messageKeysMap = new HashMap<>();

    /**
     * Sets the messages to use.
     *
     * @param lang the configuration to base the messages on
     */
    public static void setMessages(FileConfiguration lang) {
        Set<String> messageKeysSet = lang.getConfigurationSection("").getKeys(false);

        for (String messageKey : messageKeysSet) {
            messageKeysMap.put(messageKey, formatMessageColor(lang.get(messageKey).toString() + " "));
        }
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
     * Shows help menu to the user.
     *
     * @param sender sender to send message to
     * @param pageNum page number to view
     */
    public static void showHelpBoard(CommandSender sender, int pageNum) {
        int linesPerPage = 12;
        int positionsPerPage = 10;

        String header = getPrefixedMessage("help-header");
        String footer = messageKeysMap.get("help-footer");
        String[] messageBody = messageKeysMap.get("help-body").split("\n", -1);
        StringBuilder message = new StringBuilder(header + "\n");
        int position = 1;
        int currentPage = 1;
        for (String body : messageBody) {
            message.append(body).append("\n");
            if (position % positionsPerPage == 0) {
                currentPage++;
                message = new StringBuilder(message.append(footer).append("\n").toString().replaceAll("%page%", String.valueOf(currentPage)));
                message.append(header).append("\n");
            }
            position++;
        }

        ChatPaginator.ChatPage page = ChatPaginator.paginate(message.toString(), pageNum, GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH, linesPerPage);
        for (String line : page.getLines()) {
            sender.sendMessage(line);
        }
    }

    /**
     * Formats color in chat messages.
     *
     * @param message message to format
     */
    private static String formatMessageColor(String message) {
        Pattern pattern = Pattern.compile("(?<!\\\\)#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.substring(0, matcher.start()) + builder.toString() + message.substring(matcher.end());
            matcher = pattern.matcher(message);
        }

        message = message.replace("\\#", "#");
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
