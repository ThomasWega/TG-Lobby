package net.trustgames.lobby.join_leave;

import net.trustgames.core.managers.LuckPermsManager;
import net.trustgames.core.utils.ColorUtils;
import net.trustgames.lobby.Lobby;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveMessages implements Listener {

    private final Lobby lobby;

    public JoinLeaveMessages(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event){
        FileConfiguration config = lobby.getConfig();

        Player player = event.getPlayer();
        String prefix = LuckPermsManager.getPlayerPrefix(player);
        String joinMessage = config.getString("join-leave.messages.join");

        assert joinMessage != null;
        event.joinMessage(ColorUtils.color(String.format(joinMessage, prefix, player.getName())));
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        FileConfiguration config = lobby.getConfig();

        Player player = event.getPlayer();
        String prefix = LuckPermsManager.getUser(player).getCachedData().getMetaData().getPrefix();
        String leaveMessage = config.getString("join-leave.messages.leave");

        assert leaveMessage != null;
        event.quitMessage(ColorUtils.color(String.format(leaveMessage, prefix, player.getName())));
    }
}
