package net.trustgames.lobby.join_leave_messages;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class JoinLeaveMessagesHandler implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    private void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null);
        Bukkit.broadcast(JoinLeaveMessagesConfig.JOIN_MSG.formatMessage(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(null);
        Bukkit.broadcast(JoinLeaveMessagesConfig.LEAVE_MSG.formatMessage(event.getPlayer()));
    }
}
