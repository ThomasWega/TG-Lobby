package net.trustgames.lobby.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * Sets the config defaults for the default config (config.yml)
 */
public class DefaultConfig {

    /**
     * Create the defalts for the default config (config.yml)
     *
     * @param defaultConfig plugin.getConfig()
     */
    public static void create(@NotNull FileConfiguration defaultConfig) {

        // messages
        defaultConfig.addDefault("messages.spawn-teleport", "&9Spawn> &8You've been teleported to the spawn location.");

        // settings
        defaultConfig.addDefault("settings.cooldowns.spawn-command-cooldown", 3d);

    }
}

