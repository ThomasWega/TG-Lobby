package net.trustgames.lobby.protection.build;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.trustgames.core.config.CommandConfig;
import net.trustgames.core.utils.MiniMessageUtils;

public enum BuildProtectionConfig {
    ON(CommandConfig.PREFIX.value + "<green>From now on, you can place or break blocks"),
    OFF(CommandConfig.PREFIX.value + "<red>From now on, you can't place or break blocks"),
    ON_OTHER(CommandConfig.PREFIX.value + "<dark_gray>You've allowed player <player_name> to break or place blocks"),
    OFF_OTHER(CommandConfig.PREFIX.value + "<dark_gray>You've disallowed player <player_name> to break or place blocks");


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

    /**
     * {@literal Replace <player_name> tag with given component}
     *
     * @param component Component to replace the tag with
     * @return New formatted Component with replaced component tag
     */
    public Component addName(Component component) {
        return MiniMessageUtils.addName(component).deserialize(message);
    }
}
