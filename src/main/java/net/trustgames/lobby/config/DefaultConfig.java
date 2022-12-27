package net.trustgames.lobby.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class DefaultConfig {
    public static void create(@NotNull FileConfiguration defaultConfig) {

        // messages
        defaultConfig.addDefault("messages.spawn-teleport", "&7You've been teleported to the spawn location.");

        // permissions
        defaultConfig.addDefault("permissions.admin", "lobby.admin");
        defaultConfig.addDefault("permissions.staff", "lobby.staff");
        defaultConfig.addDefault("permissions.trust+", "lobby.trust+");
        defaultConfig.addDefault("permissions.trust", "lobby.trust");
        defaultConfig.addDefault("permissions.vip+", "lobby.vip+");
        defaultConfig.addDefault("permissions.vip", "lobby.vip");

        // settings
    //    defaultConfig.addDefault("settings.max-commands-per-second", 10d);
    }
}

