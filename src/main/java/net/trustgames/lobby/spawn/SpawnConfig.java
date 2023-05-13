package net.trustgames.lobby.spawn;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.trustgames.lobby.Lobby;
import net.trustgames.toolkit.config.CommandConfig;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public enum SpawnConfig {
    SPAWN_TP(CommandConfig.PREFIX.getString() + "<dark_gray>You've been teleported to the spawn location"),
    SPAWN_TP_OTHER(CommandConfig.PREFIX.getString() + "<dark_gray>You've teleported the player(s) to the spawn location");

    @Getter
    @Setter
    @Nullable
    private static Location spawnLocation;
    private final String message;

    SpawnConfig(String message) {
        this.message = message;
    }

    public static File getSpawnFile(Lobby lobby) {
        return new File(lobby.getDataFolder(), "spawn.yml");
    }

    /**
     * Updates the spawn location saved in the config file
     *
     * @param lobby    Instance of Lobby
     * @param location new Spawn Location
     * @return If setting new location was successful
     */
    public static boolean updateSpawnLocation(Lobby lobby, Location location) {
        File spawnFile = getSpawnFile(lobby);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(spawnFile);
        config.set("spawn.location", location);
        try {
            config.save(spawnFile);
            spawnLocation = location;
            return true;
        } catch (IOException e) {
            Lobby.LOGGER.log(Level.SEVERE, "Unable to save new location to spawn config", e);
            return false;
        }
    }

    /**
     * @return Formatted component message
     */
    public final Component getFormatted() {
        return MiniMessage.miniMessage().deserialize(message);
    }
}
