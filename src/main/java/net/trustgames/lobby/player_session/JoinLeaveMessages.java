package net.trustgames.lobby.player_session;

import net.trustgames.lobby.config.session.JoinLeaveMessagesConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveMessages implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        event.joinMessage(JoinLeaveMessagesConfig.JOIN_MSG.formatMessage(player));
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.quitMessage(JoinLeaveMessagesConfig.LEAVE_MSG.formatMessage(player));
    }
}
