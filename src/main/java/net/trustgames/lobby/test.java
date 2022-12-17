package net.trustgames.lobby;

import net.trustgames.core.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.logging.Logger;

public class test implements Listener {

    private final Lobby lobby;

    public test(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Core core = lobby.getCore();
        core.activityListener.onTestLobby();
    }
}
