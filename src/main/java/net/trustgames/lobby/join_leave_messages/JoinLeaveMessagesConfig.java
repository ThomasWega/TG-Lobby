package net.trustgames.lobby.join_leave_messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.trustgames.core.managers.LuckPermsManager;
import net.trustgames.toolkit.utils.MiniMessageUtils;
import org.bukkit.entity.Player;


public enum JoinLeaveMessagesConfig {
    JOIN("<color:#6b8ae8>Join | </color>"),
    LEAVE("<color:#6b8ae8>Leave | </color>"),
    JOIN_MSG(JOIN.message + "<player_prefix><player_display> <gray>has joined the lobby"),
    LEAVE_MSG(LEAVE.message + "<player_prefix><player_display> <gray>has left the lobby");


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
                .deserialize(message, TagResolver.resolver("player_display",
                        Tag.selfClosingInserting(player.displayName())));
    }
}
