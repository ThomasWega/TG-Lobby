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

        // tablist
        defaultConfig.addDefault("tablist.header", List.of("&f&lTRUSTGAMES &f- &7Chillin' on the hub"));
        defaultConfig.addDefault("tablist.footer", List.of("&fCheck out &astore.trustgames.net&f for Ranks and Benefits"));


    }
}

