package net.trustgames.lobby.config.session;

import net.kyori.adventure.text.Component;
import net.trustgames.core.utils.MiniMessageUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

public enum JoinLeaveMessagesConfig {
    JOIN("<color:#6b8ae8>Join | </color>"),
    LEAVE("<color:#6b8ae8>Leave | </color>"),
    JOIN_MSG(JOIN.message + "<yellow><prefix></yellow><white><player_name></white> <gray>has joined the lobby!</gray>"),
    LEAVE_MSG(LEAVE.message + "<yellow><prefix> </yellow><white><player_name></white> <gray>has left the lobby!</gray>");


    private final String message;

    JoinLeaveMessagesConfig(String message) {
        this.message = message;
    }

    /**
     * Replace tags with player info
     *
     * @param uuid UUID of Player to replace the tags with info of
     * @return New formatted Component message with replaced tags
     */
    public Component formatMessage(UUID uuid) {
        return MiniMessageUtils.format(uuid).deserialize(message);
    }
}
