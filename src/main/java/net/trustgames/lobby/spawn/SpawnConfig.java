package net.trustgames.lobby.spawn;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.trustgames.toolkit.config.CommandConfig;

public enum SpawnConfig {
    SPAWN_TP(CommandConfig.PREFIX.getValue() + "<dark_gray>You've been teleported to the spawn location"),
    SPAWN_TP_OTHER(CommandConfig.PREFIX.getValue() + "<dark_gray>You've teleported the player(s) to the spawn location");

    private final String message;

    SpawnConfig(String message) {
        this.message = message;
    }

    /**
     * @return Formatted component message
     */
    public final Component getFormatted() {
        return MiniMessage.miniMessage().deserialize(message);
    }
}
