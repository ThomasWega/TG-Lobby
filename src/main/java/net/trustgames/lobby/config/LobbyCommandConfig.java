package net.trustgames.lobby.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.trustgames.core.config.CommandConfig;

public enum LobbyCommandConfig {
    SPAWN_TP(CommandConfig.PREFIX.value + "<dark_gray>You've been teleported to the spawn location.");

    private final String message;

    LobbyCommandConfig(String message) {
        this.message = message;
    }

    /**
     * @return Formatted component message
     */
    public Component getMessage() {
        return MiniMessage.miniMessage().deserialize(message);
    }
}
