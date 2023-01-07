package net.trustgames.lobby.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DefaultConfig {
    public static void create(@NotNull FileConfiguration defaultConfig) {

        // messages
        defaultConfig.addDefault("messages.spawn-teleport", "&9Spawn> &8You've been teleported to the spawn location.");

        // settings
        defaultConfig.addDefault("settings.cooldowns.spawn-command-cooldown", 3d);

    }
}

