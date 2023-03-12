package net.trustgames.lobby.join_leave_messages;

import net.trustgames.core.Core;
import net.trustgames.core.cache.UUIDCache;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class JoinLeaveMessagesHandler implements Listener {

    private final UUIDCache uuidCache;

    public JoinLeaveMessagesHandler(Core core) {
        this.uuidCache = core.getUuidCache();
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null);
        uuidCache.get(event.getPlayer().getName(), uuid -> Bukkit.broadcast(JoinLeaveMessagesConfig.JOIN_MSG.formatMessage(uuid)));
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(null);
        uuidCache.get(event.getPlayer().getName(), uuid -> Bukkit.broadcast(JoinLeaveMessagesConfig.LEAVE_MSG.formatMessage(uuid)));
    }
}
