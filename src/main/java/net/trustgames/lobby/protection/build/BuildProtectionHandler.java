package net.trustgames.lobby.protection.build;

import net.trustgames.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class BuildProtectionHandler implements Listener {

    public BuildProtectionHandler(Lobby lobby) {
        Bukkit.getPluginManager().registerEvents(this, lobby);
    }

    // using Set as to prevent duplicates
    public static final Set<UUID> allowedPlayers = new HashSet<>();


    @EventHandler(priority = EventPriority.NORMAL)
    private void onBlockPlace(BlockPlaceEvent event) {
        if (!allowedPlayers.contains(event.getPlayer().getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onBlockBreak(BlockBreakEvent event) {
        if (!allowedPlayers.contains(event.getPlayer().getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerQuit(PlayerQuitEvent event) {
        allowedPlayers.remove(event.getPlayer().getUniqueId());
    }
}
