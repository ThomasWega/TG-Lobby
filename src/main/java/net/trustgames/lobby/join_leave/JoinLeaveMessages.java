package net.trustgames.lobby.join_leave;

import net.kyori.adventure.text.Component;
import net.trustgames.core.managers.LuckPermsManager;
import net.trustgames.core.utils.ColorUtils;
import net.trustgames.lobby.settings.LobbySettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveMessages implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String prefix = LuckPermsManager.getPlayerPrefix(player);
        String joinMessage = LobbySettings.JOIN_MSG;

        event.joinMessage(ColorUtils.color(String.format(joinMessage, prefix, player.getName())));
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String prefix = LuckPermsManager.getUser(player).getCachedData().getMetaData().getPrefix();
        String leaveMessage = LobbySettings.LEAVE_MSG;

        assert prefix != null;
        event.quitMessage(ColorUtils.color(String.format(leaveMessage, ColorUtils.stripColor(Component.text(prefix)), player.getName())));
    }
}
