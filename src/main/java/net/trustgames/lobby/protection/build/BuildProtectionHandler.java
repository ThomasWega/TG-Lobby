package net.trustgames.lobby.protection.build;

import net.trustgames.core.gui.type.InventoryHandler;
import net.trustgames.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;

public final class BuildProtectionHandler implements Listener {

    public BuildProtectionHandler(Lobby lobby) {
        Bukkit.getPluginManager().registerEvents(this, lobby);
    }

    private static final Map<UUID, InventoryHandler> allowedPlayersMap = BuildProtectionAllowedPlayersMap.getAllowedMap();
    private static final Map<UUID, GameMode> gameModesMap = BuildProtectionAllowedPlayersMap.GameModesMap.getGamemodesMap();

    @EventHandler(priority = EventPriority.NORMAL)
    private void onBlockPlace(BlockPlaceEvent event) {
        if (!allowedPlayersMap.containsKey(event.getPlayer().getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onBlockBreak(BlockBreakEvent event) {
        if (!allowedPlayersMap.containsKey(event.getPlayer().getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        allowedPlayersMap.remove(uuid);

        GameMode gameMode = gameModesMap.get(uuid);
        if (gameMode == null) return;
        player.setGameMode(gameMode);
        gameModesMap.remove(uuid);
    }
}
