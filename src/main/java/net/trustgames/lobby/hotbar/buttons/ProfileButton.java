package net.trustgames.lobby.hotbar.buttons;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.trustgames.core.managers.gui.buttons.HotbarGUIButton;
import net.trustgames.core.managers.item.SkullBuilder;

import java.util.List;

public class ProfileButton {
    @Getter
    private final HotbarGUIButton button = new HotbarGUIButton()
            .creator(player -> new SkullBuilder()
                    .owner(player)
                    .displayName(Component.text("My profile", NamedTextColor.GREEN)
                            .appendSpace()
                            .append(Component.text("(right-click)", NamedTextColor.GRAY))
                    )
                    .lore(List.of(
                                    Component.text("Check your profile stats", NamedTextColor.DARK_GRAY),
                                    Component.text("or do certain actions", NamedTextColor.DARK_GRAY),
                                    Component.text("like adding a friend!", NamedTextColor.DARK_GRAY)
                            )
                    )
                    .hideFlags()
            )
            .event(event -> event.getWhoClicked().sendMessage(Component.text("TO ADD")))
            .eventHotbar(event -> event.getPlayer().sendMessage(Component.text("TO ADD")));
}
