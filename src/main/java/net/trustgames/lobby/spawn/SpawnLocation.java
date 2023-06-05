package net.trustgames.lobby.spawn;

import net.trustgames.lobby.Lobby;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class SpawnLocation {
    private static @Nullable Location location;


    public static File getFile(Lobby lobby) {
        return new File(lobby.getDataFolder(), "spawn.yml");
    }

    /**
     * @param lobby Plugin instance
     * @return The current location variable or if null, the variable in config
     */
    public static @Nullable Location getLocation(Lobby lobby) {
        if (location != null) return location;

        return YamlConfiguration.loadConfiguration(getFile(lobby)).getLocation("spawn.location");
    }

    /**
     * Updates the spawn location saved in the config file
     *
     * @param lobby    Instance of Lobby
     * @param newLoc new Spawn Location
     * @return If setting new location was successful
     */
    public static boolean setLocation(Lobby lobby, Location newLoc) {
        File spawnFile = getFile(lobby);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(spawnFile);
        config.set("spawn.location", newLoc);
        try {
            config.save(spawnFile);
            location = newLoc;
            return true;
        } catch (IOException e) {
            Lobby.LOGGER.error("Unable to save new location  to spawn config", e);
            return false;
        }
    }
}
