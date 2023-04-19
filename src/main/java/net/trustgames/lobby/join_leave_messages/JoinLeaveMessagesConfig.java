package net.trustgames.lobby.join_leave_messages;

import net.kyori.adventure.text.Component;
import net.trustgames.core.managers.LuckPermsManager;
import net.trustgames.middleware.utils.MiniMessageUtils;
import org.bukkit.entity.Player;


public enum JoinLeaveMessagesConfig {
    JOIN("<color:#6b8ae8>Join | </color>"),
    LEAVE("<color:#6b8ae8>Leave | </color>"),
    JOIN_MSG(JOIN.message + "<prefix><hover:show_text:'TO ADD...'><click:run_command:'/say <player_name> TO ADD...'><white><player_name></click></hover> <gray>has joined the lobby!"),
    LEAVE_MSG(LEAVE.message + "<prefix><hover:show_text:'TO ADD...'><click:run_command:'/say <player_name> TO ADD...'><white><player_name></click></hover> <gray>has left the lobby!");


    private final String message;

    JoinLeaveMessagesConfig(String message) {
        this.message = message;
    }

    /**
     * Replace tags with player info
     *
     * @param player Player to replace the tags with info of
     * @return New formatted Component message with replaced tags
     */
    public final Component formatMessage(Player player) {
        return MiniMessageUtils.withPrefix(player.getName(), LuckPermsManager.getPlayerPrefix(player))
                .deserialize(message);
    }
}
