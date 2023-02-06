package net.trustgames.lobby.config.session;

import net.kyori.adventure.text.Component;
import net.trustgames.core.utils.MiniMessageUtils;
import org.bukkit.entity.Player;

public enum JoinLeaveMessagesConfig {
    JOIN("<color:#6b8ae8>Join | </color>"),
    LEAVE("&#6b8ae8Leave | "),
    JOIN_MSG(JOIN.message + "<yellow><prefix></yellow> <white><player_name></white> <gray>has joined the lobby!</gray>"),
    LEAVE_MSG(LEAVE.message + "<yellow><prefix></yellow> <white><player_name></white> <gray>has left the lobby!</gray>");


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
    public Component formatMessage(Player player) {
        return MiniMessageUtils.format(player).deserialize(message);
    }
}
