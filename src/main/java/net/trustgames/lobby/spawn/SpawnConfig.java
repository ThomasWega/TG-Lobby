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

public enum SpawnConfig {
    SPAWN_TP(CommandConfig.PREFIX.value + "<dark_gray>You've been teleported to the spawn location"),
    SPAWN_TP_OTHER(CommandConfig.PREFIX.value + "<dark_gray>You've teleported the player(s) to the spawn location");

    private final String message;

    SpawnConfig(String message) {
        this.message = message;
    }

    public static File getSpawnFile(Lobby lobby) {
        return new File(lobby.getDataFolder(), "spawn.yml");
    }

    /**
     * @return Formatted component message
     */
    public final Component getMessage() {
        return MiniMessage.miniMessage().deserialize(message);
    }

    @Getter @Setter
    @Nullable
    private static Location spawnLocation;

    /**
     * Updates the spawn location saved in the config file
     *
     * @param lobby Instance of Lobby
     * @param location new Spawn Location
     * @return If setting new location was successful
     */
    public static boolean updateSpawnLocation(Lobby lobby, Location location){
        File spawnFile = getSpawnFile(lobby);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(spawnFile);
        config.set("spawn.location", location);
        try {
            config.save(spawnFile);
            spawnLocation = location;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
