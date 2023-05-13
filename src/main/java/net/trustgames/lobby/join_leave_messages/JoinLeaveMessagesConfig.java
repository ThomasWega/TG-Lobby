package net.trustgames.lobby.join_leave_messages;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;


public enum JoinLeaveMessagesConfig {
    JOIN("<color:#6b8ae8>Join | </color>"),
    LEAVE("<color:#6b8ae8>Leave | </color>"),
    JOIN_MSG(JOIN.message + "<tg_player_prefix_spaced><yellow><player_name><gray> has joined the lobby"),
    LEAVE_MSG(LEAVE.message + "<tg_player_prefix_spaced><yellow><player_name><gray> has left the lobby");


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
        return MiniMessage.miniMessage().deserialize(
                message,
                MiniPlaceholders.getAudiencePlaceholders(player)
        );
    }
}
