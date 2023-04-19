package net.trustgames.lobby.protection.build;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;

public final class BuildProtectionHandler implements Listener {

    // using Set as to prevent duplicates
    public static final Set<String> allowedPlayers = new HashSet<>();


    @EventHandler(priority = EventPriority.NORMAL)
    private void onBlockPlace(BlockPlaceEvent event) {
        String playerName = event.getPlayer().getName();
        if (!allowedPlayers.contains(playerName))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onBlockBreak(BlockBreakEvent event) {
        String playerName = event.getPlayer().getName();
        if (!allowedPlayers.contains(playerName))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        allowedPlayers.remove(playerName);
    }
}
