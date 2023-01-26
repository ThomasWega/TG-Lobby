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

        // prefixes
        String prefix_spawn = "&#dbfca4Spawn | ";
        String prefix_join = "&#6b8ae8Join | ";
        String prefix_leave = "&#6b8ae8Leave | ";

        // messages
        defaultConfig.addDefault("messages.spawn-teleport",
                prefix_spawn + "&8You've been teleported to the spawn location.");

        // settings
        defaultConfig.addDefault("settings.cooldowns.spawn-command-cooldown", 3d);
        defaultConfig.addDefault("settings.double-jump.horizontal", 0.8d);
        defaultConfig.addDefault("settings.double-jump.vertical", 0.8d);
        defaultConfig.addDefault("settings.double-jump.horizontal-sprint", 0.85d);
        defaultConfig.addDefault("settings.double-jump.vertical-sprint", 0.6d);

        // join-leave messages
        defaultConfig.addDefault("join-leave.messages.join", prefix_join + "&e%1$s &7has joined the lobby!");
        defaultConfig.addDefault("join-leave.messages.leave", prefix_leave + "&e%1$s &7has left the lobby!");


    }
}

