package net.trustgames.lobby.protection.build;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.trustgames.core.config.CommandConfig;

public enum BuildProtectionConfig {
    ON(CommandConfig.PREFIX.value + "<green>From now on, you can place or break blocks"),
    OFF(CommandConfig.PREFIX.value + "<red>From now on, you can't place or break blocks");

    private final String message;

    BuildProtectionConfig(String message) {
        this.message = message;
    }

    /**
     * @return Formatted component message
     */
    public Component getMessage() {
        return MiniMessage.miniMessage().deserialize(message);
    }
}
