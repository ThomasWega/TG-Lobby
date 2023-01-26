package net.trustgames.lobby.spawn;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import net.trustgames.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;


/**
 * Events which handle the teleportation to spawn on player join or death.
 * Also has method to get the spawn.yml file
 */
public class Spawn implements Listener {

    private final Lobby lobby;

    public Spawn(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        YamlConfiguration config = YamlConfiguration.loadConfiguration(getSpawnFile());
        Location location = config.getLocation("spawn.location");
        if (location != null) {
            player.teleport(location);
        } else {
            Bukkit.getLogger().severe("Spawn location isn't set!");
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerPostRespawnEvent event) {
        Player player = event.getPlayer();

        YamlConfiguration config = YamlConfiguration.loadConfiguration(getSpawnFile());
        Location location = config.getLocation("spawn.location");
        if (location != null) {
            player.teleport(location);
        } else {
            Bukkit.getLogger().severe("Spawn location isn't set!");
            player.sendMessage(ChatColor.RED + "Spawn location isn't set! Set it with /setspawn");
        }
    }

    public File getSpawnFile() {
        return new File(lobby.getDataFolder(), "spawn.yml");
    }
}
