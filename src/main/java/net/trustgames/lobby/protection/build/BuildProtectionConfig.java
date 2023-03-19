package net.trustgames.lobby.protection.build;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.trustgames.core.config.CommandConfig;
import net.trustgames.core.utils.MiniMessageUtils;

public enum BuildProtectionConfig {
    ON(CommandConfig.PREFIX.value + "<green>From now on, you can place or break blocks"),
    OFF(CommandConfig.PREFIX.value + "<red>From now on, you can't place or break blocks"),
    ON_OTHER(CommandConfig.PREFIX.value + "<dark_gray>You've allowed player <component> to break or place blocks"),
    OFF_OTHER(CommandConfig.PREFIX.value + "<dark_gray>You've disallowed player <component> to break or place blocks");


    private final String message;

    BuildProtectionConfig(String message) {
        this.message = message;
    }

    /**
     * @return Formatted component message
     */
    public final Component getMessage() {
        return MiniMessage.miniMessage().deserialize(message);
    }

    /**
     * {@literal Replace <component> tag with given Component}
     *
     * @param component Component to replace the tag with
     * @return New formatted Component with replaced id tag
     */
    public final Component addComponent(Component component) {
        return MiniMessageUtils.component(component).deserialize(message);
    }
}
