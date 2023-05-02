package net.trustgames.lobby.spawn;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import net.trustgames.lobby.Lobby;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static net.trustgames.lobby.Lobby.LOGGER;


/**
 * Events which handle the teleportation to spawn on player join or death.
 * Also has method to get the spawn.yml file
 */
public final class SpawnHandler implements Listener {

    private final YamlConfiguration config;

    public SpawnHandler(Lobby lobby) {
        this.config = YamlConfiguration.loadConfiguration(SpawnConfig.getSpawnFile(lobby));
        SpawnConfig.setSpawnLocation(config.getLocation("spawn.location"));
    }

    @EventHandler(priority = EventPriority.LOW)
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Location location = config.getLocation("spawn.location");
        if (location == null) {
            LOGGER.severe("Spawn location isn't set!");
            return;
        }

        player.teleportAsync(location);
    }

    @EventHandler(priority = EventPriority.LOW)
    private void onPlayerDeath(PlayerPostRespawnEvent event) {
        Player player = event.getPlayer();

        Location location = config.getLocation("spawn.location");
        if (location == null) {
            LOGGER.severe("Spawn location isn't set!");
            return;
        }

        player.teleportAsync(location);
    }
}
