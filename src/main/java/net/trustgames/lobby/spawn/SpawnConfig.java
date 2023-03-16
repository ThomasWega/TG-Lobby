package net.trustgames.lobby.spawn;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.trustgames.core.config.CommandConfig;
import net.trustgames.lobby.Lobby;

import java.io.File;

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
}
