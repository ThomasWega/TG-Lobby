package net.trustgames.lobby.join_leave_messages;

import net.trustgames.core.cache.EntityCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class JoinLeaveMessagesHandler implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = EntityCache.getUUID(event.getPlayer());

        event.joinMessage(JoinLeaveMessagesConfig.JOIN_MSG.formatMessage(uuid));
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = EntityCache.getUUID(event.getPlayer());

        event.quitMessage(JoinLeaveMessagesConfig.LEAVE_MSG.formatMessage(uuid));
    }
}
