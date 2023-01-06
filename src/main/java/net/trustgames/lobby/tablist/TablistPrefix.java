package net.trustgames.lobby.tablist;

import net.trustgames.core.managers.PrefixManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TablistPrefix implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        PrefixManager.tablistPrefix();
    }
}
