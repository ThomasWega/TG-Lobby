package net.trustgames.lobby.protection.build;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.trustgames.toolkit.config.CommandConfig;
import net.trustgames.toolkit.utils.MiniMessageUtils;

public enum BuildProtectionConfig {
    SENDER_ON(CommandConfig.PREFIX.value + "<green>From now on, you can place or break blocks"),
    SENDER_OFF(CommandConfig.PREFIX.value + "<red>From now on, you can't place or break blocks"),
    SENDER_ON_OTHER(CommandConfig.PREFIX.value + "<green>You've allowed player <component> to break or place blocks"),
    SENDER_OFF_OTHER(CommandConfig.PREFIX.value + "<red>You've disallowed player <component> to break or place blocks"),
    TARGET_ON(CommandConfig.PREFIX.value + "<green>You've been allowed to break or place blocks by player <white><component>"),
    TARGET_OFF(CommandConfig.PREFIX.value + "<red>You've been disallowed to break or place blocks by player <white><component>"),
    TARGET_ON_SILENT(SENDER_ON_OTHER.message + ". <white>The player won't be notified about this"),
    TARGET_OFF_SILENT(SENDER_OFF_OTHER.message + ". <white>The player won't not be notified about this");


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
