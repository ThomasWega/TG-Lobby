package net.trustgames.lobby.spawn;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import net.trustgames.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static net.trustgames.lobby.Lobby.LOGGER;


/**
 * Events which handle the teleportation to spawn on player join or death.
 * Also has method to get the spawn.yml file
 */
public final class SpawnHandler implements Listener {
    private final Location spawnLocation;

    public SpawnHandler(Lobby lobby) {
        this.spawnLocation = SpawnLocation.getLocation(lobby);
        Bukkit.getPluginManager().registerEvents(this, lobby);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        handleTP(player);
    }

    @EventHandler
    private void onPlayerDeath(PlayerPostRespawnEvent event) {
        Player player = event.getPlayer();

        handleTP(player);
    }

    private void handleTP(Player player) {
        if (spawnLocation == null) {
            LOGGER.warn("Spawn location isn't set!");
            return;
        }
        player.teleport(spawnLocation);
    }
}
